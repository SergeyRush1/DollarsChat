package com.example.dollarschat.ui.theme.uiComponents

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dollarschat.R
import com.example.dollarschat.ui.theme.DollarsTheme

@Composable
fun DollarsThemeAlertDialog(text:String,onClick:()->Unit){
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = text, style = DollarsTheme.typography.system,
            color = DollarsTheme.color.textColor) },
        confirmButton = {
                DollarsButton(onClick =  onClick,
                    buttonColors = DollarsTheme.color.buttonColor,
                    shape = DollarsTheme.shapes.cornerShape,
                    content = {
                        Text(text = stringResource(id = R.string.confirm_button),
                            color = DollarsTheme.color.textInButtonColor)
                    })
        }, contentColor = DollarsTheme.color.tintColor,
        backgroundColor = DollarsTheme.color.backround
    )
}
@Composable
fun DollarsAlertDialog(text:String,onOkClick:()->Unit,onCancelClick:()->Unit){
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = text, style = DollarsTheme.typography.system,
            color = DollarsTheme.color.textColor) },
        confirmButton = {
            DollarsButton(onClick =  onOkClick,
                buttonColors = DollarsTheme.color.buttonColor,
                shape = DollarsTheme.shapes.cornerShape,
                content = {
                    Text(text = stringResource(id = R.string.confirm_button),
                        color = DollarsTheme.color.textInButtonColor)
                })
        }, dismissButton = {
                           TextButton(onClick = onCancelClick) {
                               Text(text = stringResource(id = R.string.cancel),
                                   color = DollarsTheme.color.textColor,
                                   style = DollarsTheme.typography.system)
                           }
        }, contentColor = DollarsTheme.color.tintColor,
        backgroundColor = DollarsTheme.color.backround
    )
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun aletrtDialogPrew(){
    DollarsTheme() {
        DollarsThemeAlertDialog(text = "Комната переполнена ") {
        }
    }
}