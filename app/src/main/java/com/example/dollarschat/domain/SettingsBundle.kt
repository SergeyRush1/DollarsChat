package com.example.dollarschat.domain

import com.example.dollarschat.ui.theme.DollarsCorners
import com.example.dollarschat.ui.theme.DollarsSize
import com.example.dollarschat.ui.theme.DollarsStyle

data class SettingsBundle(
    val isDarkMode: Boolean,
    val textSize:DollarsSize,
    val paddingSize:DollarsSize,
    val cornerStyle:DollarsCorners,
    val style: DollarsStyle
)