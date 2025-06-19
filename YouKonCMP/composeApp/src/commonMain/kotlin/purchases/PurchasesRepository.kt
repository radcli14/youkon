package purchases

import Log
import com.revenuecat.purchases.kmp.LogLevel
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.configure
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.Offerings
import com.revenuecat.purchases.kmp.models.PurchasesError
import getRevenueCatApiKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PurchasesRepository {

    init {
        Purchases.logLevel = LogLevel.DEBUG
        Purchases.configure(apiKey = getRevenueCatApiKey())
        getOfferings()
        getCustomer()
    }

    private val tag = "PurchasesRepository"

    private val _error = MutableStateFlow<PurchasesError?>(null)
    var error: StateFlow<PurchasesError?> = _error.asStateFlow()

    private val _offerings = MutableStateFlow<Offerings?>(null)
    var offerings: StateFlow<Offerings?> = _offerings.asStateFlow()

    private val _customer = MutableStateFlow<CustomerInfo?>(null)
    val customer: StateFlow<CustomerInfo?> = _customer.asStateFlow()

    /// When an error is received, add it to the StateFlow, and log it at the error level
    private fun updateError(newError: PurchasesError) {
        _error.value = newError
        error.value?.underlyingErrorMessage?.let { message -> Log.e(tag, message) }
    }

    fun getOfferings() {
        Purchases.sharedInstance.getOfferings(
            onError = { updateError(it) },
            onSuccess = { updateOfferings(it) }
        )
    }

    private fun updateOfferings(newOfferings: Offerings) {
        _offerings.value = newOfferings
        Log.d(tag, "Offerings: ${newOfferings.all}")
    }

    fun getCustomer() {
        Purchases.sharedInstance.getCustomerInfo(
            onError = { updateError(it) },
            onSuccess = { updateCustomerInfo(it) }
        )
    }

    // Call this when customer info updates from RevenueCat SDK callbacks
    private fun updateCustomerInfo(info: CustomerInfo) {
        _customer.value = info
        Log.d(tag, "Customer: Extended=${info.hasExtendedPurchase}")
    }

    companion object {
        val sharedInstance: PurchasesRepository by lazy {
            PurchasesRepository()
        }
    }
}

val CustomerInfo.hasExtendedPurchase: Boolean get() {
    return entitlements.get("Extended")?.isActive == true
}
