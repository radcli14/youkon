package viewmodel

import Log
import Storage
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import model.YkUnit

/// The set of all views contained in the quick convert card, for selection of one that is highlighted
enum class QuickConvertViews {
    SURFACE, FROM, TO, VALUE, CONVERTED
}

class QuickConvertCardViewModel : ViewModel() {
    val data = MutableStateFlow(Storage.savedQuickData)

    val unit: YkUnit get() = data.value.unit
    val value: Double get() = data.value.value
    val allUnits get() = unit.allUnits
    private val targetUnit get() = data.value.targetUnit

    private val tag = "QuickConvertCardViewModel"

    /// When the user modifies the value in the `MeasurementTextField` update the `value`
    fun updateValue(newValue: Double) {
        Log.d(tag, "value updated from $value to $newValue")
        data.update { currentData ->
            currentData.copy(value = newValue)
        }
        Storage.saveQuickDataToJson(data.value)
    }

    /// When the user modifies the `From` dropdown, update the `measurement.unit`
    fun updateUnit(newUnit: YkUnit?) {
        newUnit?.let { newUnit ->
            Log.d(tag, "unit updated from $unit to $newUnit")
            data.update { currentData ->
                val newTargetUnit = newUnit.getNewTargetUnit(targetUnit)
                currentData.copy(unit = newUnit, targetUnit = newTargetUnit)
            }
            Storage.saveQuickDataToJson(data.value)
        }
    }

    /// When the user modifies the `To` dropdown, update the `targetUnit`
    fun updateTargetUnit(newTargetUnit: YkUnit?) {
        newTargetUnit?.let {newTargetUnit ->
            Log.d(tag, "targetUnit updated from $targetUnit to $newTargetUnit")
            data.update { currentData ->
                currentData.copy(targetUnit = newTargetUnit)
            }
            Storage.saveQuickDataToJson(data.value)
        }
    }

    /// When viewing the onboard screen, this modifies which view is highlighted
    var highlightedView: MutableState<QuickConvertViews?> = mutableStateOf(null)
    fun highlight(view: QuickConvertViews?) {
        highlightedView.value = view
    }
    fun highlight(viewInt: Int?) {
        when (viewInt) {
            0 -> highlight(QuickConvertViews.SURFACE)
            1 -> highlight(QuickConvertViews.FROM)
            2 -> highlight(QuickConvertViews.TO)
            3 -> highlight(QuickConvertViews.VALUE)
            4 -> highlight(QuickConvertViews.CONVERTED)
            else -> highlight(view = null)
        }
    }
}
