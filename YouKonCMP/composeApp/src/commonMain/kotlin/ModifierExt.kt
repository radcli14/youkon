import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager


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