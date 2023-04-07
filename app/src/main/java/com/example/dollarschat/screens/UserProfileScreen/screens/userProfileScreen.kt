package com.example.dollarschat.screens

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dollarschat.ui.theme.uiComponents.MyMessageItem
import com.example.dollarschat.R
import com.example.dollarschat.data.Avatars
import com.example.dollarschat.data.FirebaseHelper
import com.example.dollarschat.data.UserProfile
import com.example.dollarschat.ui.theme.DollarsTheme
import com.example.dollarschat.ui.theme.Shapes
import com.example.dollarschat.ui.theme.uiComponents.DollarsButton
import com.example.dollarschat.ui.theme.uiComponents.DrawerView

@Composable
fun UserProfileScreen(user:UserProfile,changeUserProfileClick:()->Unit){
    var isVisibility by remember { mutableStateOf(false) }
val list = ArrayList<Pair<String,String>>()
list.add(Pair(user.login.toString(), stringResource(id = R.string.user_name)))
list.add(Pair(user.roomsCreateQuantity.toString(), stringResource(id = R.string.room_created_value)))
list.add(Pair(user.messageQuantity.toString(), stringResource(id = R.string.messahes_value)))

Column(modifier = Modifier
.fillMaxSize()) {
    Spacer(modifier = Modifier.size(70.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(visible = isVisibility, modifier = Modifier, enter = fadeIn() + expandIn() ) {
        Image(
            painter = BitmapPainter(
                Bitmap.createScaledBitmap(
                    FirebaseHelper(LocalContext.current).paintBitmap(
                        user.avatar?.image_name!!
                    )!!.asAndroidBitmap(), 1000, 1000, false
                ).asImageBitmap()
            ),
            contentDescription = "UserAvatar"
        )
    }
    }
    Spacer(modifier = Modifier.size(70.dp))
    list.forEach { pair ->
        Column(Modifier.background(color = DollarsTheme.color.backgroundItem, shape = Shapes.medium)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    Modifier
                        .padding(DollarsTheme.shapes.padding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = DollarsTheme.shapes.padding),
                        text = pair.second,
                        style = DollarsTheme.typography.body,
                        //добавить primaryText color
                        color = DollarsTheme.color.textColor
                    )

                    Text(
                        text = pair.first,
                        style = DollarsTheme.typography.body,
                        color = DollarsTheme.color.textColor
                    )
                }


            }

        }
    }
    Row(modifier = Modifier.fillMaxWidth().padding(DollarsTheme.shapes.padding),
        horizontalArrangement = Arrangement.Center) {
        DollarsButton(onClick = changeUserProfileClick) {
            Text(text = stringResource(id = R.string.change_user_profile),
                color = DollarsTheme.color.textInButtonColor, style = DollarsTheme.typography.system)
        }
    }

}

    LaunchedEffect(key1 = isVisibility, block = {
        isVisibility = true
    })

}
@Preview(showBackground = true)
@Composable
fun DrawPrew(){
    DollarsTheme() {
        val user = com.example.dollarschat.data.UserProfile(login = "Default",
            avatar = Avatars(image_name = "blueUser.png", iventPackageName = "ivent", storageReference = "ref",accessAvatar = true),
            messageQuantity = 1, roomsCreateQuantity = 1, id = "id",accessibleAvatars = "23")
        DrawerView(user)
    }

}


