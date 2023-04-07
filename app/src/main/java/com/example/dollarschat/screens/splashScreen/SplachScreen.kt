package com.example.dollarschat.screens.splashScreen

import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.dollarschat.R
import com.example.dollarschat.data.FirebaseHelper
import com.example.dollarschat.ui.theme.DollarsTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavController){
    val corutine = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = DollarsTheme.image.backgroundImage), contentDescription = "BackGround"
            , modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)

    }
    Image(painter = painterResource(id = R.drawable.ic_dollars), contentDescription = "logo")
    val firebaseHelper = FirebaseHelper(LocalContext.current)

    LaunchedEffect(key1 = corutine, block = {
        if (firebaseHelper.authCheck()){
            navController.navigate("main")
        }else{
            navController.navigate("signin")
        }
    })
}