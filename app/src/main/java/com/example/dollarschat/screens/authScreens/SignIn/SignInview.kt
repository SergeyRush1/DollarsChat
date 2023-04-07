package com.example.dollarschat.screens.authScreens.SignIn

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.example.dollarschat.screens.authScreens.SignIn.models.SignInEvent
import com.example.dollarschat.screens.authScreens.SignIn.models.SignInState
import com.example.dollarschat.screens.authScreens.SignIn.screens.SignInLoad

import com.example.dollarschat.screens.authScreens.SignIn.screens.signInScreen

@Composable
fun SignInview (signInViewModel: SignInViewModel, navController: NavController,) {
    val viewState = signInViewModel.signInViewState.observeAsState()

    when(val state = viewState.value){
       is  SignInState.LoadingScreen -> SignInLoad()
        is SignInState.SignIn -> signInScreen(
            state = state,
            regButton = { navController.navigate("reg") },
            onMailChanged = {signInViewModel.obtainEvent(SignInEvent.emailChange(it))},
            onPasswordChanged = {signInViewModel.obtainEvent(SignInEvent.passChange(it)) },
            passVisibleChange = {signInViewModel.obtainEvent(SignInEvent.changePassVisible(it))},
            navController = navController,
            loadButtonClick = {signInViewModel.obtainEvent(SignInEvent.loadButtonClick(it))}
        )

        else->throw NotImplementedError("Error State")
    }
    LaunchedEffect(key1 = viewState, block = {
        signInViewModel.obtainEvent(event = SignInEvent.loading(navController))
    })
    

}
