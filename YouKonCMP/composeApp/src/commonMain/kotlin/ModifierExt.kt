import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp


/// Modifier used to close the bottom sheet when tapping outside of it
fun Modifier.closeSheetOnTapOutside(stopEditing: () -> Unit) = composed {
    Modifier.pointerInput(Unit) {
        detectTapGestures(
            onTap = { stopEditing() }
        )
    }
}

/// Modifier used to close the keyboard when tapping outside of it
fun Modifier.closeKeyboardOnTapOutside() = composed {
    val localFocusManager = LocalFocusManager.current
    Modifier.pointerInput(Unit) {
        detectTapGestures(
            onTap = { localFocusManager.clearFocus() }
        )
    }
}

fun Modifier.fullWidthSemitransparentPadded() = composed {
    Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .padding(bottom = 32.dp)
        .background(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.69f),
            shape = MaterialTheme.shapes.large
        )
}
