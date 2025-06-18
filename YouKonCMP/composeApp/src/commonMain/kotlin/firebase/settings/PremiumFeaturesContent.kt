package firebase.settings

import Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.revenuecat.purchases.kmp.LogLevel
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.configure
import com.revenuecat.purchases.kmp.models.Offerings
import com.revenuecat.purchases.kmp.models.PurchasesError
import com.revenuecat.purchases.kmp.ui.revenuecatui.Paywall
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallOptions
import defaultPadding
import getRevenueCatApiKey
import org.jetbrains.compose.resources.stringResource
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.extend_youkon
import youkon.composeapp.generated.resources.purchase_error_generic
import youkon.composeapp.generated.resources.want_new_features


@Composable
fun PremiumFeaturesContent() {
    Purchases.logLevel = LogLevel.DEBUG
    Purchases.configure(apiKey = getRevenueCatApiKey())

    val tag = "PremiumFeaturesContent"

    var paywallIsShown by remember { mutableStateOf(false) }

    val options = remember {
        PaywallOptions(dismissRequest = { paywallIsShown = false }) {
            shouldDisplayDismissButton = true
        }
    }

    // Determine if there are purchases available
    var error: PurchasesError? = null
    var offerings: Offerings? = null
    Purchases.sharedInstance.getOfferings(
        onError = {
            error = it
            error?.underlyingErrorMessage?.let { message -> Log.d(tag, message) }
        },
        onSuccess = {
            offerings = it
            Log.d(tag, it.all.toString())
        }
    )

    Surface(
        modifier = Modifier.defaultPadding(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.defaultPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(Res.string.want_new_features))

            Button(
                onClick = {
                paywallIsShown = true

                /*Purchases.sharedInstance.purchase(
                    packageToPurchase = Package(),
                    onError = { error, userCancelled ->
                        // No purchase
                    },
                    onSuccess = { storeTransaction, customerInfo ->
                        if (customerInfo.entitlements["my_entitlement_identifier"]?.isActive == true) {
                            // Unlock that great "pro" content
                        }
                    }
                )*/

                },
                enabled = error == null
            ) {
                Text(stringResource(Res.string.extend_youkon))
            }

            if (paywallIsShown) {
                if (error != null) {
                    //error?.underlyingErrorMessage?.let { Text(it) }
                    Text(stringResource(Res.string.purchase_error_generic))
                } else if (offerings != null) {
                    Paywall(options)
                }
            }
        }
    }
}
