package purchases

import Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class PurchasesViewModel: ViewModel() {
    val repository: PurchasesRepository = purchasesRepository

    private val tag = "PurchasesViewModel"

    private val _shouldShowPaywall = MutableStateFlow(false)
    var shouldShowPaywall: StateFlow<Boolean> = _shouldShowPaywall.asStateFlow()
    fun showPaywall() {
        _shouldShowPaywall.value = true
    }
    fun hidePaywall() {
        _shouldShowPaywall.value = false
    }

    val extendedPurchaseState: StateFlow<ExtendedPurchaseState> = repository.extendedPurchaseState
    val isExtended: StateFlow<Boolean> = repository.isExtended

    fun onPurchaseCompleted() {
        repository.onPurchaseCompleted()
    }
}
