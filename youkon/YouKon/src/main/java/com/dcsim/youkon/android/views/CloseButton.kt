package com.dcsim.youkon.android.views

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable

@Composable
fun CloseButton(isExpanded: Boolean, onClick: () -> Unit) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.ArrowDropDown,
            contentDescription = "Close",
            tint = MaterialTheme.colors.primary
        )
    }
}