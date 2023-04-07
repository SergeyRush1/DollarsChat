package com.example.dollarschat.screens.Chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.dollarschat.R
import com.example.dollarschat.chat
import com.example.dollarschat.data.UserProfile
import com.example.dollarschat.screens.Chat.models.ChatEvents
import com.example.dollarschat.screens.Chat.models.ChatState
import com.example.dollarschat.screens.Chat.screens.ChatLoading
import com.example.dollarschat.screens.Chat.screens.RoomListScreen
import com.example.dollarschat.screens.Chat.screens.chatScreen
import com.example.dollarschat.ui.theme.uiComponents.DollarsThemeAlertDialog


@Composable
fun ChatView(chatViewModel: ChatViewModel,
             navController: NavController) {
    val viewState = chatViewModel.chatViewState.observeAsState()
    val loadState  = false


    when(val state = viewState.value){
        is ChatState.Loading-> {ChatLoading()
        chatViewModel.obtainEvent(ChatEvents.Loading)}
        is ChatState.RoomList -> RoomListScreen(state = state,
            roomClick = { chatViewModel.obtainEvent(ChatEvents.RoomSelected(it)) },
            canelButonAlert ={chatViewModel.obtainEvent(ChatEvents.canelButtonClick)},
            okAlertPassDialog = {chatViewModel.obtainEvent(ChatEvents.PassEnter(it))},
            okAlertDialog = {chatViewModel.obtainEvent(ChatEvents.okAlertDialogClick)} )
        is ChatState.InRoom -> chatScreen(state = state,
            painter = chatViewModel.firebaseHelper,
            messageSendClick = {chatViewModel.obtainEvent(ChatEvents.SendMessage(it))},
            exitRoomClick = {chatViewModel.obtainEvent(ChatEvents.ChangeAlertDialogState)},
            userId = chatViewModel.userProfile.id!!,
            okAlertDialogClick = {chatViewModel.obtainEvent(ChatEvents.ExitRoom)},
            cancelArertDialogClick = {chatViewModel.obtainEvent(ChatEvents.ChangeAlertDialogState)},
            removeUserClick ={chatViewModel.obtainEvent(ChatEvents.RemoveUsers(it))},
            getUsersInRoom = {chatViewModel.obtainEvent(ChatEvents.getUsersInRoom)},
            closeUserListClick ={chatViewModel.obtainEvent(ChatEvents.ClosedUserList)} )
        is ChatState.LoadingMessageInRoom -> ChatLoading()
        is ChatState.RoomDellited -> {
            DollarsThemeAlertDialog(text = stringResource(id = R.string.admin_delit_room),
            onClick = {chatViewModel.obtainEvent(ChatEvents.RoomDelited)}) }
        is ChatState.GoToRoom ->{chatViewModel.obtainEvent(ChatEvents.GoToRoom(state.room))}
        is ChatState.GoToCreatedRoom ->{chatViewModel.obtainEvent(ChatEvents.getUsersInRoom)}
        
        is ChatState.ExpelledUser ->{
            DollarsThemeAlertDialog(text = "Вас исклчили из комнаты",
                onClick = {chatViewModel._chatViewState.postValue(ChatState.Loading)}) }
        else->throw NotImplementedError("Error State")

    }
    LaunchedEffect(key1 = loadState, block = {
        chatViewModel.obtainEvent(event = ChatEvents.Loading)
    })
}