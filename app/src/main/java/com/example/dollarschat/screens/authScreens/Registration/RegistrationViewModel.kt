package com.example.dollarschat.screens.authScreens.Registration

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dollarschat.data.FirebaseHelper
import com.example.dollarschat.handler.EventHandler
import com.example.dollarschat.screens.authScreens.Registration.models.RegistrationEvent
import com.example.dollarschat.screens.authScreens.Registration.models.RegistrationState
import com.example.dollarschat.screens.authScreens.SignIn.models.SignInState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(@ApplicationContext application: Context): ViewModel(),
    EventHandler<RegistrationEvent> {
    val _regViewState: MutableLiveData<RegistrationState> =
        MutableLiveData(RegistrationState.Registration())
    val regViewState: LiveData<RegistrationState> = _regViewState
    val context = application
    val firebaseHelper = FirebaseHelper(application)

    val TAG = "Registration"

    override fun obtainEvent(event: RegistrationEvent) {
        when (val currentState = _regViewState.value) {
            is RegistrationState.Registration -> reduce(event,currentState)
            else -> {}
        }
    }

    private fun reduce(event: RegistrationEvent, currentState: RegistrationState.Registration) {
        when (event) {
            is RegistrationEvent.mailChange-> userEmailCheck(event.newValue,currentState)
            is RegistrationEvent.firstPassChange -> firstPassCheck(event.newValue,currentState)
            is RegistrationEvent.secondPassChange -> secondPassCheck(event.newValue,currentState)
            is RegistrationEvent.firstPassVisibleChange -> _regViewState.postValue(currentState.copy(firstPassVisible = !event.currentValue))
            is RegistrationEvent.secondPassVisibleChange -> _regViewState.postValue(currentState.copy(secondPassVisible = !event.currentValue))
            is RegistrationEvent.regButtonClick -> registrationUser(event,currentState)
            else -> {}
        }
    }
    private fun firstPassCheck(pass:String,currentState: RegistrationState.Registration){
        val PASSWORD_PATTERN: Pattern =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
        val check =  pass.matches(PASSWORD_PATTERN.toRegex())
        _regViewState.postValue(currentState.copy(firstPass = pass, firstPassCheck = check))
    }
    private fun secondPassCheck(pass:String,currentState: RegistrationState.Registration){
        val PASSWORD_PATTERN: Pattern =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
        val check =  pass.matches(PASSWORD_PATTERN.toRegex())
        _regViewState.postValue(currentState.copy(secondPAss = pass, secondPassCheck = check))
    }

    private fun userEmailCheck(email:String,currentState: RegistrationState.Registration){
        val MAIL_PATTERN:Pattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+")
        val emailCheck=email.matches(MAIL_PATTERN.toRegex())
        _regViewState.postValue(currentState.copy(email = email, emailCheck = emailCheck))
    }
    private fun registrationUser(event: RegistrationEvent.regButtonClick,currentState: RegistrationState.Registration){
        _regViewState.postValue(currentState.copy(loadState = !currentState.loadState))
        if (currentState.secondPassCheck&&currentState.firstPassCheck&&(currentState.firstPass==currentState.secondPAss)){
            viewModelScope.launch(Dispatchers.Main) {
                if (firebaseHelper.registration(currentState.email,currentState.firstPass)){
                    Log.d(TAG,"успешная регистрация")
                    if (firebaseHelper.signInUser(email = currentState.email, pass = currentState.firstPass)) {
                        event.navController.navigate("userCreate")
                    }
                }else{
                    //добавить обработку ошибки регистрации
                    _regViewState.postValue(currentState.copy(loadState = !currentState.loadState))

                }
            }
        }else{
           val toast =  Toast.makeText(context," заполните поля правильно",Toast.LENGTH_LONG)
            toast.show()
            Log.d(TAG,"неправльно заполнены поля")
            _regViewState.postValue(currentState.copy(loadState = currentState.loadState))
        }
    }


















//    private fun userRegistration(event: AuthEvent.RegistrationCompliteClik) {
//        viewModelScope.launch(Dispatchers.IO) {
//            if(loder.userRegistration(event.userMail,event.userPassword)){
//                if (loder.signInUser(event.userMail,event.userPassword)){
//                    val userProfile = UserProfile(userName = "DEFAULT",userAvatar = "blackUser.png", accessibleAvatars = "startedPack")
//                    loder.createUserProfile(Firebase.auth.currentUser,"DEFAULT", avatarName = "DEFAULT")
//                    _regViewState.postValue(RegistrationViewState.CreareUser(loder.currentUser!!))
//                }else{
//                    //добавить уведомление об ошибке
//                    _regViewState.postValue(RegistrationViewState.Registration)}
//            }else{
//                //добавить уведомление об ошибке
//                _regViewState.postValue(RegistrationViewState.Registration)
//            }
//        }
//    }


}
