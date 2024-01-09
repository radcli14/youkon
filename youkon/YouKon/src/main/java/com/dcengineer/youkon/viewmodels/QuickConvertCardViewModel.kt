package com.dcengineer.youkon.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dcengineer.youkon.YkMeasurement
import com.dcengineer.youkon.YkUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/// The set of all views contained in the quick convert card, for selection of one that is highlighted
enum class QuickConvertViews {
    SURFACE, FROM, TO, VALUE, CONVERTED
}

class QuickConvertCardViewModel(
    initialMeasurement: YkMeasurement = YkMeasurement(2.26, YkUnit.METERS)
): ViewModel() {
    val measurement = MutableStateFlow(initialMeasurement)

    val unit: YkUnit get() = measurement.value.unit
    val value: Double get() = measurement.value.value
    val allUnits get() = unit.allUnits
    var equivalentUnits by mutableStateOf(unit.equivalentUnits())
    var targetUnit by mutableStateOf(unit.newTargetUnit)

    var convertedText by mutableStateOf(measurement.value.unitAndConversion(targetUnit))

    private val tag = "QuickConvertCardViewModel"

    /// When the user modifies the value in the `MeasurementTextField` update the `value`
    fun updateValue(newValue: Double) {
        Log.d(tag, "value updated from $value to $newValue")
        measurement.update { currentMeasurement ->
            currentMeasurement.copy(value = newValue)
        }
        updateConvertedText()
    }

    /// When the user modifies the `From` dropdown, update the `measurement.unit`
    fun updateUnit(newUnit: YkUnit?) {
        newUnit?.let {
            Log.d(tag, "unit updated from $unit to $it")
            measurement.update { currentMeasurement ->
                currentMeasurement.copy(unit = it)
            }
            equivalentUnits = it.equivalentUnits()
            targetUnit = it.getNewTargetUnit(targetUnit)
            updateConvertedText()
        }
    }

    /// When the user modifies the `To` dropdown, update the `targetUnit`
    fun updateTargetUnit(newUnit: YkUnit?) {
        newUnit?.let {
            Log.d(tag, "targetUnit updated from $targetUnit to $it")
            targetUnit = it
            updateConvertedText()
        }
    }

    /// When a new value is received, update the text at the bottom of the card
    private fun updateConvertedText() {
        val newText = measurement.value.unitAndConversion(targetUnit)
        Log.d(tag, "convertedText updated from $convertedText to $newText")
        convertedText = newText
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
