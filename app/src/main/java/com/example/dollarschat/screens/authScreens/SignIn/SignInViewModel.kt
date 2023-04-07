package com.example.dollarschat.screens.authScreens.SignIn

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dollarschat.data.*
import com.example.dollarschat.handler.EventHandler
import com.example.dollarschat.screens.authScreens.SignIn.models.SignInEvent
import com.example.dollarschat.screens.authScreens.SignIn.models.SignInState
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(@ApplicationContext application: Context): ViewModel(), EventHandler<SignInEvent> {
    val _signInViewState: MutableLiveData<SignInState> = MutableLiveData(SignInState.LoadingScreen)
    val signInViewState: LiveData<SignInState> = _signInViewState
    private val firebaseHelper = FirebaseHelper(application)
    private val TAG = "signIn"
    private val PASSWORD_PATTERN: Pattern =
        Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
    val MAIL_PATTERN:Pattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+")
    private val context = application
    var prefs:SharedPreferences = application.getSharedPreferences(TABLE_NAME,Context.MODE_PRIVATE)
    var imagePref:SharedPreferences = application.getSharedPreferences(TABLE_NAME_AVATARS,Context.MODE_PRIVATE)

    override fun obtainEvent(event: SignInEvent) {
        when(val currentState = _signInViewState.value){
            is SignInState.LoadingScreen -> reduce(event,currentState)
            is SignInState.SignIn -> reduce(event,currentState)

            else->{ }
        }
    }

    //Loading
    private fun reduce(event: SignInEvent, currentState: SignInState.LoadingScreen){
        when(event){
            is SignInEvent.loading -> authCheck(event)
            else ->{}
        }
    }
    private fun reduce(event: SignInEvent,currentSignInState: SignInState.SignIn){
        when(event){
            is SignInEvent.passChange -> { userPassCheck(event.newValue,currentSignInState)}
            is SignInEvent.emailChange ->{ userEmailCheck(event.newValue,currentSignInState)}
            is SignInEvent.changePassVisible -> _signInViewState.postValue(currentSignInState.copy(passwordVisible = !event.passVisible))
            is SignInEvent.loadButtonClick -> userSignIn(event,currentSignInState)
            else ->{}
        }
    }

    private fun userPassCheck(pass:String,currentSignInState: SignInState.SignIn){
        val check =  pass.matches(PASSWORD_PATTERN.toRegex())
        _signInViewState.postValue(currentSignInState.copy(passCheck = check, password = pass))
    }
    private fun userEmailCheck(email:String,currentSignInState: SignInState.SignIn){
        _signInViewState.postValue(currentSignInState.copy(mail = email))
        val emailCheck=email.matches(MAIL_PATTERN.toRegex())
        _signInViewState.postValue(currentSignInState.copy( emailCheck =emailCheck , mail = email))
    }

    private fun authCheck(event: SignInEvent.loading){
        viewModelScope.launch(Dispatchers.Main) {
            if (firebaseHelper.authCheck()){
                val userProfile = firebaseHelper.userProfileCheck()
                if (userProfile!=null){
                    val editor = prefs.edit()
                    editor.putString(USER_PROFILE_NAME, userProfile.login)
                    editor.putString(USER_PROFILE_AVATAR, userProfile.avatar?.image_name)
                    editor.putString(USER_PROFILE_ID, userProfile.id)
                    editor.putString(USER_PROFILE_ACCESSIBLE_AVATARS, userProfile.accessibleAvatars)
                    editor.putInt(USER_PROFILE_MESSAGE_QUANTITY, userProfile.messageQuantity!!)
                    editor.putInt(USER_PROFILE_ROOM_CREATE_QUANTITY,userProfile.roomsCreateQuantity!!)
                    editor.apply()
                    val imageEditor = imagePref.edit()
                    imageEditor.putString(IMAGE_NAME,userProfile.avatar?.image_name)
                    imageEditor.putString(IMAGE_COLOR,userProfile.avatar?.color)
                    imageEditor.putString(IMAGE_STORAGE_REF,userProfile.avatar?.storageReference)
                    imageEditor.putString(IMAGE_IVENT_PACKEGE_NAME,userProfile.avatar?.iventPackageName)
                    imageEditor.putBoolean(IMEGE_ACCESSABLE,userProfile.avatar?.accessAvatar!!)
                    imageEditor.apply()
                   // val database = Firebase.database
                    //val userRef = database.getReference(USER_PROFILE_REFERENCE)

                    event.navController.navigate("main")
                }
            }else{
                _signInViewState.postValue(SignInState.SignIn())
            }


        }

    }
    private fun userSignIn(event: SignInEvent.loadButtonClick,currentSignInState: SignInState.SignIn){
        viewModelScope.launch(Dispatchers.Main){
            _signInViewState.postValue(currentSignInState.copy(loadButtonState = !currentSignInState.loadingButtonState))
           if( firebaseHelper.signInUser(currentSignInState.mail,currentSignInState.password)){
               Log.d(TAG,"Вошел")
               val toast = Toast.makeText(context,"вошли",Toast.LENGTH_LONG)
               toast.show()
               event.navController.navigate("main")
           }else{
               val toast = Toast.makeText(context,"не вошли",Toast.LENGTH_LONG)
               toast.show()
           }

        }

    }

}

