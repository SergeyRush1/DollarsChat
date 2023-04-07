package com.example.dollarschat.screens.Chat.screens

import android.app.AlertDialog
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.dollarschat.R
import com.example.dollarschat.data.ChatRoomBase
import com.example.dollarschat.screens.Chat.models.ChatState
import com.example.dollarschat.ui.theme.uiComponents.AlertPassDialog
import com.example.dollarschat.ui.theme.uiComponents.DollarsThemeAlertDialog
import com.example.dollarschat.ui.theme.uiComponents.RoomListItem

@Composable
fun RoomListScreen(state:ChatState.RoomList,
                   roomClick:(ChatRoomBase)->Unit,
                   canelButonAlert:()->Unit,
                   okAlertPassDialog:(String)->Unit,
                   okAlertDialog:()->Unit){
    LazyColumn(
        modifier = Modifier
            .padding(top = 10.dp)
    ) {
        items(state.roomList.size) {
            Row(modifier = Modifier.clickable {
                roomClick(state.roomList[it])
            }) {
                RoomListItem(room = state.roomList[it])
            }
            if (state.alertState) {
                AlertPassDialog(canelButtonAlert = canelButonAlert,
                    okAlertDialogClick = okAlertPassDialog,
                    passwordState = state.passState)
            }
            if (state.fullRoomAlert){
                DollarsThemeAlertDialog(text = stringResource(id = R.string.full_room_message), onClick = okAlertDialog)
            }
        }
    }
}