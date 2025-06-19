package purchases

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import defaultPadding
import org.jetbrains.compose.resources.stringResource
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.extend_youkon
import youkon.composeapp.generated.resources.have_extended
import youkon.composeapp.generated.resources.purchase_error_generic
import youkon.composeapp.generated.resources.want_new_features


@Composable
fun PremiumFeaturesContent(
    extendedPurchaseState: PurchasesRepository.ExtendedPurchaseState,
    showPaywall: () -> Unit
) {
    Column(
        modifier = Modifier.defaultPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Purchases", style = MaterialTheme.typography.titleLarge)

        when (extendedPurchaseState) {
            PurchasesRepository.ExtendedPurchaseState.BASIC -> {
                Text(stringResource(Res.string.want_new_features))
                Button(onClick = showPaywall) {
                    Text(stringResource(Res.string.extend_youkon))
                }
            }
            PurchasesRepository.ExtendedPurchaseState.EXTENDED -> {
                Text(stringResource(Res.string.have_extended))
            }
            PurchasesRepository.ExtendedPurchaseState.ERROR -> {
                Text(stringResource(Res.string.purchase_error_generic))
            }
        }
    }

}
