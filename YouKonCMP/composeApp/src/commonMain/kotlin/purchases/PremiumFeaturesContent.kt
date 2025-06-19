package purchases

import Log
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
import com.revenuecat.purchases.kmp.LogLevel
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.configure
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.Offerings
import com.revenuecat.purchases.kmp.models.PurchasesError
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallOptions
import defaultPadding
import getRevenueCatApiKey
import org.jetbrains.compose.resources.stringResource
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.extend_youkon
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

    Surface(
        modifier = Modifier.defaultPadding(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.defaultPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Purchases", style = MaterialTheme.typography.titleLarge)

            if (customer?.hasExtendedPurchase == true) {
                Text("✅ You've got extended!")
            } else if (error != null) {
                //error?.underlyingErrorMessage?.let { Text(it) }
                Text(stringResource(Res.string.purchase_error_generic))
            } else {
                Text(stringResource(Res.string.want_new_features))
                Button(
                    onClick = { openScreen(PAYWALL_SCREEN) }
                ) {
                    Text(stringResource(Res.string.extend_youkon))
                }
            }
        }
    }
}
