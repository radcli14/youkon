package viewmodel

import Log
import Storage
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.TextLayoutResult
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import model.YkUnit
import kotlin.math.absoluteValue

/// The set of all views contained in the quick convert card, for selection of one that is highlighted
enum class QuickConvertViews {
    SURFACE, FROM, TO, VALUE, CONVERTED
}

class QuickConvertCardViewModel(private val storage: Storage? = null) : ViewModel() {
    val data = MutableStateFlow(storage?.savedQuickData ?: Storage().savedQuickData)

    val unit: YkUnit get() = data.value.unit
    val value: Double get() = data.value.value
    fun allUnits(isExtended: Boolean = false): Array<YkUnit> {
        return if (isExtended) unit.allUnits else unit.basicUnits
    }
    fun equivalentUnits(isExtended: Boolean = false): Array<YkUnit> {
        return when (isExtended) {
            true -> unit.equivalentUnits()
            false -> unit.equivalentUnits().filter { unit.basicUnits.contains(it) }.toTypedArray()
        }
    }
    private val targetUnit get() = data.value.targetUnit

    var convertedTextFitsOnOneLine = MutableStateFlow(true)

    private val tag = "QuickConvertCardViewModel"

    /// When the user modifies the value in the `MeasurementTextField` update the `value`
    fun updateValue(newValue: Double) {
        // Check that the new value actually is different, otherwise take no action
        if (newValue != value) {
            Log.d(tag, "value updated from $value to $newValue")
            convertedTextFitsOnOneLine.value = true
            data.update { currentData ->
                currentData.copy(value = newValue)
            }
            storage?.saveQuickDataToJson(data.value)
        }
    }

    /// When the user modifies the `From` dropdown, update the `measurement.unit`
    fun updateUnit(newUnit: YkUnit?) {
        newUnit?.let { newUnit ->
            Log.d(tag, "unit updated from $unit to $newUnit")
            convertedTextFitsOnOneLine.value = true
            data.update { currentData ->
                val newTargetUnit = newUnit.getNewTargetUnit(targetUnit)
                currentData.copy(unit = newUnit, targetUnit = newTargetUnit)
            }
            storage?.saveQuickDataToJson(data.value)
        }
    }

    /// When the user modifies the `To` dropdown, update the `targetUnit`
    fun updateTargetUnit(newTargetUnit: YkUnit?) {
        newTargetUnit?.let {newTargetUnit ->
            Log.d(tag, "targetUnit updated from $targetUnit to $newTargetUnit")
            convertedTextFitsOnOneLine.value = true
            data.update { currentData ->
                currentData.copy(targetUnit = newTargetUnit)
            }
            storage?.saveQuickDataToJson(data.value)
        }
    }

    /// When new text is received in the `ConvertedText`, check whether it is multiple lines, if it is, label the converted text as large
    fun handleConvertedTextLayout(result: TextLayoutResult) {
        if (result.lineCount > 1) {
            convertedTextFitsOnOneLine.value = false
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
