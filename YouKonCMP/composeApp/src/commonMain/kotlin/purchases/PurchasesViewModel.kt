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
    val repository = PurchasesRepository.sharedInstance

    private val tag = "PurchasesViewModel"

    private val _shouldShowPaywall = MutableStateFlow(false)
    var shouldShowPaywall: StateFlow<Boolean> = _shouldShowPaywall.asStateFlow()
    fun showPaywall() {
        _shouldShowPaywall.value = true
    }
    fun hidePaywall() {
        _shouldShowPaywall.value = false
    }

    private val _extendedPurchaseState = MutableStateFlow(repository.extendedPurchaseState)
    val extendedPurchaseState: StateFlow<PurchasesRepository.ExtendedPurchaseState> = _extendedPurchaseState.asStateFlow()

    val isExtended: StateFlow<Boolean> = extendedPurchaseState.map { it == PurchasesRepository.ExtendedPurchaseState.EXTENDED }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            extendedPurchaseState.value == PurchasesRepository.ExtendedPurchaseState.EXTENDED
        )

    init {
        viewModelScope.launch {
            repository.customer.collect {
                Log.d(tag, "CustomerInfo changed: $it ${it?.hasExtendedPurchase == true}")
                _extendedPurchaseState.value = repository.extendedPurchaseState
            }
        }
    }
}
