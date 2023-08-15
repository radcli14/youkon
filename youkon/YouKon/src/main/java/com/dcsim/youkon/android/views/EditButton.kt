package com.dcsim.youkon.android.views

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable

@Composable
fun EditButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Close",
            tint = MaterialTheme.colors.primary
        )
    }
}


@Composable
fun PlusButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            tint = MaterialTheme.colors.primary
        )
    }
}


@Composable
fun MinusButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Subtract",
            tint = MaterialTheme.colors.primary
        )
    }
}