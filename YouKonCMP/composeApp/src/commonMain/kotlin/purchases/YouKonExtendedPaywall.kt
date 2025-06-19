package purchases

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.revenuecat.purchases.kmp.ui.revenuecatui.Paywall
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallOptions

@Composable
fun YouKonExtendedPaywall() {
    val options = PaywallOptions(
        dismissRequest = PurchasesRepository.sharedInstance::hidePaywall
    )
    Paywall(options)
}
