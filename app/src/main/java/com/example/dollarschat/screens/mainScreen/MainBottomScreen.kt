package com.example.dollarschat.screens.mainScreen

import androidx.annotation.StringRes
import com.example.dollarschat.R


sealed class MainBottomScreen(val route: String,val resourceId: Int,@StringRes val resName:Int) {
    object Settings:MainBottomScreen("Settings",R.drawable.baseline_settings_24,R.string.Setting_screen)
    object Chat:MainBottomScreen("Chat",R.drawable.baseline_message_24,R.string.Chat_screen)
    object Profile:MainBottomScreen("Profile",R.drawable.baseline_account_box_24,R.string.Profile_screen)
    object Event:MainBottomScreen("Event",R.drawable.baseline_sports_motorsports_24,R.string.Event_screen)


}