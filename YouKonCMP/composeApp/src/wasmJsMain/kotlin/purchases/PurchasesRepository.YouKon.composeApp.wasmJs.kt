package purchases

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private class WasmPurchasesRepository : PurchasesRepository {
    override val extendedPurchaseState: StateFlow<ExtendedPurchaseState> = MutableStateFlow(ExtendedPurchaseState.BASIC)
    override val isExtended: StateFlow<Boolean> = MutableStateFlow(false)
    override fun onPurchaseCompleted() { /* no-op */ }
}

actual val purchasesRepository: PurchasesRepository = WasmPurchasesRepository()