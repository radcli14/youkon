package com.dcsim.youkon.android.views

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val roundedRadius = 8.dp

val grayBackground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray

@Composable
fun Modifier.editButtonModifier(
    color: Color = grayBackground,
    alpha: Float = 0.7f,
    width: Dp = 42.dp,
    height: Dp = 36.dp,
    padding: Dp = 8.dp,
    shape: Shape = RoundedCornerShape(roundedRadius)
) = composed {
    Modifier
        .background(
            color = color.copy(alpha = alpha),
            shape = shape
        )
        .width(width)
        .height(height)
        .padding(padding)
}

