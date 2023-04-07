package com.example.dollarschat.ui.theme.uiComponents

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dollarschat.ui.theme.DollarsTheme
import java.time.temporal.ValueRange

@Composable
fun DollarsSlider(valueRange: ClosedFloatingPointRange<Float>,state:MutableState<Float>){
    Slider(
        value = state.value,
        onValueChange = {
            state.value = it
        },
        modifier = Modifier.padding(horizontal = 16.dp),
        valueRange = valueRange,
        steps = 0,
        colors = SliderDefaults.colors(
            thumbColor = DollarsTheme.color.unFocusColor,
            disabledThumbColor = Color.Red,
            inactiveTickColor = Color.Blue,
            activeTickColor = DollarsTheme.color.focusColor,
            activeTrackColor = DollarsTheme.color.focusColor,
            inactiveTrackColor = DollarsTheme.color.unFocusColor,
            disabledActiveTrackColor = Color.White,
            disabledInactiveTickColor = Color.Cyan,
            disabledInactiveTrackColor = Color.Green,
            disabledActiveTickColor = Color.Magenta
        )
    )
}
@Preview()
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SliderPrew(){
    val state = remember{ mutableStateOf(1f) }
    DollarsTheme() {
        DollarsSlider(1f..50f,state)
    }

}