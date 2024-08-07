package com.dcengineer.youkon

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign

/// Intended to hold decimal field with additional buttons over top
@Composable
actual fun CustomDecimalTextField(
    value: TextFieldValue,
    modifier: Modifier,
    onValueChange: (TextFieldValue) -> Unit,
) {
    //val textField = remember { CustomAppleDecimalTextField() }
    /*
    DisposableEffect(Unit) {
        onDispose {
            textField.inputAccessoryView = null
        }
    }*/

    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.End
    )

    BasicTextField(
        value = value,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        onValueChange = { newText ->
            onValueChange(newText)
        },
        textStyle = textStyle.copy(
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End
        ),
    )
}