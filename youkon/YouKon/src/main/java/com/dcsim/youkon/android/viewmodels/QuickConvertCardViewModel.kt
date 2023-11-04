package com.dcsim.youkon.android.viewmodels

import androidx.compose.runtime.getValue
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
    var targetUnit by mutableStateOf(newTargetUnit)
    var convertedText by mutableStateOf(measurement.valueAndConversion(targetUnit))

    init {
        setConvertedText()
    }

    /// When a new value is received, update the text at the bottom of the card
    fun setConvertedText() {
        val short = measurement.unit.shortUnit
        val converted = measurement.convertTo(targetUnit).valueString
        convertedText = "$short  ➜  $converted"
    }

    /// When the user modifies the `From` dropdown, update the `measurement.unit`
    fun updateUnit(unit: YkUnit?) {
        if (unit != null) {
            measurement.unit = unit
            equivalentUnits = measurement.unit.equivalentUnits()
            if (targetUnit == unit || unit !in equivalentUnits) {
                targetUnit = newTargetUnit
            }
        }
        setConvertedText()
    }

    /// When the user modifies the `From` dropdown, this provides the first option for a target unit that can be converted from the `measurement.unit` but is not the same unit
    private val newTargetUnit: YkUnit get() = equivalentUnits.first { it != measurement.unit }
}
