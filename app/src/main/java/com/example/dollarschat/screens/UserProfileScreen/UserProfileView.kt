package com.example.dollarschat.screens.UserProfileScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.dollarschat.R
import com.example.dollarschat.data.UserProfile
import com.example.dollarschat.screens.Chat.models.ChatEvents
import com.example.dollarschat.screens.Chat.models.ChatState
import com.example.dollarschat.screens.Chat.screens.ChatLoading
import com.example.dollarschat.screens.Chat.screens.RoomListScreen
import com.example.dollarschat.screens.Chat.screens.chatScreen
import com.example.dollarschat.screens.UserProfileScreen
import com.example.dollarschat.screens.UserProfileScreen.models.ProfileEvent
import com.example.dollarschat.screens.UserProfileScreen.models.ProfileState
import com.example.dollarschat.screens.UserProfileScreen.screens.ChangeUserProfile
import com.example.dollarschat.screens.UserProfileScreen.screens.ProfileLoading
import com.example.dollarschat.screens.mainScreen.MainBottomScreen
import com.example.dollarschat.ui.theme.DollarsStyle
import com.example.dollarschat.ui.theme.uiComponents.DollarsThemeAlertDialog

@Composable
fun UserProfileView(profileViewModel: UserProfileViewModel,
                    localUser:UserProfile,
                    changeLocalUser:(UserProfile)->Unit,
                    mainColorChange:(DollarsStyle)->Unit
) {
        val viewState = profileViewModel.userCreateViewState.observeAsState()
        when (val state = viewState.value) {
            is ProfileState.Loading -> ProfileLoading()
            is ProfileState.ProfileInfo -> UserProfileScreen(user = localUser,
                changeUserProfileClick = {
                    profileViewModel.obtainEvent(ProfileEvent.Loading)


                })
            is ProfileState.ChangeUserProfile -> {
                ChangeUserProfile(
                    backButtonClick = { profileViewModel.obtainEvent(ProfileEvent.ProfileInfo) },
                    localUser = localUser,
                    profileChangeClick = {profileViewModel.obtainEvent(ProfileEvent.ChangeUserProfile(it.first,it.second,changeLocalUser,mainColorChange))},
                    state = state,
                    newLoginChange = {profileViewModel.obtainEvent(ProfileEvent.ChangeEnteredLogin(it))})

            }


            else -> throw NotImplementedError("Error State")

        }

    LaunchedEffect(key1 = viewState, block = {
        profileViewModel.obtainEvent(event = ProfileEvent.ProfileInfo)
    })
}