package com.example.dollarschat.screens.UserProfileScreen

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dollarschat.data.*
import com.example.dollarschat.data.localBase.AvatarDatabase.AvatarsRepository
import com.example.dollarschat.handler.EventHandler
import com.example.dollarschat.screens.UserProfileScreen.models.ProfileEvent
import com.example.dollarschat.screens.UserProfileScreen.models.ProfileState
import com.example.dollarschat.screens.authScreens.UserCreate.models.UserCreateEvent
import com.example.dollarschat.screens.authScreens.UserCreate.models.UserCreateState
import com.example.dollarschat.ui.theme.DollarsStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel
@Inject constructor(@ApplicationContext application: Context, private val avatarsRepository: AvatarsRepository, ):
    ViewModel(), EventHandler<ProfileEvent> {
    val USER_LOGIN: Pattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9\\-]{0,10}")
    val _userProfileViewState: MutableLiveData<ProfileState> = MutableLiveData(ProfileState.ProfileInfo)
    val userCreateViewState: LiveData<ProfileState> = _userProfileViewState
    val firebaseHelper = FirebaseHelper(application)
    val settings = Settings(application)


    override fun obtainEvent(event: ProfileEvent) {
        when (val currentState = _userProfileViewState.value) {
            is ProfileState.Loading -> loadAvatars()
              is ProfileState.ProfileInfo -> reduce(event,currentState)
            is ProfileState.ChangeUserProfile->reduce(event,currentState)
            else -> {}
        }
    }

    private fun reduce(event: ProfileEvent, createState: ProfileState.ProfileInfo) {
        when (event) {
            is ProfileEvent.Loading ->loadAvatars()
            is ProfileEvent.ChangeClick -> _userProfileViewState.postValue(ProfileState.Loading)
            else -> {}
        }

    }
    private fun reduce(event: ProfileEvent,createState: ProfileState.ChangeUserProfile){
        when(event){
            is ProfileEvent.ProfileInfo ->_userProfileViewState.postValue(ProfileState.ProfileInfo)
            is ProfileEvent.ChangeUserProfile -> changeUserProfile(event,createState)
            is ProfileEvent.ChangeEnteredLogin -> checkUserLogin(event,createState)
            else ->{}
        }

    }
    private fun checkUserLogin(event: ProfileEvent.ChangeEnteredLogin,curentState: ProfileState.ChangeUserProfile){
        val loginCheck = event.newValue.matches(USER_LOGIN.toRegex())
        _userProfileViewState.postValue(curentState.copy(newUserLoginCheck = loginCheck, newUserLogin = event.newValue))
    }
    private fun changeUserProfile(event: ProfileEvent.ChangeUserProfile,curentState:ProfileState.ChangeUserProfile){
        viewModelScope.launch(Dispatchers.IO) {
            firebaseHelper.changeUserProfile(event.newAnatar,event.newLogin)
            if (event.newLogin!=""&& curentState.newUserLoginCheck){
                settings.changeLocalUser(USER_PROFILE_NAME,event.newLogin)
            }

            settings.changeLocalUser(USER_PROFILE_AVATAR, avtar = event.newAnatar)
            val pro = firebaseHelper.getLoacalUser()
            pro?.let {
                event.changelocalUser(pro)
                event.changeMainColor(when(pro.avatar?.color){
                    "blue" -> { DollarsStyle.Blue}
                    "black"->(DollarsStyle.Black)
                    "green"->{ DollarsStyle.Green}
                    "yellow"->{ DollarsStyle.Yellow}
                    "red"->{ DollarsStyle.Red}
                    else->{ DollarsStyle.Blue}
                })
            }
            _userProfileViewState.postValue(ProfileState.ProfileInfo)
        }
    }

    private fun loadAvatars(){
       viewModelScope.launch(Dispatchers.IO) {
           val avatars = avatarsRepository.getAll()
           val packs = firebaseHelper.avatarPacksLoader()
           val user = firebaseHelper.getLoacalUser()
           val userPacks = firebaseHelper.getAccesAvatar(user)
           val accesPacks = ArrayList<Pair<String,Boolean>>()
           for (i in packs.indices){
               val index = i
               var access = false
               for ( c in userPacks.indices){
                   if (packs[index]==userPacks[c]){
                       access = true
                   }
               }
               accesPacks.add(Pair(packs[index],access))
           }
           _userProfileViewState.postValue(ProfileState.ChangeUserProfile(avatars,accesPacks))

       }

    }
}