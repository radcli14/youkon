package view

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
    shape: Shape = CircleShape
) = composed {
    this
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

val animatedColor: Color
    @Composable
    get() {
        val infiniteTransition = rememberInfiniteTransition()
        val color by infiniteTransition.animateColor(
            initialValue = MaterialTheme.colorScheme.primary,
            targetValue = MaterialTheme.colorScheme.primaryContainer,
            animationSpec = infiniteRepeatable(
                animation = tween(750, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "Transition color to draw attention to sections of the view in the onboarding"
        )
        return color
    }

/// Provides a view modifier for a colored shadow if the selected view is highlighted in the onboarding screen
fun Modifier.onboardingModifier(isHighlighted: Boolean): Modifier = composed {
    if (isHighlighted)
        this.border(width = 3.dp, color = animatedColor, shape = MaterialTheme.shapes.large)
    else
        this
}