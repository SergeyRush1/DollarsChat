package com.example.dollarschat.screens.authScreens.UserCreate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.dollarschat.data.UserProfile
import com.example.dollarschat.domain.SettingsBundle
import com.example.dollarschat.screens.authScreens.UserCreate.models.UserCreateEvent
import com.example.dollarschat.screens.authScreens.UserCreate.models.UserCreateState
import com.example.dollarschat.screens.authScreens.UserCreate.screens.UserCreateLoading
import com.example.dollarschat.screens.authScreens.UserCreate.screens.UserCreateScreen
import com.example.dollarschat.ui.theme.DollarsCorners
import com.example.dollarschat.ui.theme.DollarsStyle

@Composable
fun UserCreateView(userCreateViewModel: UserCreateViewModel,
                   navController: NavController,
                   onSettingsChanged: (DollarsStyle) -> Unit, userCreate:(UserProfile)->Unit) {
    val viewState = userCreateViewModel.userCreateViewState.observeAsState()


    when(val state = viewState.value){
        is UserCreateState.Loading -> UserCreateLoading()
        is UserCreateState.CreateUser -> UserCreateScreen(state = state,
            userLoginChange = {userCreateViewModel.obtainEvent(UserCreateEvent.UserLoginChange(it))},
            //vent.LoadButtonClick(state.avatarList[0]
            loadButtonClick = {userCreateViewModel.obtainEvent(UserCreateEvent.LoadButtonClick(state.avatarList[state.mainPage],state.userlogin))},
            userAvatatClick = {userCreateViewModel.obtainEvent(UserCreateEvent.AvatarChangeClick(it))} )
        is UserCreateState.GoToChat -> {userCreateViewModel.obtainEvent(UserCreateEvent.GoToChat(navController,onSettingsChanged,userCreate))}
        is UserCreateState.stop->{}
        else->throw NotImplementedError("Error State")

    }
    LaunchedEffect(key1 = viewState, block = {
        userCreateViewModel.obtainEvent(event = UserCreateEvent.LoadingStartedPack)
    })
}