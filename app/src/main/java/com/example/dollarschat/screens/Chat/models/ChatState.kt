package com.example.dollarschat.screens.Chat.models

import androidx.compose.ui.graphics.ImageBitmap
import com.example.dollarschat.data.ChatRoomBase
import com.example.dollarschat.data.Message
import com.example.dollarschat.data.UserProfile

sealed class ChatState {
    object Loading:ChatState()
    data class Chat(val userProfile: UserProfile,val bitmap:ImageBitmap):ChatState()
    data class RoomList(val roomList:ArrayList<ChatRoomBase>,
                        val alertState:Boolean = false,
                        val passState:Boolean = true,
                        val fullRoomAlert:Boolean = false):ChatState()
    data class InRoom(val message:ArrayList<Message>?,
                      val newMessage:String ="",
                      val loadState:Boolean =false,
                      val alertExitDialog:Boolean = false,
                      val room:ChatRoomBase,
                      val exceptionFronRoom:Boolean = false,
                      val userListAlert:Boolean=false):ChatState()
    object LoadingMessageInRoom:ChatState()

    object RoomLoadimg:ChatState()
    object RoomDellited:ChatState()
    data class GoToRoom(val room:ChatRoomBase):ChatState()
    object GoToCreatedRoom:ChatState()
    object ExpelledUser:ChatState()

}