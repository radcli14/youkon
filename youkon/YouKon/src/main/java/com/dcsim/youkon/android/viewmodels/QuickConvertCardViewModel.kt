package com.dcsim.youkon.android.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dcsim.youkon.YkMeasurement
import com.dcsim.youkon.YkUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


class QuickConvertCardViewModel(
    initialMeasurement: YkMeasurement = YkMeasurement(2.26, YkUnit.METERS)
): ViewModel() {
    val measurement = MutableStateFlow(initialMeasurement)

    val unit: YkUnit get() = measurement.value.unit
    private val value: Double get() = measurement.value.value
    val allUnits get() = unit.allUnits
    var equivalentUnits by mutableStateOf(measurement.value.unit.equivalentUnits())
    var targetUnit by mutableStateOf(newTargetUnit)

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
            if (targetUnit == it || targetUnit !in equivalentUnits) {
                targetUnit = newTargetUnit
            }
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

    /// When the user modifies the `From` dropdown, this provides the first option for a target unit that can be converted from the `measurement.unit` but is not the same unit
    private val newTargetUnit: YkUnit get() = equivalentUnits.first { it != unit }
}
