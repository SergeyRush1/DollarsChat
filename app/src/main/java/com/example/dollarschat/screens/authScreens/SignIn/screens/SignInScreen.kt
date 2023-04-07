package com.example.dollarschat.screens.authScreens.SignIn.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dollarschat.R
import com.example.dollarschat.screens.authScreens.SignIn.SignInViewModel
import com.example.dollarschat.screens.authScreens.SignIn.models.SignInEvent
import com.example.dollarschat.screens.authScreens.SignIn.models.SignInState
import com.example.dollarschat.ui.theme.DollarsColor
import com.example.dollarschat.ui.theme.DollarsTheme
import com.example.dollarschat.ui.theme.uiComponents.DollarsOutlineTextField
import com.example.dollarschat.ui.theme.uiComponents.LoadingButton

@Composable
fun signInScreen(state:SignInState.SignIn,
                 regButton:()->Unit,
                 onMailChanged:(String)->Unit,
                 onPasswordChanged:(String)->Unit,
                 passVisibleChange:(Boolean)->Unit,
                 loadButtonClick: (NavController)->Unit,
                 navController: NavController) {
    val email = remember{ mutableStateOf("") }
    val password = remember{ mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding(bottom = 300.dp), verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_dollars), contentDescription = "main LOGO",
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Inside
        )
    }
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            DollarsOutlineTextField(label = stringResource(id = R.string.email),
                value =email.value ,
                onValueChange = { onMailChanged(it)
                                email.value = it},
                isError = !state.emailCheck)
//            TextField(
//                value = state.mail,
//                onValueChange = onMailChanged,
//                modifier = Modifier.padding(DollarsTheme.shapes.padding),
//                colors = TextFieldDefaults.textFieldColors(
//                    textColor = DollarsTheme.color.textColor,
//                    backgroundColor = DollarsTheme.color.tintColor,
//                    disabledLabelColor = DollarsTheme.color.backround,
//                    unfocusedIndicatorColor = emailColor,
//                    focusedIndicatorColor = emailColor,
//                    cursorColor = emailColor
//                ),
//                label = { Text(text = "MAIL", color = emailColor) },
//                singleLine = true,
//
//            )
        }
        Spacer(modifier = Modifier.size(20.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            DollarsOutlineTextField(label = stringResource(id = R.string.password),
                onValueChange = { password.value = it
                                onPasswordChanged(it)} ,
                isError = !state.passCheck,
                value = password.value ,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (state.passwordVisible){
                    VisualTransformation.None
                }else{
                    PasswordVisualTransformation() },
                trailingIcon = {
                    val pasWisibleIcon = if (state.passwordVisible){
                        R.drawable.ic_baseline_visibility_24
                    }else{R.drawable.ic_baseline_visibility_off_24}
                    val bescripton = if (state.passwordVisible) {
                        "Hide Password"
                    } else {
                        "Show password"
                    }
                    IconButton(onClick = {passVisibleChange(state.passwordVisible)}) {
                        Icon(painter = painterResource(id = pasWisibleIcon),
                            contentDescription = bescripton, tint = DollarsTheme.color.textInButtonColor )
                    }
                })




//            TextField(value = state.password,
//                onValueChange = onPasswordChanged,
//                modifier = Modifier.padding(DollarsTheme.shapes.padding),
//                colors = TextFieldDefaults.textFieldColors(
//                    textColor = DollarsTheme.color.textColor,
//                    backgroundColor = DollarsTheme.color.tintColor,
//                    disabledLabelColor = DollarsTheme.color.backround,
//                    unfocusedIndicatorColor = passColor,
//                    focusedIndicatorColor = passColor,
//                    cursorColor = passColor),
//                label = { Text(text = "PASSWORD", color = passColor)},
//            visualTransformation = if (state.passwordVisible){
//                VisualTransformation.None
//            }else{
//                PasswordVisualTransformation()
//            },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                trailingIcon = {
//                    val pasWisibleIcon = if (state.passwordVisible){
//                        R.drawable.ic_baseline_visibility_24
//                    }else{R.drawable.ic_baseline_visibility_off_24}
//                    val bescripton = if (state.passwordVisible) {
//                        "Hide Password"
//                    } else {
//                        "Show password"
//                    }
//                    IconButton(onClick = {passVisibleChange(state.passwordVisible)}) {
//                        Icon(painter = painterResource(id = pasWisibleIcon),
//                            contentDescription = bescripton, tint = DollarsTheme.color.textInButtonColor )
//                    }
//                }
//            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp), horizontalArrangement = Arrangement.Center) {
        LoadingButton(onClick = { loadButtonClick(navController) },
            buttonColors = ButtonDefaults.buttonColors(DollarsTheme.color.buttonColor),
            shape = DollarsTheme.shapes.cornerShape, loading = state.loadingButtonState) {
            Text(text = stringResource(id = R.string.confirm_button))
        }
            Box(modifier = Modifier) {
                TextButton(onClick = regButton) {
                    Text(text = "Регистрация", color = DollarsTheme.color.textColor)
                }
            }
        }
      }
    }



@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun signInPrew(){
    DollarsTheme(){
      //  signInScreen()
    }

}