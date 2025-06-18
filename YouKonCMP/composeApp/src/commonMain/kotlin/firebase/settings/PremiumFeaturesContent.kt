package firebase.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.revenuecat.purchases.kmp.LogLevel
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.PurchasesConfiguration
import defaultPadding
import getRevenueCatApiKey
import org.jetbrains.compose.resources.stringResource
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.extend_youkon
import youkon.composeapp.generated.resources.want_new_features

@Composable
fun PremiumFeaturesContent() {
    Purchases.logLevel = LogLevel.DEBUG
    Purchases.configure(
        configuration = PurchasesConfiguration(
            apiKey = getRevenueCatApiKey()
        )
    )

    /*val options = remember {
        PaywallOptions(dismissRequest = { TODO("Handle dismiss") }) {
            shouldDisplayDismissButton = true
        }
    }*/

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
            Button(onClick = {}) {
                Text(stringResource(Res.string.extend_youkon))
            }
        }
    }
}
