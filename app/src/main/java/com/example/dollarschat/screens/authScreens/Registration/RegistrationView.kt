package com.example.dollarschat.screens.authScreens.Registration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.dollarschat.screens.authScreens.Registration.models.RegistrationEvent
import com.example.dollarschat.screens.authScreens.Registration.models.RegistrationState
import com.example.dollarschat.screens.authScreens.Registration.screens.RegistrationScreen
import com.example.dollarschat.screens.authScreens.SignIn.models.SignInEvent

@Composable
fun RegistrationView (registrationViewModel: RegistrationViewModel, navController: NavController) {
    val viewState = registrationViewModel.regViewState.observeAsState()

    when(val state = viewState.value){
        is RegistrationState.Registration -> RegistrationScreen(state = state,
            onMailChanged = {registrationViewModel.obtainEvent(RegistrationEvent.mailChange(it))},
            passVisibleChange = {registrationViewModel.obtainEvent(RegistrationEvent.firstPassVisibleChange(it))},
            onPasswordChanged = {registrationViewModel.obtainEvent(RegistrationEvent.firstPassChange(it))},
            onDoublePassChange = {registrationViewModel.obtainEvent(RegistrationEvent.secondPassChange(it))},
            doublePassVisibleChange = {registrationViewModel.obtainEvent(RegistrationEvent.secondPassVisibleChange(it))},
            regButtonClick ={registrationViewModel.obtainEvent(RegistrationEvent.regButtonClick(it))},
            navController = navController)
        else->throw NotImplementedError("Error State")
    }
    LaunchedEffect(key1 = viewState, block = {
        registrationViewModel.obtainEvent(event = RegistrationEvent.enter)
    })
}