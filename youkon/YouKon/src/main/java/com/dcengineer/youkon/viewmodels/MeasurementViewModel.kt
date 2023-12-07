package com.dcengineer.youkon.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dcengineer.youkon.YkMeasurement
import com.dcengineer.youkon.YkUnit
import kotlinx.coroutines.flow.MutableStateFlow

class MeasurementViewModel(initialMeasurement: YkMeasurement): ViewModel() {
    val measurement = MutableStateFlow(initialMeasurement)

    private val tag = "MeasurementViewModel"

    var measurementName by mutableStateOf(initialMeasurement.name)
    var measurementDescription by mutableStateOf(initialMeasurement.about)
    var value by mutableDoubleStateOf(initialMeasurement.value)
    var unit by mutableStateOf(initialMeasurement.unit)

    fun updateName(newName: String) {
        Log.d(tag, "value updated from $measurementName to $newName")
        measurement.value.name = newName
        measurementName = newName
    }

    fun updateDescription(newDescription: String) {
        Log.d(tag, "value updated from $measurementDescription to $newDescription")
        measurement.value.about = newDescription
        measurementDescription = newDescription
    }

    /// When the user modifies the value in the `MeasurementTextField` update the `value`
    fun updateValue(newValue: Double) {
        Log.d(tag, "value updated from $value to $newValue")
        measurement.value.value = newValue
        value = newValue
    }

    /// When the user modifies the `From` dropdown, update the `measurement.unit`
    fun updateUnit(newUnit: YkUnit?) {
        newUnit?.let {
            Log.d(tag, "unit updated from $unit to $it")
            measurement.value.unit = it
            unit = it
        }
    }
}