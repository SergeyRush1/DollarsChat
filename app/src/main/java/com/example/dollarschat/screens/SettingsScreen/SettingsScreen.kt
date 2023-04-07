package com.example.dollarschat.screens.SettingsScreen

import android.content.Context
import android.graphics.BitmapFactory
import android.view.MenuItem
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.dollarschat.domain.SettingsBundle
import com.example.dollarschat.ui.theme.DollarsTheme
import com.example.dollarschat.ui.theme.uiComponents.MenuItemModel
import com.example.dollarschat.R
import com.example.dollarschat.data.*
import com.example.dollarschat.ui.theme.DollarsSize
import com.example.dollarschat.ui.theme.uiComponents.MenuItem
import com.example.dollarschat.ui.theme.uiComponents.MessageItem
import com.example.dollarschat.ui.theme.uiComponents.MyMessageItem

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    settings: SettingsBundle,
    onSettingsChanged: (SettingsBundle) -> Unit,
    localUser:UserProfile
) {
    Surface(
        modifier = modifier,
        color = DollarsTheme.color.backgroundItem,
    ) {
        Column() {
            val settingsmemorySaver = Settings(LocalContext.current)


            Row(
                modifier = Modifier.padding(DollarsTheme.shapes.padding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = com.example.dollarschat.R.string.action_dark_theme_enable),
                    color = DollarsTheme.color.textColor,
                    style = DollarsTheme.typography.body
                )
                Checkbox(
                    checked = settings.isDarkMode, onCheckedChange = {
                        onSettingsChanged.invoke(settings.copy(isDarkMode = !settings.isDarkMode))
                        settingsmemorySaver.setDarkMode(!settings.isDarkMode)
                    },
                    colors = CheckboxDefaults.colors(
                        //поменять цвет
                        checkedColor = DollarsTheme.color.menuIconColor,
                        //Добавить цвет
                        uncheckedColor = DollarsTheme.color.menuIconColor
                    )
                )
            }

            Divider(
                modifier = Modifier.padding(start = DollarsTheme.shapes.padding),
                thickness = 0.5.dp,
                color = DollarsTheme.color.textColor.copy(
                    alpha = 0.3f
                )
            )
            //  MARGIN
            MenuItem(
                model = MenuItemModel(
                    title = stringResource(id = R.string.title_padding_size),
                    currentIndex = when (settings.paddingSize) {
                        DollarsSize.Small -> 0
                        DollarsSize.Medium -> 1
                        DollarsSize.Big -> 2
                    },
                    values = listOf(
                        stringResource(id = R.string.title_padding_small),
                        stringResource(id = R.string.title_padding_medium),
                        stringResource(id = R.string.title_padding_big)
                    )
                ),
                onItemSelected = {
                    val settingsNew = settings.copy(
                        paddingSize = when (it) {
                            0 -> DollarsSize.Small
                            1 -> DollarsSize.Medium
                            2 -> DollarsSize.Big
                            else -> throw NotImplementedError("No valid value for this $it")
                        }
                    )
                    settingsmemorySaver.setSize(PADDING_SIZE,settingsNew.paddingSize)
                    onSettingsChanged.invoke(settingsNew)
                }
            )
            //TEXT SIZE
            MenuItem(
                model = MenuItemModel(
                    title = stringResource(id = R.string.title_font_size),
                    currentIndex = when (settings.textSize) {
                        DollarsSize.Small -> 0
                        DollarsSize.Medium -> 1
                        DollarsSize.Big -> 2
                    },
                    values = listOf(
                        stringResource(id = R.string.title_font_size_small),
                        stringResource(id = R.string.title_font_size_medium),
                        stringResource(id = R.string.title_font_size_big)
                    )
                ),
                onItemSelected = {
                    val settingsNew = settings.copy(
                        textSize = when (it) {
                            0 -> DollarsSize.Small
                            1 -> DollarsSize.Medium
                            2 -> DollarsSize.Big
                            else -> throw NotImplementedError("No valid value for this $it")
                        }
                    )
                    settingsmemorySaver.setSize(FONT_SIZE,settingsNew.textSize)
                    onSettingsChanged.invoke(settingsNew)
                }
            )
            MyMessageItem(bitmap = FirebaseHelper(LocalContext.current).paintBitmap(localUser.avatar!!.image_name)?:
            BitmapFactory.decodeResource(LocalContext.current.resources,R.drawable.ic_blueuser).asImageBitmap(),
                message = Message(user = localUser, stringResource(id = R.string.message_example)))

            MessageItem(bitmap = FirebaseHelper(LocalContext.current).paintBitmap(localUser.avatar!!.image_name)?:
            BitmapFactory.decodeResource(LocalContext.current.resources,R.drawable.ic_blueuser).asImageBitmap(),
                message = Message(user = localUser, stringResource(id = R.string.message_example)))



        }
    }
}