package com.example.dollarschat.screens.authScreens.UserCreate.models

import androidx.navigation.NavController
import com.example.dollarschat.data.Avatars
import com.example.dollarschat.data.UserProfile
import com.example.dollarschat.domain.SettingsBundle
import com.example.dollarschat.ui.theme.DollarsCorners
import com.example.dollarschat.ui.theme.DollarsStyle

sealed class UserCreateEvent {
    object Loading:UserCreateEvent()
    object stop:UserCreateEvent()
    object LoadingStartedPack:UserCreateEvent()
    data class MainPagePaint(val imageName:String):UserCreateEvent()
    data class LoadButtonClick(val image: Avatars,val userLogin: String):UserCreateEvent()
    data class UserLoginChange(val userLogin:String):UserCreateEvent()
    data class AvatarChangeClick(val imageIndex: Int):UserCreateEvent()
    data class GoToChat(val navController: NavController,
                        val onSettingsChanged: (DollarsStyle) -> Unit,
                        val userInMemory:(UserProfile)->Unit):UserCreateEvent()

}