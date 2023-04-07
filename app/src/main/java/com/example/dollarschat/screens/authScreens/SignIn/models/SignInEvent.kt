package com.example.dollarschat.screens.authScreens.SignIn.models

import androidx.navigation.NavController

sealed class SignInEvent {
    data class loading(val navController: NavController):SignInEvent()
    data class emailChange(val newValue:String):SignInEvent()
    data class passChange(val newValue:String):SignInEvent()
    data class passwordCheck(val passCheck: Boolean):SignInEvent()
    data class emailCheck(val mailCheck: Boolean):SignInEvent()
    data class changePassVisible (val passVisible:Boolean ):SignInEvent()
    data class loadButtonClick(val navController: NavController):SignInEvent()
}