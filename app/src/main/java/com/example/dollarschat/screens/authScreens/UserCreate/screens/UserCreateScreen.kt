package com.example.dollarschat.screens.authScreens.UserCreate.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dollarschat.data.Avatars
import com.example.dollarschat.data.FirebaseHelper
import com.example.dollarschat.data.localBase.AvatarDatabase.AvatarEntity
import com.example.dollarschat.screens.authScreens.SignIn.models.SignInEvent
import com.example.dollarschat.screens.authScreens.UserCreate.UserCreateViewModel
import com.example.dollarschat.screens.authScreens.UserCreate.models.UserCreateState
import com.example.dollarschat.ui.theme.DollarsTheme
import com.example.dollarschat.ui.theme.uiComponents.LoadingButton

@Composable
fun UserCreateScreen(state:UserCreateState.CreateUser,
                     userLoginChange:(String)->Unit,
                     loadButtonClick:()->Unit,
                     userAvatatClick:(Int)->Unit ) {


    DollarsTheme() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = DollarsTheme.color.backround),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box() {
                Image(
                    painter = BitmapPainter(state.bitmapRes[state.mainPage]),
                    contentDescription = "UserAvater",
                    modifier = Modifier
                        .padding(bottom = 50.dp)
                        .height(400.dp)
                )
            }

            TextField(
                value = state.userlogin,
                onValueChange = userLoginChange,
                modifier = Modifier.background(color = DollarsTheme.color.tintColor),
                textStyle = TextStyle(fontSize = 20.sp),
                shape =DollarsTheme.shapes.cornerShape,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = DollarsTheme.color.textColor,
                    backgroundColor = DollarsTheme.color.tintColor,
                    disabledLabelColor = DollarsTheme.color.backround,
                    unfocusedIndicatorColor = DollarsTheme.color.tintColor,
                    focusedIndicatorColor = DollarsTheme.color.tintColor,
                    cursorColor = DollarsTheme.color.textColor
                )
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height(250.dp),
                state = rememberLazyGridState(),
                contentPadding = PaddingValues(10.dp)
            ) {
                items(state.bitmapRes.size) { index ->
                    Card(
                        backgroundColor = Color.Blue,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxSize()
                    ) {
                        Image(painter = BitmapPainter(state.bitmapRes[index]),
                            contentDescription = state.avatarList[index].image_name,
                            modifier = Modifier
                                .background(color = DollarsTheme.color.backround)
                                .clickable(onClick = {userAvatatClick(index)}) )
                        ///
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            LoadingButton(onClick = loadButtonClick,
                buttonColors = ButtonDefaults.buttonColors(DollarsTheme.color.buttonColor),
                shape = DollarsTheme.shapes.cornerShape,
                loading = state.loadState ) {
                Text(text = "OK")
            }
            Spacer(modifier = Modifier.height(50.dp))


        }



    }
}