package com.example.dollarschat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dollarschat.screens.SettingsScreen.SettingsScreen
import com.example.dollarschat.screens.mainScreen.MainBottomScreen
import com.example.dollarschat.screens.tabs.dailyFlow
import com.example.dollarschat.ui.theme.DollarsTheme

//@Composable
//fun BottomBar(navController: NavController){
//    val childNavController = rememberNavController()
//    val items = listOf(
//        MainBottomScreen.Chat,
//        MainBottomScreen.Settings,
//        MainBottomScreen.Profile
//    )
//    Column {
//        Box(modifier = Modifier.weight(1f)) {
//            NavHost(
//                navController = childNavController,
//                startDestination = MainBottomScreen.Chat.route
//            ) {
//                dailyFlow(navController)
//
//                composable(MainBottomScreen.Settings.route) {
//                  //  SettingsScreen()
//                }
//                composable(MainBottomScreen.Profile.route){
//                    UserProfile()
//                }
//            }
//        }
//    }
//    Box(
//        modifier = Modifier
//            .height(56.dp)
//            .fillMaxWidth()
//    ) {
//        BottomNavigation {
//            val navBackStackEntry by childNavController.currentBackStackEntryAsState()
//            val currentDestination = navBackStackEntry?.destination
//            val previousDestination = remember { mutableStateOf(items.first().route) }
//
//            items.forEach { screen ->
//                val isSelected = currentDestination?.hierarchy
//                    ?.any { it.route == screen.route } == true
//
//                BottomNavigationItem(
//                    modifier = Modifier.background(DollarsTheme.color.tintColor),
//                    icon = {
//                        Icon(
//                            imageVector = when (screen) {
//                                MainBottomScreen.Profile -> Icons.Filled.Favorite
//                                MainBottomScreen.Chat -> Icons.Filled.Favorite
//                                MainBottomScreen.Settings -> Icons.Filled.Settings
//                            },
//                            contentDescription = null,
//                            tint = if (isSelected) DollarsTheme.color.menuElementColor else DollarsTheme.color.iconRegistrationColor
//                        )
//                    },
//                    label = {
//                        Text(
//                            stringResource(id = screen.resourceId),
//                            color = if (isSelected) DollarsTheme.color.textColor else DollarsTheme.color.textColor
//                        )
//                    },
//                    selected = isSelected,
//                    onClick = {
//                        if (screen.route == previousDestination.value) return@BottomNavigationItem
//                        previousDestination.value = screen.route
//
//                        childNavController.navigate(screen.route) {
//                            popUpTo(childNavController.graph.findStartDestination().id) {
//                                saveState = true
//                            }
//
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    })
//            }
//        }
//    }
//
//}