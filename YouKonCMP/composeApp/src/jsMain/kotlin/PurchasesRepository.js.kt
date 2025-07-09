package purchases

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PurchasesRepositoryImpl : PurchasesRepository {
    private val _extendedPurchaseState = MutableStateFlow(ExtendedPurchaseState.BASIC)
    override val extendedPurchaseState: StateFlow<ExtendedPurchaseState> = _extendedPurchaseState
    override val isExtended: StateFlow<Boolean> = MutableStateFlow(false)
    override fun onPurchaseCompleted() { /* no-op */ }
    companion object {
        val sharedInstance: PurchasesRepositoryImpl by lazy { PurchasesRepositoryImpl() }
    }
}

actual val purchasesRepository: PurchasesRepository = PurchasesRepositoryImpl.sharedInstance 