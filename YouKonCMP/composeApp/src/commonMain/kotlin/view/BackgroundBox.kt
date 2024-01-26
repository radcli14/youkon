package view

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import getPlatform
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

/// Holds the background image, content overlaid on top of it
@OptIn(ExperimentalResourceApi::class)
@Composable
fun BackgroundBox(content: @Composable () -> Unit) {
    val isIphone = "iOS" in getPlatform().name
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource("background.png"),
            contentDescription = "Background image of mountains",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.colorMatrix(
                colorMatrix(if (isSystemInDarkTheme()) -80f else 80f)
            )
        )
        Box(
            modifier = Modifier
                .padding(
                    top = if (isIphone) 36.dp else 0.dp,
                    bottom = if (isIphone) 12.dp else 0.dp
                )
        ) {
            content()
        }
    }
}

fun colorMatrix(brightness: Float): ColorMatrix {
    val matrixAsArray = floatArrayOf(
        1f, 0f, 0f, 0f, brightness,
        0f, 1f, 0f, 0f, brightness,
        0f, 0f, 1f, 0f, brightness,
        0f, 0f, 0f, 1f, 0f
    )
    return ColorMatrix(matrixAsArray)
}