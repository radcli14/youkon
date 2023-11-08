package com.dcsim.youkon.android.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.editButtonModifier(
    color: Color = MaterialTheme.colors.surface,
    alpha: Float = 0.7f,
    width: Dp = 42.dp,
    height: Dp = 36.dp,
    padding: Dp = 8.dp,
    shape: Shape = RoundedCornerShape(8.dp)
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