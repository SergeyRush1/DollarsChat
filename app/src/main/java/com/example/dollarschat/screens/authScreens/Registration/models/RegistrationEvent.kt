package com.example.dollarschat.screens.authScreens.Registration.models

import androidx.navigation.NavController

sealed class RegistrationEvent {
    object enter:RegistrationEvent()
    data class mailChange(val newValue:String):RegistrationEvent()
    data class firstPassChange(val newValue:String):RegistrationEvent()
    data class secondPassChange(val newValue: String):RegistrationEvent()
    data class firstPassVisibleChange(val currentValue:Boolean):RegistrationEvent()
    data class secondPassVisibleChange(val currentValue:Boolean):RegistrationEvent()
    data class regButtonClick(val navController: NavController):RegistrationEvent()

}