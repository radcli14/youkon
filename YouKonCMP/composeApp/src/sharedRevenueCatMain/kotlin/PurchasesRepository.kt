import com.revenuecat.purchases.kmp.LogLevel
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.configure
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.Offerings
import com.revenuecat.purchases.kmp.models.PurchasesError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PurchasesRepository {

    init {
        Purchases.logLevel = LogLevel.DEBUG
        Purchases.configure(apiKey = getRevenueCatApiKey())
    }

    private val tag = "PurchasesRepository"

    private val _error = MutableStateFlow<PurchasesError?>(null)
    var error: StateFlow<PurchasesError?> = _error.asStateFlow()
    val errorMessage: String get() {
        error.value?.underlyingErrorMessage?.let {
            return "Error: $it"
        }
        return "No Error"
    }

    private val _offerings = MutableStateFlow<Offerings?>(null)
    var offerings: StateFlow<Offerings?> = _offerings.asStateFlow()

    private val _customer = MutableStateFlow<CustomerInfo?>(null)
    val customer: StateFlow<CustomerInfo?> = _customer.asStateFlow()

    /// New method to start fetching data
    fun startFetchingData() {
        getOfferings()
        getCustomer()
    }

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
    fun updateCustomerInfo(info: CustomerInfo) {
        _customer.value = info
        Log.d(tag, "Customer: Extended=${info.hasExtendedPurchase}")
    }

    enum class ExtendedPurchaseState {
        EXTENDED, BASIC, ERROR
    }

    val extendedPurchaseState: ExtendedPurchaseState
        get() {
        return when {
            customer.value?.hasExtendedPurchase == true -> ExtendedPurchaseState.EXTENDED
            error.value != null -> ExtendedPurchaseState.ERROR
            else -> ExtendedPurchaseState.BASIC
        }
    }

    companion object {
        val sharedInstance: PurchasesRepository by lazy {
            val instance = PurchasesRepository()
            instance.startFetchingData() // Call after instance is fully constructed
            instance
        }
    }
}

val CustomerInfo.hasExtendedPurchase: Boolean get() {
    return entitlements.get("Extended")?.isActive == true
}
