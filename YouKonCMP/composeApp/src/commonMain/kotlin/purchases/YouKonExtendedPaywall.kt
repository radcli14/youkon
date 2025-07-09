package purchases

import androidx.compose.runtime.Composable

@Composable
expect fun YouKonExtendedPaywall(
    dismissRequest: () -> Unit,
    onPurchaseCompleted: () -> Unit
)
