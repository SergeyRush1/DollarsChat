package com.example.dollarschat.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dollarschat.R


@Composable
fun DollarsTheme(style:DollarsStyle = DollarsStyle.Black,
                 textSize:DollarsSize = DollarsSize.Medium,
                 paddingSize:DollarsSize=DollarsSize.Medium,
                 corners: DollarsCorners=DollarsCorners.Rounded,
                 darkTheme:Boolean = isSystemInDarkTheme(),
                 content: @Composable () -> Unit
){
    val colors = when (darkTheme) {
        true ->{
            when(style){
                DollarsStyle.Blue -> blueNightColorPalette
                DollarsStyle.Black -> blackNightColorPalette
                DollarsStyle.Green -> greenNightColorPalette
                DollarsStyle.Red -> redNightColorPalette
                DollarsStyle.Yellow -> yellowNightColorPalette
            }
        }
        false ->{
            when(style){
                DollarsStyle.Blue -> blueLightPalette
                DollarsStyle.Black -> blackLightColorPalette
                DollarsStyle.Green -> greenLightColorPalette
                DollarsStyle.Red -> redLightColorPalette
                DollarsStyle.Yellow -> yellowLightColorPalette
            }

        }
    }
    val image =DollarsImage( when(darkTheme){
        false->R.drawable.ic_ligh_back
        true -> R.drawable.ic_night_back }
    )

    val typography = DollarsTypography(
        heading = TextStyle(
           fontSize = when(textSize){
               DollarsSize.Small ->24.sp
               DollarsSize.Medium -> 28.sp
               DollarsSize.Big -> 32.sp
           }, fontWeight = FontWeight.Normal
        ),
        body = TextStyle(
            fontSize = when(textSize){
                DollarsSize.Small -> 16.sp
                DollarsSize.Medium -> 20.sp
                DollarsSize.Big -> 26.sp
            }, fontWeight = FontWeight.Normal
        ),
        toolbar = TextStyle(
            fontSize = when (textSize){
                DollarsSize.Small -> 16.sp
                DollarsSize.Medium -> 20.sp
                DollarsSize.Big -> 26.sp
            }, fontWeight = FontWeight.Medium
        ),
        caption = TextStyle(
            fontSize = when (textSize){
                DollarsSize.Small -> 16.sp
                DollarsSize.Medium -> 18.sp
                DollarsSize.Big -> 20.sp
            }, fontWeight = FontWeight.Medium
        ), system = TextStyle(
            fontSize = when(textSize){
                DollarsSize.Small -> 16.sp
                DollarsSize.Medium -> 20.sp
                DollarsSize.Big -> 26.sp
            }, fontWeight = FontWeight.Normal
    ))


    val shapes = DollarsShape(
        padding = when(paddingSize){
            DollarsSize.Small -> 4.dp
            DollarsSize.Medium -> 16.dp
            DollarsSize.Big -> 12.dp
        },
        cornerShape = when(corners){
            DollarsCorners.Flat -> RoundedCornerShape(0.dp)
            DollarsCorners.Rounded -> RoundedCornerShape(25.dp)
        }
    )

    CompositionLocalProvider(
        localDollarsColor provides colors,
        localDollarsTypography provides typography,
        localDollarsShape provides shapes,
        localDollarsImage provides image,
        content = content
    )

}