package com.example.dollarschat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dollarschat.data.FirebaseHelper
import com.example.dollarschat.domain.SettingsBundle
import com.example.dollarschat.screens.Chat.ChatView
import com.example.dollarschat.screens.Chat.ChatViewModel
import com.example.dollarschat.screens.authScreens.Registration.RegistrationView
import com.example.dollarschat.screens.authScreens.Registration.RegistrationViewModel
import com.example.dollarschat.screens.authScreens.SignIn.SignInViewModel
import com.example.dollarschat.screens.authScreens.SignIn.SignInview
import com.example.dollarschat.screens.authScreens.UserCreate.UserCreateView
import com.example.dollarschat.screens.authScreens.UserCreate.UserCreateViewModel
import com.example.dollarschat.screens.mainScreen.MainScreen
import com.example.dollarschat.ui.theme.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.example.dollarschat.data.FONT_SIZE
import com.example.dollarschat.data.PADDING_SIZE
import com.example.dollarschat.data.Settings
import com.example.dollarschat.screens.splashScreen.SplashScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var localUSer = remember { mutableStateOf(FirebaseHelper(this).getLoacalUser()) }
            var isDarkMode by rememberSaveable { mutableStateOf(Settings(this).getDarkMode()) }
            var currentFontSize by rememberSaveable { mutableStateOf(Settings(this).getSize(FONT_SIZE)) }
            var currentPaddingSize = rememberSaveable { mutableStateOf(Settings(this).getSize(PADDING_SIZE)) }
            var currentCornersStyle = rememberSaveable { mutableStateOf(DollarsCorners.Rounded) }
            var currentStyle by rememberSaveable(){ mutableStateOf(when(localUSer.value?.avatar?.color){
                "blue" -> {DollarsStyle.Blue}
                "black"->(DollarsStyle.Black)
                "green"->{DollarsStyle.Green}
                "yellow"->{DollarsStyle.Yellow}
                "red"->{DollarsStyle.Red}
                else->{DollarsStyle.Blue}
            }) }

            DollarsTheme(
                style = currentStyle,
                darkTheme = isDarkMode,
                textSize = currentFontSize,
                corners = currentCornersStyle.value,
                paddingSize = currentPaddingSize.value) {
                val navController = rememberNavController()
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = if (isDarkMode) nightColorPalette.tintColor else lightColorPalette.tintColor,
                        darkIcons = !isDarkMode
                    )
                }
                    NavHost(navController = navController, startDestination = "splash") {
                        composable("splash"){
                            SplashScreen(navController = navController)
                        }

                        composable("signin") {
                            val signInViewModel = hiltViewModel<SignInViewModel>()
                            SignInview(
                                signInViewModel = signInViewModel,
                                navController = navController
                            )
                        }
                        composable("reg") {
                            val regViewModel = hiltViewModel<RegistrationViewModel>()
                            RegistrationView(
                                registrationViewModel = regViewModel,
                                navController = navController
                            )
                        }
                        composable("userCreate") {
                            val userCreateViewModel = hiltViewModel<UserCreateViewModel>()
                            UserCreateView(
                                userCreateViewModel = userCreateViewModel,
                                navController = navController,
                                onSettingsChanged = { currentStyle = it },
                                userCreate ={localUSer.value = it}
                            )
                        }
//                        composable("chat") {
//                            val chatViewModel = hiltViewModel<ChatViewModel>()
//                            ChatView(chatViewModel = chatViewModel, navController = navController)
//                        }
                        composable("main") {
                            loadAllAvatar()
                            val settings = SettingsBundle(isDarkMode = isDarkMode,
                                style = currentStyle,
                                textSize = currentFontSize,
                                cornerStyle = currentCornersStyle.value,
                                paddingSize = currentPaddingSize.value,)
                            MainScreen(
                                navController = navController,
                                settings = settings,
                                onSettingsChanged = {
                                    isDarkMode = it.isDarkMode
                                    currentStyle = it.style
                                    currentFontSize= it.textSize
                                    currentCornersStyle.value=it.cornerStyle
                                    currentPaddingSize.value= it.paddingSize
                                },
                                localUser = localUSer.value,
                                localUserChange = {
                                    localUSer.value = it
                                },
                                mainColorChange = {currentStyle = it}
                            )
                        }
//                        composable("bottombar"){
//                            BottomBar(navController = navController)
//                        }
                    }

            }

        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            val top = insets.getInsets(WindowInsetsCompat.Type.ime()).top
            view.updatePadding(bottom = bottom, top = top )

            insets
        }
    }
    fun loadAllAvatar(){
        lifecycleScope.launch(Dispatchers.IO){
            FirebaseHelper(this@MainActivity).loadAllAvatarInMemory()
        }
    }

}


