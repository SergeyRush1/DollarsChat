package com.example.dollarschat.ui.theme.uiComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dollarschat.ui.theme.DollarsTheme
import com.example.dollarschat.ui.theme.Shapes
import com.example.dollarschat.R
import com.example.dollarschat.data.Avatars
import com.example.dollarschat.data.Message

@Composable
fun MessageItem(bitmap:ImageBitmap,message:Message){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp,
                start = DollarsTheme.shapes.padding,
                bottom =  DollarsTheme.shapes.padding,
                end =  DollarsTheme.shapes.padding)
    ) {
        Column() {
            Image(
                painter = BitmapPainter(bitmap),
                contentDescription = "User Pick", modifier = Modifier.size(60.dp)
            )
            Text(text = message.user?.login?: "Default",
                modifier = Modifier.width(60.dp),
                textAlign = TextAlign.Center,
                maxLines = 2, style = DollarsTheme.typography.caption)
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier) {

                Box(modifier = Modifier.padding(start = 14.dp, top = 2.dp, end = 2.dp, bottom = 2.dp)) {


                    Box(
                        modifier = Modifier
                            .background(
                                color = Color.White,
                                shape = Shapes.medium
                            )
                            .padding(2.dp)
                    ) {
                        Box(
                            modifier = Modifier.background(
                                color = Color.Black,
                                shape = Shapes.medium
                            )
                        ) {
                            Text(
                                text = message.message.toString(),
                                color = when(message.user?.avatar?.color){
                                    "black" ->{ DollarsTheme.color.tintColor}
                                    else->{DollarsTheme.color.textColor}},
                                modifier = Modifier
                                    .background(
                                        color = when (message.user?.avatar?.color) {
                                            "blue" -> { DollarsTheme.color.blueUser }
                                            "black" -> { DollarsTheme.color.blackUser }
                                            "green" -> { DollarsTheme.color.greenUser }
                                            "yellow" -> { DollarsTheme.color.yellowUser }
                                            "red" -> { DollarsTheme.color.redUser }
                                            else -> { DollarsTheme.color.blueUser }
                                        },
                                        shape = Shapes.small
                                    )
                                    .padding(12.dp), style = DollarsTheme.typography.body
                            )
                        }

                    }
                }
                Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(top = 14.dp)) {
                    Image(
                        painter = when(message.user?.avatar?.color){
                            "blue"->{ painterResource(id = R.drawable.trew_blue)}
                            "black" ->{ painterResource(id = R.drawable.untitled)}
                            "green" ->{ painterResource(id = R.drawable.trew_green)}
                            "yellow" ->  painterResource(id = R.drawable.trew_yellow)
                            "red" -> {painterResource(id = R.drawable.trew_red)}
                            else ->{ painterResource(id = R.drawable.trew_blue) } },
                        contentDescription = "trew",Modifier.size(20.dp)
                    )
                }
            }
        }
        }

    }

@Preview
@Composable
fun mesegePrew(){
    DollarsTheme() {
        //MessageItem()

    }
}