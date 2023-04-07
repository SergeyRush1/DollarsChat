package com.example.dollarschat.ui.theme.uiComponents

import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.dollarschat.ui.theme.DollarsTheme

@Composable
fun DollarsButton(onClick: () -> Unit,
                  modifier: Modifier = Modifier,
                  buttonColors: Color = DollarsTheme.color.buttonColor,
                  shape: Shape =DollarsTheme.shapes.cornerShape,
                  content: @Composable () -> Unit,){
    Button(onClick = onClick,
        modifier = modifier,
        shape = shape,
        colors = ButtonDefaults.buttonColors(buttonColors)) {
        content()

    }
}
@Preview
@Composable
fun ButPrew(){
    DollarsTheme() {
        DollarsButton(
            onClick = { /*TODO*/ },
            buttonColors = DollarsTheme.color.buttonColor,
            shape = DollarsTheme.shapes.cornerShape,
            content = { Text(text = "OK", color = DollarsTheme.color.textInButtonColor)})

        }
    }
