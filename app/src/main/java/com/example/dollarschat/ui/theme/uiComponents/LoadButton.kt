package com.example.dollarschat.ui.theme.uiComponents

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.dollarschat.ui.theme.DollarsTheme

private const val MarginSingle = 5
private const val NumIndicators = 3
private const val IndicatorSize =12
private const val AnimationDurationMillis = 300
private const val AnimationDelayMillis = AnimationDurationMillis / 3
@Composable
fun LoadingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    buttonColors: ButtonColors,
    indicatorSpacing: Dp = MarginSingle.dp,
    shape: Shape,
    content: @Composable () -> Unit,
) {
    val contentAlpha by animateFloatAsState(targetValue = if (loading) 0f else 1f)
    val loadingAlpha by animateFloatAsState(targetValue = if (loading) 1f else 0f)

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors =buttonColors,
        shape = shape
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            LoadingIndicator(
                animating = loading,
                modifier = Modifier.graphicsLayer { alpha = loadingAlpha },
                color = buttonColors.contentColor(enabled = enabled).value,
                indicatorSpacing = indicatorSpacing,
            )
            Box(
                modifier = Modifier.graphicsLayer { alpha = contentAlpha }
            ) {
                content()
            }
        }
    }
}
@Composable
private fun LoadingDot(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(shape = CircleShape)
            .background(color = color)
            .height(IndicatorSize.dp)
            .width(IndicatorSize.dp)
    )
}
@Composable
private fun LoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = DollarsTheme.color.backround,
    indicatorSpacing: Dp = 1.dp,
    animating:Boolean
) {
    val animatedValues = List(NumIndicators) { index ->
        var animatedValue by remember(key1 = animating) { mutableStateOf(0f) }
        LaunchedEffect(key1 = animating) {
            animate(
                initialValue = IndicatorSize / 2f,
                targetValue = -IndicatorSize / 2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = AnimationDurationMillis),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset(AnimationDelayMillis * index),
                ),
            ) { value, _ -> animatedValue = value }
        }
        animatedValue
    }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        animatedValues.forEach{animatedValues->
            LoadingDot(
                color = DollarsTheme.color.textInButtonColor,
                modifier = Modifier
                    .padding(horizontal = indicatorSpacing, vertical = 10.dp)
                    .width(IndicatorSize.dp)
                    .aspectRatio(1f)
                    .offset(y = animatedValues.dp)
            )
        }
    }
}