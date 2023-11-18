package com.dcsim.youkon.android

import android.graphics.BitmapFactory
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.android.views.createPaletteSync

@Composable
fun YoukonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val resources = LocalContext.current.resources

    // Create a palette based on the background image
    val backgroundBitmap = BitmapFactory.decodeResource(resources, R.drawable.background)
    val palette = createPaletteSync(backgroundBitmap)

    // Assign swatches from the palette
    val defaults = MaterialTheme.colorScheme
    val vibrant = Color(palette.getVibrantColor(defaults.primary.toArgb()))
    val vibrantLight = Color(palette.getLightVibrantColor(defaults.primary.toArgb()))
    val vibrantDark = Color(palette.getDarkVibrantColor(defaults.primary.toArgb()))
    val muted = Color(palette.getMutedColor(defaults.primary.toArgb()))
    val mutedLight = Color(palette.getLightMutedColor(defaults.primary.toArgb()))
    val mutedDark = Color(palette.getDarkMutedColor(defaults.primary.toArgb()))


    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = vibrant,
            secondary = mutedDark
        )
    } else {
        lightColorScheme(
            primary = vibrant,
            secondary = muted
        )
    }

    val shapes = Shapes(
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(8.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        //typography = typography,
        shapes = shapes,
        content = content
    )
}
