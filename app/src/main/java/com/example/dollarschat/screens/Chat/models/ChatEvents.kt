package com.example.dollarschat.screens.Chat.models

import com.example.dollarschat.data.ChatRoomBase
import com.example.dollarschat.data.Message
import com.example.dollarschat.data.UserProfile

sealed class ChatEvents {
    object Loading:ChatEvents()
    data class RoomSelected(val room:ChatRoomBase):ChatEvents()
    data class NewMessageChange(val newValue:String =""):ChatEvents()
    data class SendMessage(val message:String):ChatEvents()
    data class PassEnter(val pass:String):ChatEvents()
    object canelButtonClick:ChatEvents()
    object ExitRoom:ChatEvents()
    object okAlertDialogClick:ChatEvents()
    object ChangeAlertDialogState:ChatEvents()
    object RoomDelited:ChatEvents()
    data class GoToRoom(val room:ChatRoomBase):ChatEvents()
    data class RemoveUsers(val removeUser:ArrayList<UserProfile>):ChatEvents()
    object getUsersInRoom:ChatEvents()
    object ClosedUserList:ChatEvents()
    object ClickAfterRemove:ChatEvents()
}