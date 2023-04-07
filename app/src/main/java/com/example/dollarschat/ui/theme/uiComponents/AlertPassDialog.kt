package com.example.dollarschat.ui.theme.uiComponents

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dollarschat.ui.theme.DollarsTheme
import com.example.dollarschat.R

@Composable
fun AlertPassDialog(canelButtonAlert:()->Unit,
                    okAlertDialogClick:(String)->Unit,
                    passwordState:Boolean){
    val state = remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = {},
        title = {
            when(passwordState){
                true-> { Text(text = stringResource(id = R.string.password_information),
                    color = DollarsTheme.color.textColor,
                    style = DollarsTheme.typography.system) }
                false -> { Text(text = stringResource(id = R.string.password_error),
                    color = DollarsTheme.color.errorColor, style = DollarsTheme.typography.system) }
            }
        },
        text = {
            TextField(modifier = Modifier.padding(top = 10.dp),
                value = state.value,
                onValueChange = {state.value = it},
                colors = TextFieldDefaults.textFieldColors(
                    textColor = DollarsTheme.color.textColor,
                    backgroundColor = DollarsTheme.color.backgroundItem,
                    disabledLabelColor = DollarsTheme.color.backround,
                    unfocusedIndicatorColor = DollarsTheme.color.focusColor,
                    focusedIndicatorColor = DollarsTheme.color.textColor,
                    cursorColor = DollarsTheme.color.textColor))
        },
        confirmButton = {
            TextButton(
                onClick =  canelButtonAlert) {
                Text(text = stringResource(id = R.string.cancel),
                    color = DollarsTheme.color.textColor)
            }
        },
        dismissButton = {
            DollarsButton(onClick = { okAlertDialogClick(state.value) },
                buttonColors = DollarsTheme.color.buttonColor,
                shape = DollarsTheme.shapes.cornerShape,
                content = {Text(text = stringResource(id = R.string.confirm_button),
                    color = DollarsTheme.color.textInButtonColor)})
        }, contentColor = DollarsTheme.color.tintColor
    )
}
@Preview
@Composable
fun alertPrew(){
    DollarsTheme {
       AlertPassDialog(okAlertDialogClick = {}, canelButtonAlert = {}, passwordState = true)
    }
}