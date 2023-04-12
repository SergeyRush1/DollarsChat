package com.example.dollarschat.screens.Chat.screens

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.media.Rating
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.clipScrollableContainer
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.example.dollarschat.DollarsApp
import com.example.dollarschat.MainActivity
import com.example.dollarschat.R
import com.example.dollarschat.data.*
import com.example.dollarschat.screens.Chat.models.ChatState
import com.example.dollarschat.ui.theme.DollarsTheme
import com.example.dollarschat.ui.theme.uiComponents.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun chatScreen(state:ChatState.InRoom,
               painter:FirebaseHelper,
               messageSendClick:(String)->Unit,
               exitRoomClick:()->Unit,
               userId:String,
               okAlertDialogClick:()->Unit,
               cancelArertDialogClick:()->Unit,
               removeUserClick:(ArrayList<UserProfile>)->Unit,
               getUsersInRoom:()->Unit,
               closeUserListClick:()->Unit){

       val listState = rememberLazyListState()
       val coroutineScope = rememberCoroutineScope()
       val newMessage = remember{ mutableStateOf("") }
    
   // var isVisibility by remember { mutableStateOf(false) }
    if (state.alertExitDialog){
        DollarsAlertDialog(text = stringResource(id = R.string.exit_on_room_message),
            onOkClick =okAlertDialogClick, onCancelClick = cancelArertDialogClick )
    }
       Column() {
           LazyColumn(
               Modifier
                   .padding(bottom = 150.dp)
                   .imePadding(), state = listState,
               verticalArrangement = Arrangement.Bottom,
               reverseLayout = false) {

               items(state.message?.size!!) {
                   if (state.message[it].user == null) {
                       RemoveUser(userName = state.message[it].message!!)
                   } else{
                       when (state.message[it].message) {
                           "" -> {
                               JoinInRoomMessege(userName = state.message[it].user?.login.toString())
                           }
                           "exit" -> {
                               ExitRoomMessage(userName = state.message[it].user?.login.toString())
                           }
                           else -> {
                               when (state.message[it].user?.id) {
                                   userId -> {
                                       MyMessageItem(
                                           bitmap = painter.paintBitmap(state.message[it].user?.avatar?.image_name!!)!!,
                                           message = state.message[it]
                                       )
                                   }
                                   else -> {
                                       MessageItem(
                                           bitmap = painter.paintBitmap(state.message[it].user?.avatar?.image_name!!)!!,
                                           message = state.message[it]
                                       )
                                   }
                               }
                           }

                       }
               }




//                   if (state.message[it].message == "") {
//                       JoinInRoomMessege(userName = state.message[it].user?.login.toString())
//                   } else { if (state.message[it].message=="exit"){
//                       ExitRoomMessage(userName = state.message[it].user?.login.toString())
//                   }else {
//                       if (state.message[it].user!!.id != userId) {
//                           MessageItem(
//                               bitmap = painter.paintBitmap(state.message[it].user?.avatar?.image_name!!)!!,
//                               message = state.message[it]
//                           )
//                       } else {
//
//                           MyMessageItem(
//                               bitmap = painter.paintBitmap(state.message[it].user?.avatar?.image_name!!)!!,
//                               message = state.message[it]
//                           )
//
//                       }
//                   }
//
//                   }
               }

           }
       }
     //val config = LocalConfiguration.current
//    if (config.orientation == Configuration.ORIENTATION_LANDSCAPE){

        Column(
            modifier = Modifier.fillMaxSize(),
            Arrangement.Bottom
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = DollarsTheme.color.tintColor),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.clickable {
                    coroutineScope.launch(Dispatchers.Main) {
                        listState.scrollToItem(state.message!!.lastIndex)
                    }
                }) {
                    DollarsOutlineTextField(label = "", value = newMessage.value, onValueChange = {
                        newMessage.value = it
                    } )
                }
            }
            Box() {

                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(color = DollarsTheme.color.tintColor)
                        .padding(10.dp), horizontalArrangement = Arrangement.Center
                ) {
                    LoadingButton(
                        onClick = {
                            messageSendClick(newMessage.value)
                            newMessage.value = ""
                        },
                        buttonColors = ButtonDefaults.buttonColors(DollarsTheme.color.buttonColor),
                        shape = DollarsTheme.shapes.cornerShape, loading = state.loadState
                    ) {
                        Text(text = "POST")
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp), horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = exitRoomClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_exit_to_app_24),
                            contentDescription = "exit", tint = DollarsTheme.color.menuIconColor
                        )

                    }
                    IconButton(onClick = getUsersInRoom
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_supervisor_account_24),
                            contentDescription = "people_in_room", tint = DollarsTheme.color.menuIconColor
                        )

                    }
                }
            }
      //  }
    }
    if (state.userListAlert){
        state.room.party?.let {
            ListOfUsers(party = it, cancel = closeUserListClick, removeUserClick = removeUserClick,state.userAdmin,userId )
        }

    }
    BackHandler(onBack = exitRoomClick)




LaunchedEffect(key1 = state.message, block = {
    coroutineScope.launch(Dispatchers.Main) {
        state.message?.let {
            if (state.message.lastIndex > 0) {
                listState.scrollToItem(state.message.lastIndex)
            }
        }

    }})
}

@Preview
@Composable
fun chatPrew(){
    val messege = ArrayList<Message>()
    messege.add(Message(user = UserProfile(login = "Default",
        avatar = Avatars(image_name = "blueUser.png", iventPackageName = "ivent", storageReference = "ref",accessAvatar = true),
        messageQuantity = 1, roomsCreateQuantity = 1, id = "id",accessibleAvatars = "23")))




}