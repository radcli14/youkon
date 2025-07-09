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

class PurchasesRepositoryImpl : PurchasesRepository {
    init {
        Purchases.logLevel = LogLevel.DEBUG
        Purchases.configure(apiKey = getRevenueCatApiKey())
    }

    private val tag = "PurchasesRepository"

    private val _error = MutableStateFlow<PurchasesError?>(null)
    private val _offerings = MutableStateFlow<Offerings?>(null)
    private val _customer = MutableStateFlow<CustomerInfo?>(null)

    private val _extendedPurchaseState = MutableStateFlow(ExtendedPurchaseState.BASIC)
    override val extendedPurchaseState: StateFlow<ExtendedPurchaseState> = _extendedPurchaseState.asStateFlow()
    override val isExtended: StateFlow<Boolean> = MutableStateFlow(false) // You can implement this as needed

    private var lastCustomerInfo: CustomerInfo? = null

    init {
        startFetchingData()
    }

    private fun startFetchingData() {
        getOfferings()
        getCustomer()
    }

    private fun updateError(newError: PurchasesError) {
        _error.value = newError
        _extendedPurchaseState.value = ExtendedPurchaseState.ERROR
        newError.underlyingErrorMessage?.let { message -> Log.e(tag, message) }
    }

    private fun getOfferings() {
        Purchases.sharedInstance.getOfferings(
            onError = { updateError(it) },
            onSuccess = { _offerings.value = it; Log.d(tag, "Offerings: ${it.all}") }
        )
    }

    private fun getCustomer() {
        Purchases.sharedInstance.getCustomerInfo(
            onError = { updateError(it) },
            onSuccess = { updateCustomerInfo(it) }
        )
    }

    // Platform-specific: call this from the paywall callback
    fun updateCustomerInfo(info: CustomerInfo) {
        lastCustomerInfo = info
        _customer.value = info
        _extendedPurchaseState.value = if (info.hasExtendedPurchase) ExtendedPurchaseState.EXTENDED else ExtendedPurchaseState.BASIC
        Log.d(tag, "Customer: Extended=${info.hasExtendedPurchase}")
    }

    override fun onPurchaseCompleted() {
        lastCustomerInfo?.let { updateCustomerInfo(it) }
    }

    companion object {
        val sharedInstance: PurchasesRepositoryImpl by lazy { PurchasesRepositoryImpl() }
    }
}

val CustomerInfo.hasExtendedPurchase: Boolean get() {
    return entitlements.get("Extended")?.isActive == true
}

actual val purchasesRepository: PurchasesRepository = PurchasesRepositoryImpl.sharedInstance
