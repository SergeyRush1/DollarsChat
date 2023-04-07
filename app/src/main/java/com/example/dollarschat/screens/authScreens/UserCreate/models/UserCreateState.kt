package com.example.dollarschat.screens.authScreens.UserCreate.models

import androidx.compose.ui.graphics.ImageBitmap
import com.example.dollarschat.data.Avatars
import java.util.Objects

sealed class UserCreateState(
) {
    object Loading:UserCreateState()
    data class CreateUser(val avatarList:ArrayList<Avatars>,
                          val bitmapRes:ArrayList<ImageBitmap>,
                          val mainPage:Int = 0,
                          val loadState:Boolean = false,
                          val userlogin:String ="" ):UserCreateState()
    object GoToChat:UserCreateState()
    object stop:UserCreateState()
}