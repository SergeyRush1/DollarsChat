package com.example.dollarschat.screens.UserProfileScreen.models

import com.example.dollarschat.data.Avatars
import com.example.dollarschat.data.localBase.AvatarDatabase.AvatarEntity

sealed class ProfileState {
  object Loading:ProfileState()
    object ProfileInfo:ProfileState()
  data class ChangeUserProfile(val avatars:List<AvatarEntity>,
                               val packs:ArrayList<Pair<String,Boolean>>,
                               val newUserLogin:String = "",
                               val newUserLoginCheck:Boolean = false):ProfileState()
}