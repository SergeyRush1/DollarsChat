package com.example.dollarschat.screens.authScreens.Registration.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dollarschat.R
import com.example.dollarschat.screens.authScreens.Registration.models.RegistrationEvent
import com.example.dollarschat.screens.authScreens.Registration.models.RegistrationState
import com.example.dollarschat.ui.theme.DollarsTheme
import com.example.dollarschat.ui.theme.uiComponents.DollarsOutlineTextField
import com.example.dollarschat.ui.theme.uiComponents.LoadingButton

@Composable
fun RegistrationScreen(state:RegistrationState.Registration,
                       onMailChanged:(String)->Unit,
                       onPasswordChanged:(String)->Unit,
                       onDoublePassChange:(String)->Unit,
                       passVisibleChange:(Boolean)->Unit,
                       doublePassVisibleChange:(Boolean)->Unit,
                       regButtonClick:(NavController)->Unit,
                       navController: NavController) {

    var emailColor = DollarsTheme.color.errorColor
    var passColor=DollarsTheme.color.errorColor
    var doublePassColor=DollarsTheme.color.errorColor
    if (state.emailCheck){
        emailColor = DollarsTheme.color.textColor
    }
    if (state.firstPassCheck){
        passColor = DollarsTheme.color.textColor
    }
    if (state.secondPassCheck){
        doublePassColor = DollarsTheme.color.textColor
    }
    
    DollarsTheme() {
        Column(
            modifier = Modifier
                .padding(bottom = 300.dp), verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_dollars), contentDescription = "main LOGO",
                alignment = Alignment.TopCenter,
               modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Inside
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom)
        {

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                DollarsOutlineTextField(label = stringResource(id = R.string.email),
                    value =state.email ,
                    onValueChange = onMailChanged ,
                    isError = !state.emailCheck)
//                TextField(
//                    value = state.email,
//                    onValueChange = onMailChanged,
//                    modifier = Modifier.padding(DollarsTheme.shapes.padding),
//                    colors = TextFieldDefaults.textFieldColors(
//                        textColor = DollarsTheme.color.textColor,
//                        backgroundColor = DollarsTheme.color.tintColor,
//                        disabledLabelColor = DollarsTheme.color.backround,
//                        unfocusedIndicatorColor = emailColor,
//                        focusedIndicatorColor = emailColor,
//                        cursorColor = emailColor
//                    ),
//                    label = { Text(text = "MAIL", color = emailColor) },
//                    singleLine = true,
//
//                    )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {


                DollarsOutlineTextField(label = stringResource(id = R.string.password),
                    onValueChange = onPasswordChanged,
                    isError = !state.firstPassCheck,
                    value = state.firstPass ,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (state.firstPassVisible){
                        VisualTransformation.None
                    }else{
                        PasswordVisualTransformation() },
                    trailingIcon = {
                        val pasWisibleIcon = if (state.firstPassVisible){
                            R.drawable.ic_baseline_visibility_24
                        }else{R.drawable.ic_baseline_visibility_off_24}
                        val bescripton = if (state.firstPassVisible) {
                            "Hide Password"
                        } else {
                            "Show password"
                        }
                        IconButton(onClick = {passVisibleChange(state.firstPassVisible)}) {
                            Icon(painter = painterResource(id = pasWisibleIcon),
                                contentDescription = bescripton, tint = DollarsTheme.color.textInButtonColor )
                        }
                    })
//                TextField(value = state.firstPass,
//                    onValueChange = onPasswordChanged,
//                    modifier = Modifier.padding(DollarsTheme.shapes.padding),
//                    colors = TextFieldDefaults.textFieldColors(
//                        textColor = DollarsTheme.color.textColor,
//                        backgroundColor = DollarsTheme.color.tintColor,
//                        disabledLabelColor = DollarsTheme.color.backround,
//                        unfocusedIndicatorColor = passColor,
//                        focusedIndicatorColor = passColor,
//                        cursorColor = passColor
//                    ),
//                    label = { Text(text = "PASSWORD", color = passColor) },
//                    visualTransformation = if (state.firstPassVisible) {
//                        VisualTransformation.None
//                    } else {
//                        PasswordVisualTransformation()
//                    },
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                    trailingIcon = {
//                        val pasWisibleIcon = if (state.firstPassVisible) {
//                            R.drawable.ic_baseline_visibility_24
//                        } else {
//                            R.drawable.ic_baseline_visibility_off_24
//                        }
//                        val bescripton = if (state.firstPassVisible) {
//                            "Hide Password"
//                        } else {
//                            "Show password"
//                        }
//                        IconButton(onClick = { passVisibleChange(state.firstPassVisible) }) {
//                            Icon(
//                                painter = painterResource(id = pasWisibleIcon),
//                                contentDescription = bescripton,
//                                tint = DollarsTheme.color.menuIconColor
//                            )
//                        }
//                    }
//                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                DollarsOutlineTextField(label = stringResource(id = R.string.password),
                    onValueChange = onDoublePassChange,
                    isError = !state.secondPassCheck,
                    value = state.secondPAss ,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (state.secondPassVisible){
                        VisualTransformation.None
                    }else{
                        PasswordVisualTransformation() },
                    trailingIcon = {
                        val pasWisibleIcon = if (state.secondPassVisible){
                            R.drawable.ic_baseline_visibility_24
                        }else{R.drawable.ic_baseline_visibility_off_24}
                        val bescripton = if (state.secondPassVisible) {
                            "Hide Password"
                        } else {
                            "Show password"
                        }
                        IconButton(onClick = {passVisibleChange(state.secondPassVisible)}) {
                            Icon(painter = painterResource(id = pasWisibleIcon),
                                contentDescription = bescripton, tint = DollarsTheme.color.textInButtonColor )
                        }
                    })
//                TextField(value = state.secondPAss,
//                    onValueChange = onDoublePassChange,
//                    modifier = Modifier.padding(DollarsTheme.shapes.padding),
//                    colors = TextFieldDefaults.textFieldColors(
//                        textColor = DollarsTheme.color.textColor,
//                        backgroundColor = DollarsTheme.color.tintColor,
//                        disabledLabelColor = DollarsTheme.color.backround,
//                        unfocusedIndicatorColor = doublePassColor,
//                        focusedIndicatorColor = doublePassColor,
//                        cursorColor = doublePassColor
//                    ),
//                    label = { Text(text = "PASSWORD", color = doublePassColor) },
//                    visualTransformation = if (state.secondPassVisible) {
//                        VisualTransformation.None
//                    } else {
//                        PasswordVisualTransformation()
//                    },
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                    trailingIcon = {
//                        val pasWisibleIcon = if (state.secondPassVisible) {
//                            R.drawable.ic_baseline_visibility_24
//                        } else {
//                            R.drawable.ic_baseline_visibility_off_24
//                        }
//                        val bescripton = if (state.secondPassVisible) {
//                            "Hide Password"
//                        } else {
//                            "Show password"
//                        }
//                        IconButton(onClick = { doublePassVisibleChange(state.secondPassVisible) }) {
//                            Icon(
//                                painter = painterResource(id = pasWisibleIcon),
//                                contentDescription = bescripton,
//                                tint = DollarsTheme.color.menuIconColor
//                            )
//                        }
//                    }
//                )

            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
            LoadingButton(onClick = { regButtonClick(navController) },
                buttonColors = ButtonDefaults.buttonColors(DollarsTheme.color.buttonColor),
                shape = DollarsTheme.shapes.cornerShape,
                loading = state.loadState) {
                Text(text = "Зарегестрироваться", color = DollarsTheme.color.textColor)
            }
        }
            Spacer(modifier = Modifier.size(20.dp))
        }






    }
}