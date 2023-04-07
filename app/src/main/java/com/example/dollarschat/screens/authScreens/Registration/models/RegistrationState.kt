package com.example.dollarschat.screens.authScreens.Registration.models

sealed class RegistrationState {
    data class Registration(val email:String = "",
                            val firstPass:String="",
                            val secondPAss:String = "",
                            val emailCheck:Boolean = false,
                            val firstPassCheck:Boolean=false,
                            val secondPassCheck:Boolean = false,
                            val firstPassVisible:Boolean = false,
                            val secondPassVisible:Boolean = false,
                            val loadState:Boolean=false):RegistrationState()
}