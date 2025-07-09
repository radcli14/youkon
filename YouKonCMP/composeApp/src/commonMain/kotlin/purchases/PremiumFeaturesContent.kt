package purchases

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import theming.defaultPadding
import org.jetbrains.compose.resources.stringResource
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.extend_youkon
import youkon.composeapp.generated.resources.have_extended
import youkon.composeapp.generated.resources.purchase_error_generic
import youkon.composeapp.generated.resources.purchases
import youkon.composeapp.generated.resources.want_new_features


@Composable
fun PremiumFeaturesContent(
    extendedPurchaseState: ExtendedPurchaseState,
    showPaywall: () -> Unit
) {
    Column(
        modifier = Modifier.defaultPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.purchases),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        when (extendedPurchaseState) {
            ExtendedPurchaseState.BASIC -> {
                Text(stringResource(Res.string.want_new_features), color = MaterialTheme.colorScheme.onSurface)
                Button(onClick = showPaywall) {
                    Text(stringResource(Res.string.extend_youkon), color = MaterialTheme.colorScheme.onSurface)
                }
            }
            ExtendedPurchaseState.EXTENDED -> {
                Text(stringResource(Res.string.have_extended), color = MaterialTheme.colorScheme.onSurface)
            }
            ExtendedPurchaseState.ERROR -> {
                Text(stringResource(Res.string.purchase_error_generic), color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }

}
