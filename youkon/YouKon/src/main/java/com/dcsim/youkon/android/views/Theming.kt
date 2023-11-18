package com.dcsim.youkon.android.views

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette

val roundedRadius = 8.dp

fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

val grayBackground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray

@Composable
fun Modifier.editButtonModifier(
    color: Color = grayBackground,
    alpha: Float = 0.7f,
    width: Dp = 36.dp,
    height: Dp = 36.dp,
    padding: Dp = 8.dp,
    shape: Shape = MaterialTheme.shapes.medium
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


@Composable
fun pickerColor(isSelected: Boolean): Color {
    return if (isSelected) pickerSelectedColor else grayBackground
}

val pickerSelectedColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface

val pickerTextColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
