package com.dcsim.youkon.android.views

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun CloseButton(expansion: ProjectExpansionLevel, onClick: () -> Unit) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = closeImageVector(expansion),
            contentDescription = "Close",
            tint = MaterialTheme.colors.primary
        )
    }
}


/// Decides what icon to show dependent on whether the project is expanded or not
fun closeImageVector(expansion: ProjectExpansionLevel): ImageVector {
    return when(expansion) {
        ProjectExpansionLevel.STATIC -> Icons.Default.Close
        else -> Icons.Default.ArrowDropDown
    }
}