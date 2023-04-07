package com.example.dollarschat.screens.tabs

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.dollarschat.data.UserProfile
import com.example.dollarschat.screens.Chat.ChatView
import com.example.dollarschat.screens.Chat.ChatViewModel
import com.example.dollarschat.screens.RoomCreator.NewRoomCreated
import com.example.dollarschat.screens.mainScreen.MainBottomScreen

fun NavGraphBuilder.dailyFlow(
    navController: NavController,

) {
    navigation(startDestination = "chat", route = MainBottomScreen.Chat.route) {
        composable("chat") {
            val chatViewModel = hiltViewModel<ChatViewModel>()
           // chatViewModel.loadRooms()
            ChatView(navController = navController, chatViewModel = chatViewModel)
        }
        composable("createRoom"){
            NewRoomCreated(navController)
        }

    }
}