package purchases

import Log
import androidx.compose.runtime.Composable
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.StoreTransaction
import com.revenuecat.purchases.kmp.ui.revenuecatui.Paywall
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallListener
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallOptions

@Composable
actual fun YouKonExtendedPaywall(
    dismissRequest: () -> Unit,
    onPurchaseCompleted: () -> Unit
) {
    // Create the listener which will update the customer entitlements on successful purchase
    val paywallListener = object : PaywallListener {
        override fun onPurchaseCompleted(customerInfo: CustomerInfo, storeTransaction: StoreTransaction) {
            PurchasesRepositoryImpl.sharedInstance.updateCustomerInfo(customerInfo)
            onPurchaseCompleted()
            Log.d( "YouKonPaywallListener","Purchase completed: ${storeTransaction.productIds}")
        }
    }

    // Use the builder to create the options which include the listener
    val builder = PaywallOptions.Builder(dismissRequest)
    builder.listener = paywallListener
    val options = builder.build()

    // Display the paywall composable
    Paywall(options)
} 