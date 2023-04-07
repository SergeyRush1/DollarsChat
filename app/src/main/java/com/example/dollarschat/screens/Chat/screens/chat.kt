package com.example.dollarschat

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import com.example.dollarschat.data.FirebaseHelper
import com.example.dollarschat.screens.Chat.models.ChatState

@Composable
fun chat(state:ChatState.Chat){

  Column() {
      Spacer(modifier = Modifier.size(56.dp))
      Text(text = state.userProfile.login!!)
      Text(text = state.userProfile.accessibleAvatars!!)
      Text(text = state.userProfile.id!!)
      Image(painter = BitmapPainter(state.bitmap), contentDescription ="dsdsd" )


  }

}