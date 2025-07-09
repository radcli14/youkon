package purchases

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
actual fun YouKonExtendedPaywall(
    dismissRequest: () -> Unit,
    onPurchaseCompleted: () -> Unit
) {
    Text("Sorry, YouKon extended is not available on this platform.")
} 