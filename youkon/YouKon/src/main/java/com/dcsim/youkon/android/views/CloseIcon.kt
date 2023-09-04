package com.dcsim.youkon.android.views

import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.dcsim.youkon.ProjectExpansionLevel

@Composable
fun CloseIcon(expansion: ProjectExpansionLevel) {
    Icon(
        imageVector = closeImageVector(expansion),
        contentDescription = "Close",
        tint = MaterialTheme.colors.primary
    )
}


/// Decides what icon to show dependent on whether the project is expanded or not
fun closeImageVector(expansion: ProjectExpansionLevel): ImageVector {
    return when(expansion) {
        ProjectExpansionLevel.COMPACT -> Icons.Default.KeyboardArrowRight
        else -> Icons.Default.KeyboardArrowDown
    }
}