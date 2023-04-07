package com.example.dollarschat.ui.theme.uiComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.dollarschat.R
import com.example.dollarschat.ui.theme.DollarsTheme

@Composable
fun JoinInRoomMessege(userName:String){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(text = "------$userName"+" "+ stringResource(id = R.string.join_in_room)+"---------",
            color = DollarsTheme.color.textColor,
            style = DollarsTheme.typography.body)
    }

}

@Composable
fun ExitRoomMessage(userName: String){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            text = "------$userName"+" " + stringResource(id = R.string.user_exit_message) + "---------",
            color = DollarsTheme.color.textColor,
            style = DollarsTheme.typography.body
        )
    }
}
@Composable
fun RemoveUser(userName: String){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            text = "------$userName"+" " + stringResource(id = R.string.remove_user_message) + "---------",
            color = DollarsTheme.color.textColor,
            style = DollarsTheme.typography.body
        )
    }
}