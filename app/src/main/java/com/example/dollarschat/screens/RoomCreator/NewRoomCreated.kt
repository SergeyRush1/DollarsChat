package com.example.dollarschat.screens.RoomCreator

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.dollarschat.data.TABLE_NAME
import com.example.dollarschat.data.TABLE_NAME_ROOM
import com.example.dollarschat.screens.Chat.screens.NewRoomCreatedScreen

@Composable
fun  NewRoomCreated(navController: NavController){
    val context = LocalContext.current

    val roomPref:SharedPreferences = context.getSharedPreferences(TABLE_NAME_ROOM,Context.MODE_PRIVATE)
    val profilePref:SharedPreferences = context.getSharedPreferences(TABLE_NAME,Context.MODE_PRIVATE)
    NewRoomCreatedScreen(roomPref,profilePref,navController)
}