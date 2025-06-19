package purchases

import PAYWALL_SCREEN
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallOptions
import defaultPadding
import org.jetbrains.compose.resources.stringResource
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.extend_youkon
import youkon.composeapp.generated.resources.have_extended
import youkon.composeapp.generated.resources.purchase_error_generic
import youkon.composeapp.generated.resources.want_new_features


@Composable
fun PremiumFeaturesContent(openScreen: (String) -> Unit) {
    val repository = PurchasesRepository.sharedInstance
    val offerings by repository.offerings.collectAsState()
    val customer by repository.customer.collectAsState()
    val error by repository.error.collectAsState()

    val tag = "PremiumFeaturesContent"

    var paywallIsShown by remember { mutableStateOf(false) }

    val options = remember {
        PaywallOptions(dismissRequest = { paywallIsShown = false }) {
            shouldDisplayDismissButton = true
        }
    }

    Column(
        modifier = Modifier.defaultPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Purchases", style = MaterialTheme.typography.titleLarge)

        when (repository.extendedPurchaseState) {
            PurchasesRepository.ExtendedPurchaseState.BASIC -> {
                Text(stringResource(Res.string.want_new_features))
                Button(onClick = repository::showPaywall) {
                    Text(stringResource(Res.string.extend_youkon))
                }
            }
            PurchasesRepository.ExtendedPurchaseState.EXTENDED -> {
                Text(stringResource(Res.string.have_extended))
            }
            PurchasesRepository.ExtendedPurchaseState.ERROR -> {
                Text(repository.errorMessage)
                Text(stringResource(Res.string.purchase_error_generic))
            }
        }
    }

}
