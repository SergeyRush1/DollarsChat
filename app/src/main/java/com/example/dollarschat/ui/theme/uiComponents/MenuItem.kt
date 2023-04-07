package com.example.dollarschat.ui.theme.uiComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dollarschat.ui.theme.DollarsTheme
import com.example.dollarschat.R

data class MenuItemModel(
    val title: String,
    val currentIndex: Int = 0,
    val values: List<String>
)
@Composable
fun MenuItem(
    model: MenuItemModel,
    onItemSelected: ((Int) -> Unit)? = null
) {
    val isDropdownOpen = remember { mutableStateOf(false) }
    val currentPosition = remember { mutableStateOf(model.currentIndex) }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                Modifier
                    .clickable {
                        isDropdownOpen.value = true
                    }
                    .padding(DollarsTheme.shapes.padding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = DollarsTheme.shapes.padding),
                    text = model.title,
                    style = DollarsTheme.typography.body,
                    //добавить primaryText color
                    color = DollarsTheme.color.textColor
                )

                Text(
                    text = model.values[currentPosition.value],
                    style = DollarsTheme.typography.body,
                    //***************************
                    color = DollarsTheme.color.textColor
                )

                Icon(
                    modifier = Modifier
                        .padding(start = DollarsTheme.shapes.padding / 4)
                        .size(18.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = com.example.dollarschat.R.drawable.ic_baseline_arrow_24),
                    contentDescription = "Arrow",
                    tint = DollarsTheme.color.menuIconColor
                )
            }

            Divider(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.BottomStart),
                thickness = 0.5.dp,
                color = DollarsTheme.color.textColor.copy(
                    alpha = 0.3f
                )
            )
        }

        // Dropdown doesnt work
        // https://issuetracker.google.com/u/1/issues/211474319
        DropdownMenu(
            expanded = isDropdownOpen.value,
            onDismissRequest = {
                isDropdownOpen.value = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(DollarsTheme.color.backgroundItem)
        ) {
            model.values.forEachIndexed { index, value ->
                DropdownMenuItem(onClick = {
                    currentPosition.value = index
                    isDropdownOpen.value = false
                    onItemSelected?.invoke(index)
                }) {
                    Text(
                        text = value,
                        style = DollarsTheme.typography.body,
                        //******************
                        color = DollarsTheme.color.textColor
                    )
                }
            }
        }
    }
}
@Composable
@Preview
fun MenuItem_Preview() {
    DollarsTheme(
        darkTheme = true
    ) {
        MenuItem(
            model = MenuItemModel(
                title = stringResource(id = R.string.title_font_size),
                values = listOf(
                    stringResource(id = R.string.title_font_size_small),
                    stringResource(id = R.string.title_font_size_medium),
                    stringResource(id = R.string.title_font_size_big)
                )
            )
        )
    }
}