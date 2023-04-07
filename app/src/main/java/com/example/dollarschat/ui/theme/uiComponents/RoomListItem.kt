package com.example.dollarschat.ui.theme.uiComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dollarschat.data.ChatRoomBase
import com.example.dollarschat.data.UserProfile
import com.example.dollarschat.ui.theme.DollarsTheme
import com.example.dollarschat.R

@Composable
fun RoomListItem(room:ChatRoomBase){

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = DollarsTheme.shapes.padding, bottom = DollarsTheme.shapes.padding).background(DollarsTheme.color.backgroundItem) ){
        Column(modifier = Modifier.padding(DollarsTheme.shapes.padding)) {
            Text(text =room.roomName.toString(), fontSize = 26.sp,
                color = DollarsTheme.color.textColor)
            Text(text = room.admin!!.login.toString(), color = DollarsTheme.color.textColor)
        }


            Column(modifier = Modifier.fillMaxWidth(),horizontalAlignment = Alignment.End) {
                Text(text = "${room.party?.size?: 0}/ ${room.size.toString()}",
                    color = DollarsTheme.color.textColor,
                    style = DollarsTheme.typography.system )
                if (!room.accesRoom!!) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_lock_24),
                        contentDescription = "lock",
                        tint = DollarsTheme.color.menuIconColor
                    )
                }
            }
        Divider(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.Bottom),
            thickness = 0.5.dp,
            color = DollarsTheme.color.textColor.copy(
                alpha = 0.3f
            )
        )
    }
}
@Preview(showBackground = true)
@Composable
fun RoomPrew(){
    DollarsTheme() {
        val user = UserProfile(login = ":Penis", messageQuantity = 4,roomsCreateQuantity = 5)

        val room = ChatRoomBase(roomName = "Vaflilniya",accesRoom = false,
            password = "DevilMayCry4dmc",
            party = ArrayList<UserProfile>(),
            admin = UserProfile(login = ":Penis", messageQuantity = 4,roomsCreateQuantity = 5),size = 5
        )
    RoomListItem(room = room)
    }

}