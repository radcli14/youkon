package com.dcsim.youkon.android.views

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun NameText(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.subtitle1,
        fontWeight = FontWeight.SemiBold
    )
}