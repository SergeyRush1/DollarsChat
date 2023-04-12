

package com.example.dollarschat.screens.mainScreen

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dollarschat.AnimatableExample
import com.example.dollarschat.domain.SettingsBundle
import com.example.dollarschat.screens.tabs.dailyFlow
import com.example.dollarschat.ui.theme.DollarsTheme
import com.example.dollarschat.R
import com.example.dollarschat.data.FirebaseHelper
import com.example.dollarschat.data.UserProfile
import com.example.dollarschat.screens.RoomCreator.NewRoomCreated
import com.example.dollarschat.screens.SettingsScreen.SettingsScreen
import com.example.dollarschat.screens.UserProfileScreen
import com.example.dollarschat.screens.UserProfileScreen.UserProfileView
import com.example.dollarschat.screens.UserProfileScreen.UserProfileViewModel
import com.example.dollarschat.ui.theme.DollarsStyle
import com.example.dollarschat.ui.theme.uiComponents.DrawerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    settings: SettingsBundle,
    onSettingsChanged: (SettingsBundle) -> Unit,
    localUser:UserProfile?,
    localUserChange:(UserProfile)->Unit,
    mainColorChange:(DollarsStyle)->Unit
) {
    val childNavController = rememberNavController()
    val items = listOf(
        MainBottomScreen.Profile,
        MainBottomScreen.Event,
        MainBottomScreen.Chat,
        MainBottomScreen.Settings,

    )
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val titleTopBar = remember { mutableStateOf("Chat") }

   Scaffold(
       scaffoldState = scaffoldState,
       topBar = {
           TopAppBar(
               title = {
                   Text(
                       text = titleTopBar.value,
                       color = DollarsTheme.color.textColor,
                       style = DollarsTheme.typography.toolbar
                   )
               },
//               navigationIcon = {
//                   IconButton(onClick = {
//                       coroutineScope.launch { scaffoldState.drawerState.open() }
//                   }) {
//                       Icon(
//                           painter = painterResource(R.drawable.ic_baseline_menu_24),
//                           contentDescription = "Menu",
//                           tint = DollarsTheme.color.menuIconColor
//                       )
//                   }
//               },
                actions = {
                   IconButton(onClick = { childNavController.navigate("createRoom") }) {
                       Icon(
                           painter = painterResource(id = R.drawable.ic_baseline_group_add_24),
                           contentDescription = "dddd",
                           tint = DollarsTheme.color.menuIconColor
                       )
                   }

                   IconButton(onClick = {
                       navController.popBackStack()
                       childNavController.navigate("chat")
                   }) {
                       Icon(painter = painterResource(id = R.drawable.ic_baseline_replay_24),
                           contentDescription = "replay" ,
                           tint = DollarsTheme.color.menuIconColor)

                   }
                   IconButton(onClick = {Firebase.auth.signOut()
                       navController.popBackStack()
                       navController.navigate("signin")
                   }) {
                       Icon(painter = painterResource(id = R.drawable.ic_outline_exit_to_app_24),
                           contentDescription = "exit", tint = DollarsTheme.color.menuIconColor)

                   }
               },
               backgroundColor = DollarsTheme.color.navigationElementColor
           )


       },
//       drawerContent = {
//       DrawerView(localUser!!)
//   }, drawerGesturesEnabled = true)
   ){
       Image(painter = painterResource(id = DollarsTheme.image.backgroundImage), contentDescription = "BackGround"
       , modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)

       Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.padding(it)) {
           Box(modifier = Modifier.weight(1f)) {
               NavHost(
                   navController = childNavController,
                   startDestination = MainBottomScreen.Chat.route
               ) {
                   dailyFlow(navController)

                   composable(MainBottomScreen.Settings.route) {
                       SettingsScreen(settings = settings,
                           onSettingsChanged = onSettingsChanged,
                           localUser = localUser!!)
                   }
                   composable(MainBottomScreen.Profile.route) {
                     val profileViewModel = hiltViewModel<UserProfileViewModel>()
                       UserProfileView(
                           profileViewModel = profileViewModel,
                           localUser = localUser!!,
                           changeLocalUser = localUserChange,
                           mainColorChange = mainColorChange
                       )
                   }
                   composable(MainBottomScreen.Event.route){
                       AnimatableExample()
                   }
               }
           }

           Box(
               modifier = Modifier
                   .height(56.dp)
                   .fillMaxWidth()
           ) {
               BottomNavigation {
                   val navBackStackEntry by childNavController.currentBackStackEntryAsState()
                   val currentDestination = navBackStackEntry?.destination
                   val previousDestination = remember { mutableStateOf(items[2].route) }

                   items.forEach { screen ->
                       val isSelected = currentDestination?.hierarchy
                           ?.any { it.route == screen.route } == true

                       BottomNavigationItem(
                           modifier = Modifier.background(DollarsTheme.color.tintColor),
                           icon = {
                               Icon(painter = when(screen){
                                   MainBottomScreen.Profile -> painterResource(id = screen.resourceId)
                                   MainBottomScreen.Chat-> painterResource(id = screen.resourceId)
                                   MainBottomScreen.Settings -> painterResource(id = screen.resourceId)
                                   MainBottomScreen.Event-> painterResource(id = screen.resourceId)

                               }, contentDescription = null,
                                   tint = if (isSelected) DollarsTheme.color.mainColor else DollarsTheme.color.textColor)
                           },
                           label = {
                               Text(
                                   stringResource(id = screen.resName),
                                   color = if (isSelected) DollarsTheme.color.mainColor else DollarsTheme.color.textColor
                               )
                           },
                           selected = isSelected,
                           onClick = {
                              if (screen.route == previousDestination.value) return@BottomNavigationItem
                               previousDestination.value = screen.route
                               titleTopBar.value=screen.route

                               childNavController.navigate(screen.route) {
                                   popUpTo(childNavController.graph.findStartDestination().id) {
                                       saveState = true
                                   }

                                   launchSingleTop = true
                                   restoreState = true
                               }
                           })
                   }
               }
           }
       }
       AnimatableExample()
   }




}