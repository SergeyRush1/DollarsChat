package com.example.dollarschat.screens.UserProfileScreen.models

import com.example.dollarschat.data.Avatars
import com.example.dollarschat.data.UserProfile
import com.example.dollarschat.ui.theme.DollarsStyle

sealed class ProfileEvent (){
    object Loading:ProfileEvent()
    object ChangeClick:ProfileEvent()
    object ProfileInfo:ProfileEvent()
    data class ChangeUserProfile(val newAnatar:Avatars,
                                 val newLogin:String?=null,
                                 val changelocalUser:(UserProfile)->Unit,
                                 val changeMainColor:(DollarsStyle)->Unit):ProfileEvent()
    data class ChangeEnteredLogin(val newValue:String):ProfileEvent()
}