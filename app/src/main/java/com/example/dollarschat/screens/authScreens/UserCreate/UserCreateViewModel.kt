package com.example.dollarschat.screens.authScreens.UserCreate

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteConstraintException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dollarschat.data.FirebaseHelper
import com.example.dollarschat.data.TABLE_NAME
import com.example.dollarschat.data.TABLE_NAME_AVATARS
import com.example.dollarschat.data.localBase.AvatarDatabase.AvatarEntity
import com.example.dollarschat.data.localBase.AvatarDatabase.AvatarsRepository
import com.example.dollarschat.handler.EventHandler
import com.example.dollarschat.screens.authScreens.SignIn.models.SignInEvent
import com.example.dollarschat.screens.authScreens.UserCreate.models.UserCreateEvent
import com.example.dollarschat.screens.authScreens.UserCreate.models.UserCreateState
import com.example.dollarschat.ui.theme.DollarsStyle
import com.example.dollarschat.ui.theme.DollarsTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.regex.Pattern
import javax.inject.Inject
@HiltViewModel
class UserCreateViewModel @Inject constructor(@ApplicationContext application: Context,
                                              private val avatarsRepository: AvatarsRepository): ViewModel(), EventHandler<UserCreateEvent> {
    val _userCreateViewState: MutableLiveData<UserCreateState> = MutableLiveData(UserCreateState.Loading)
    val userCreateViewState: LiveData<UserCreateState> = _userCreateViewState
   // val USER_LOGIN: Pattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9\\-]{0,10}")
    private val TAG:String = "authCheck"
    val firebaseHelper = FirebaseHelper(application)
    var profilePref: SharedPreferences = application.getSharedPreferences(TABLE_NAME,Context.MODE_PRIVATE)
    var imagePref:SharedPreferences = application.getSharedPreferences(TABLE_NAME_AVATARS,Context.MODE_PRIVATE)


    override fun obtainEvent(event: UserCreateEvent) {
        when(val currentState = _userCreateViewState.value){
            is UserCreateState.Loading-> reduce(event,currentState)
            is UserCreateState.CreateUser -> reduce(event,currentState)
            is UserCreateState.GoToChat -> reduce(event,currentState)
            else->{ }
        }
    }

    private fun reduce (event: UserCreateEvent,createState: UserCreateState.Loading){
        when(event){
            is UserCreateEvent.LoadingStartedPack -> loadStartedPack()
            else -> {}
        }

    }
    private fun loadStartedPack() {
    viewModelScope.launch {
        val bitmapRes=ArrayList<ImageBitmap>()
        val avatarsRef = firebaseHelper.getPackAvatar("StartedPack",true)
        for (i in avatarsRef.indices){
          val file = firebaseHelper.saveAvatarInFile(avatarsRef[i])

            if (file!=null && file.exists()){
                Log.d(TAG,"загрузка удалась")
                try {
                    val avatarEntity:AvatarEntity = AvatarEntity.avatarCreator(avatarsRef[i])
                   avatarsRepository.addNewAvatar(avatarEntity)
                }catch (e: SQLiteConstraintException){
                    Log.d(TAG,"загрузка не удалась")
                }
            }
            val bitmap = firebaseHelper.paintBitmap(avatarsRef[i].image_name)
            bitmap?.let { bitmapRes.add(bitmap) }
        }

        _userCreateViewState.postValue(UserCreateState.CreateUser(avatarsRef,bitmapRes))
    }
    }
    private fun reduce(event: UserCreateEvent,currentState: UserCreateState.CreateUser){
        when(event){
            is UserCreateEvent.LoadButtonClick-> goToChat(event,currentState)
            is UserCreateEvent.UserLoginChange -> _userCreateViewState.postValue(currentState.copy(userlogin = event.userLogin))
            is UserCreateEvent.AvatarChangeClick -> _userCreateViewState.postValue(currentState.copy(mainPage = event.imageIndex))
            else ->{}
        }
    }

    private fun goToChat(event: UserCreateEvent.LoadButtonClick,currentState: UserCreateState.CreateUser) {
        _userCreateViewState.postValue(currentState.copy(loadState = !currentState.loadState))
        viewModelScope.launch(Dispatchers.IO) {
            val check = firebaseHelper.createUserProfile(
                login = event.userLogin,
                avatar = event.image,
                profilePref,
                imagePref
            )
            if (check) {
                _userCreateViewState.postValue(UserCreateState.GoToChat)
            }else{_userCreateViewState.postValue(currentState.copy(loadState = false))}
        }
    }

    private fun reduce(event: UserCreateEvent,currentState: UserCreateState.GoToChat){
        when(event){

            is UserCreateEvent.GoToChat -> goToChatNavigate(event)
            else ->{}
        }
    }
    private fun goToChatNavigate(event: UserCreateEvent.GoToChat){
        val user =  firebaseHelper.getLoacalUser()
        when(user?.avatar?.color) {
                "blue"->{event.onSettingsChanged(DollarsStyle.Blue)}
                "black" ->{event.onSettingsChanged(DollarsStyle.Black)}
                "green" ->{event.onSettingsChanged(DollarsStyle.Green)}
                "yellow" ->  {event.onSettingsChanged(DollarsStyle.Yellow)}
                "red" -> {event.onSettingsChanged(DollarsStyle.Red)}
                else ->{ }
            }
        event.userInMemory(user!!)

        event.navController.navigate("main")
        _userCreateViewState.postValue(UserCreateState.stop)
    }
}