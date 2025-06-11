package view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.Font
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.philosopher_bold
import youkon.composeapp.generated.resources.philosopher_bolditalic
import youkon.composeapp.generated.resources.philosopher_italic
import youkon.composeapp.generated.resources.philosopher_regular

val philosopherFont: FontFamily
    @Composable
    get() {
        return FontFamily(
            Font(Res.font.philosopher_regular, weight = FontWeight.Normal),
            Font(Res.font.philosopher_bold, weight = FontWeight.Bold),
            Font(Res.font.philosopher_italic, style = FontStyle.Italic),
            Font(Res.font.philosopher_bolditalic, weight = FontWeight.Bold, style = FontStyle.Italic)
        )
    }

val grayBackground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray

/// For the subtract or reorder buttons, this gives them a consistent animation
@Composable
fun AnimatedVisibilityForControls(
    visible: Boolean,
    content: @Composable (AnimatedVisibilityScope.() -> Unit)
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandHorizontally(),
        exit = fadeOut() + shrinkHorizontally()
    ) {
        content()
    }
}

/// Button that appears left of projects or measurements to delete a single one
@Composable
fun SubtractButton(
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    IconButton(
        onClick = { onClick() }
    ) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = contentDescription,
            modifier = Modifier.editButtonModifier(
                color = MaterialTheme.colorScheme.error,
                alpha = 1f,
                width = 24.dp,
                height = 24.dp,
                padding = 4.dp,
                shape = MaterialTheme.shapes.medium
            ),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun UpDownButtons(
    contentDescriptionLeader: String,
    onClick: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(end = 8.dp)
    ) {
        for (direction in arrayOf("up", "down")) {
            IconButton(
                modifier = Modifier.editButtonModifier(
                    alpha = 0.5f,
                    padding = 4.dp,
                    shape = MaterialTheme.shapes.medium
                ),
                onClick = { onClick(direction) } // vm.value.onReorderControlButtonTap(project, direction) }
            ) {
                Icon(
                    imageVector = if (direction == "up") Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "$contentDescriptionLeader $direction" // "Reorder ${project.name} project $direction",
                )
            }
        }
    }
}

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