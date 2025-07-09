package purchases

import kotlinx.coroutines.flow.StateFlow

interface PurchasesRepository {
    val extendedPurchaseState: StateFlow<ExtendedPurchaseState>
    val isExtended: StateFlow<Boolean>
    fun onPurchaseCompleted() // Platform-independent, no parameters
}

// Platform-independent purchase state
enum class ExtendedPurchaseState {
    EXTENDED, BASIC, ERROR
}

expect val purchasesRepository: PurchasesRepository
