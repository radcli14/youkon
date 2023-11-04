package com.dcsim.youkon.android.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dcsim.youkon.YkMeasurement
import com.dcsim.youkon.YkUnit


class QuickConvertCardViewModel(
    initialMeasurement: YkMeasurement = YkMeasurement(2.26, YkUnit.METERS)
): ViewModel() {
    var measurement by mutableStateOf(initialMeasurement)
    var equivalentUnits by mutableStateOf(measurement.unit.equivalentUnits())
    var unit by mutableStateOf(measurement.unit)
    var value by mutableDoubleStateOf(measurement.value)
    var targetUnit by mutableStateOf(newTargetUnit)
    var convertedText by mutableStateOf(measurement.unitAndConversion(targetUnit))

    private val tag = "QuickConvertCardViewModel"

    /// When the user modifies the value in the `MeasurementTextField` update the `value`
    fun updateValue(newValue: Double) {
        Log.d(tag, "value updated from $value to $newValue")
        measurement.value = newValue
        value = measurement.value
        updateConvertedText()
    }

    /// When the user modifies the `From` dropdown, update the `measurement.unit`
    fun updateUnit(newUnit: YkUnit?) {
        newUnit?.let {
            Log.d(tag, "unit updated from $unit to $it")
            measurement.unit = it
            unit = measurement.unit
            equivalentUnits = measurement.unit.equivalentUnits()
            if (targetUnit == it || it !in equivalentUnits) {
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
        val newText = measurement.unitAndConversion(targetUnit)
        Log.d(tag, "convertedText updated from $convertedText to $newText")
        convertedText = newText
    }

    /// When the user modifies the `From` dropdown, this provides the first option for a target unit that can be converted from the `measurement.unit` but is not the same unit
    private val newTargetUnit: YkUnit get() = equivalentUnits.first { it != measurement.unit }
}
