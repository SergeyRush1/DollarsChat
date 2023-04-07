package com.example.dollarschat.ui.theme.uiComponents

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.dollarschat.ui.theme.DollarsTheme

@Composable
fun DollarsOutlineTextField(
    label:String,
    value:String,
    onValueChange:(String)->Unit,
    isError:Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable() (() -> Unit) = {}) {



    OutlinedTextField(value = value,
        onValueChange = onValueChange,
        textStyle = DollarsTheme.typography.body,
        modifier = Modifier.padding(DollarsTheme.shapes.padding),
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = DollarsTheme.color.textColor,
            disabledTextColor = DollarsTheme.color.editTextBack,
            backgroundColor = DollarsTheme.color.editTextBack,
            cursorColor = DollarsTheme.color.focusColor,
            errorCursorColor = DollarsTheme.color.errorColor,
            focusedBorderColor = DollarsTheme.color.focusColor,
            unfocusedBorderColor = DollarsTheme.color.unFocusColor,
            disabledBorderColor = Color.Blue,
            errorBorderColor = DollarsTheme.color.errorColor,
            leadingIconColor = Color.Red,
            disabledLeadingIconColor = Color.Red,
            errorLeadingIconColor = Color.Red,
            trailingIconColor = DollarsTheme.color.focusColor,
            disabledTrailingIconColor = DollarsTheme.color.unFocusColor,
            errorTrailingIconColor = DollarsTheme.color.errorColor,
            focusedLabelColor = DollarsTheme.color.focusColor,
            unfocusedLabelColor = DollarsTheme.color.unFocusColor,
            disabledLabelColor = Color.Red,
            errorLabelColor = DollarsTheme.color.errorColor,
            placeholderColor = Color.Red,
            disabledPlaceholderColor = Color.Yellow

        ), label = { Text(text = label, style = DollarsTheme.typography.system )},
        singleLine = true,
        shape = DollarsTheme.shapes.cornerShape)
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TextFieldPrew(){
    DollarsTheme {
        val state = remember{ mutableStateOf("") }

    }
}