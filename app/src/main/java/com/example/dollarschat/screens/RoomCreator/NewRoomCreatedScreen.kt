package com.example.dollarschat.screens.Chat.screens

import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dollarschat.ui.theme.DollarsTheme
import com.example.dollarschat.R
import com.example.dollarschat.data.*
import com.example.dollarschat.ui.theme.uiComponents.DollarsOutlineTextField
import com.example.dollarschat.ui.theme.uiComponents.DollarsSlider

@Composable
fun NewRoomCreatedScreen(roomPref:SharedPreferences,profilePref:SharedPreferences,navController: NavController){
    var isChacked = remember { mutableStateOf(false) }
    val passwordVisible = remember { mutableStateOf(false) }
    var roomName = remember { mutableStateOf("") }
    var peopleInRoom = remember { mutableStateOf(1f) }
    var AccesInRoom = remember { mutableStateOf(false) }
    var passwordRoom = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = stringResource(id = R.string.room_name_info),
            style = DollarsTheme.typography.system,
            color = DollarsTheme.color.textColor)
        DollarsOutlineTextField(label = stringResource(id = R.string.room_name),
            value = roomName.value , 
            onValueChange ={roomName.value = it} )
//        TextField(
//            value = RoomName.value,
//            onValueChange = { RoomName.value = it },
//            colors = TextFieldDefaults.textFieldColors(
//                focusedIndicatorColor = DollarsTheme.color.focusColor
//            )
//        )
        Text(text = stringResource(id = R.string.room_size_create_info) +"${peopleInRoom.value.toInt()}",
            color = DollarsTheme.color.textColor,
            style = DollarsTheme.typography.system)

        DollarsSlider(valueRange = 1f..50f, state = peopleInRoom)
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isChacked.value,
                onCheckedChange = { checked ->
                isChacked.value = checked
                AccesInRoom.value = checked},
                colors = CheckboxDefaults.colors(
                    //поменять цвет
                    checkedColor = DollarsTheme.color.menuIconColor,
                    //Добавить цвет
                    uncheckedColor = DollarsTheme.color.menuIconColor
                ))

            Text(text = stringResource(id = R.string.room_acces_info),
                color = DollarsTheme.color.textColor,
                style = DollarsTheme.typography.system)
        }
        if (isChacked.value) {
            DollarsOutlineTextField(label = stringResource(id = R.string.room_password),
                value = passwordRoom.value , onValueChange = { passwordRoom.value = it },
                keyboardOptions =  KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible.value) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon ={
                    val image = if (passwordVisible.value) {
                        R.drawable.ic_baseline_visibility_24
                    } else {
                        R.drawable.ic_baseline_visibility_off_24
                    }
                    val bescripton = if (passwordVisible.value) {
                        "Hide Password"
                    } else {
                        "Show password"
                    }
                    IconButton(onClick = {
                        passwordVisible.value = !passwordVisible.value
                    }) {
                        Icon(
                            painter = painterResource(id = image),
                            contentDescription = "Img"
                        )

                    }
                } )
            
            
//            TextField(value = PasswordRoom.value,
//                onValueChange = { PasswordRoom.value = it },
//                colors = TextFieldDefaults.textFieldColors(
////                    focusedIndicatorColor = AppTeme.color.Backround,
////                    disabledLabelColor = AppTeme.color.Backround,
////                    placeholderColor = AppTeme.color.Backround,
////                    disabledIndicatorColor = AppTeme.color.Backround
//                ),
//                label = {
//                    Text(
//                        text = "Password",
//                        color = DollarsTheme.color.textColor
//                    )
//                },
//                singleLine = true,
//                placeholder = { Text(text = "Password") },
//                visualTransformation = if (passwordVisible.value) {
//                    VisualTransformation.None
//                } else {
//                    PasswordVisualTransformation()
//                },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                trailingIcon = {
//                    val image = if (passwordVisible.value) {
//                        R.drawable.ic_baseline_visibility_24
//                    } else {
//                        R.drawable.ic_baseline_visibility_off_24
//                    }
//                    val bescripton = if (passwordVisible.value) {
//                        "Hide Password"
//                    } else {
//                        "Show password"
//                    }
//                    IconButton(onClick = {
//                        passwordVisible.value = !passwordVisible.value
//                    }) {
//                        Icon(
//                            painter = painterResource(id = image),
//                            contentDescription = "Img"
//                        )
//
//                    }
//                }
//
//            )

        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {
                val editor = roomPref.edit()
                    editor.putInt(ROOM_SIZE, peopleInRoom.value.toInt())
                    editor.putBoolean(ROOM_ACESS, !AccesInRoom.value)
                    editor.putString(ROOM_NAME,roomName.value)
                    editor.putInt(ROOM_SIZE,peopleInRoom.value.toInt())
                    editor.putString(ROOM_PASSWORD,passwordRoom.value)
                    editor.putString(ROOM_ID,profilePref.getString(USER_PROFILE_ID,"DEFAULT"))
                    editor.apply()
                    navController.navigate("main")
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = DollarsTheme.color.buttonColor)
            ) {
                Text(text = stringResource(id = R.string.confirm_button), color = DollarsTheme.color.textInButtonColor)
            }
            TextButton(onClick = {navController.popBackStack()}) {
                Text(text = stringResource(id = R.string.cancel), color = DollarsTheme.color.textColor)

            }
        }
    }

    
}
@Preview
@Composable
fun NewPrew(){
    DollarsTheme() {
        //NewRoomCreatedScreen()
    }

}