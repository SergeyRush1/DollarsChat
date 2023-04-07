package com.example.dollarschat.ui.theme.uiComponents

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.dollarschat.R
import com.example.dollarschat.data.Avatars
import com.example.dollarschat.data.FirebaseHelper
import com.example.dollarschat.data.UserProfile
import com.example.dollarschat.ui.theme.DollarsTheme
import java.nio.file.attribute.FileAttribute
import kotlin.io.path.createTempDirectory

@Composable
fun DrawerView(user:UserProfile) {
    val list = ArrayList<Pair<String,String>>()


    list.add(Pair(user.login.toString(), stringResource(id = R.string.user_name)))
    list.add(Pair(user.roomsCreateQuantity.toString(), stringResource(id = R.string.room_created_value)))
    list.add(Pair(user.messageQuantity.toString(), stringResource(id = R.string.messahes_value)))
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = DollarsTheme.color.backround)) {
            Spacer(modifier = Modifier.size(70.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter =  BitmapPainter(Bitmap.createScaledBitmap(FirebaseHelper(LocalContext.current).paintBitmap(user.avatar?.image_name!!)!!.asAndroidBitmap(),1000,1000,false).asImageBitmap()),
                    contentDescription = "UserAvatar"
                )
            }
            Spacer(modifier = Modifier.size(70.dp))
            list.forEach { pair ->
                Column(Modifier.background(color = DollarsTheme.color.backgroundItem)) {
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
        }

}
@Preview(showBackground = true)
@Composable
fun DrawPrew(){
    DollarsTheme() {
        val user = UserProfile(login = "Default",
            avatar = Avatars(image_name = "blueUser.png", iventPackageName = "ivent", storageReference = "ref",accessAvatar = true),
            messageQuantity = 1, roomsCreateQuantity = 1, id = "id",accessibleAvatars = "23")
        DrawerView(user)
    }

}