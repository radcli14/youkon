package com.dcsim.youkon.android.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun BasicTextFieldWithHint(
    value: String,
    hint: String = "",
    textStyle: TextStyle = MaterialTheme.typography.body1,
    onValueChange: (String) -> Unit = {}
) {
    Box {
        if (value.isBlank()) {
            Text(hint,
                style = textStyle,
                color = Color.Gray
            )
        }
        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            textStyle = textStyle,
        )
    }
}