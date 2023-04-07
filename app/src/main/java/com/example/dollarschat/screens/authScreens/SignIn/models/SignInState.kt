package com.example.dollarschat.screens.authScreens.SignIn.models

import androidx.compose.ui.graphics.Color
import com.example.dollarschat.ui.theme.DollarsTheme

sealed class SignInState {
    object LoadingScreen : SignInState()
    object Registration : SignInState()
    data class SignIn(
        val mail: String = "",
        val password: String = "",
        val emailCheck: Boolean = false,
        val passCheck: Boolean = false,
        val loadingButtonState:Boolean = false,
        val passwordVisible:Boolean = false,
        val loadButtonState:Boolean = false
    ) : SignInState()
}
