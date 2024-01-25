package com.dcengineer.youkon.views

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.dcengineer.youkon.ProjectExpansionLevel

/// A right arrow icon that rotates 90 deg when the project is expanded, to achieve an effect
/// similar to a `DisclosureGroup` in iOS
@Composable
fun CloseIcon(
    expansion: ProjectExpansionLevel
) {
    // The rotation angle in degrees
    val rotation = remember {
        Animatable(if (expansion == ProjectExpansionLevel.COMPACT) 0f else 90f)
    }

    // Animation on change of the `ProjectExpansionLevel`, when it is the compact state, will be
    // oriented facing right, when that changes to static, rotates by 90 deg to face downward
    LaunchedEffect(expansion) {
        when (expansion) {
            ProjectExpansionLevel.COMPACT -> { rotation.animateTo(0f) }
            else -> { rotation.animateTo(90f) }
        }
    }

    Icon(
        imageVector = Icons.Default.KeyboardArrowRight,
        contentDescription = "Close",
        modifier = Modifier.rotate(rotation.value).size(24.dp),
        tint = MaterialTheme.colorScheme.primary
    )
}
