package com.example.dollarschat.ui.theme.uiComponents

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable

fun ScrollToTopButton(onClick:()->Unit){
   Button(
        onClick = onClick){
       Text(text = "TOP")
   }
    

}
