package com.dcengineer.youkon

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue

@Composable
expect fun CustomDecimalTextField(
    value: TextFieldValue,
    modifier: Modifier,
    onValueChange: (TextFieldValue) -> Unit
)