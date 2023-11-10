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

class MeasurementViewModel(initialMeasurement: YkMeasurement): ViewModel() {
    val measurement = MutableStateFlow(initialMeasurement)
    var equivalentUnits by mutableStateOf(initialMeasurement.unit.equivalentUnits())

    private val tag = "MeasurementViewModel"

    private val measurementName get() = measurement.value.name
    private val measurementDescription get() = measurement.value.name
    private val value get() = measurement.value.value
    private val unit get() = measurement.value.unit

    fun updateName(newName: String) {
        Log.d(tag, "value updated from $measurementName to $newName")
        measurement.update { currentMeasurement ->
            currentMeasurement.copy(name = newName)
        }
    }

    fun updateDescription(newDescription: String) {
        Log.d(tag, "value updated from $measurementDescription to $newDescription")
        measurement.update { currentMeasurement ->
            currentMeasurement.copy(about = newDescription)
        }
    }

    /// When the user modifies the value in the `MeasurementTextField` update the `value`
    fun updateValue(newValue: Double) {
        Log.d(tag, "value updated from $value to $newValue")
        measurement.update { currentMeasurement ->
            currentMeasurement.copy(value = newValue)
        }
    }

    /// When the user modifies the `From` dropdown, update the `measurement.unit`
    fun updateUnit(newUnit: YkUnit?) {
        newUnit?.let {
            Log.d(tag, "unit updated from $unit to $it")
            measurement.update { currentMeasurement ->
                currentMeasurement.copy(unit = it)
            }
            equivalentUnits = it.equivalentUnits()
        }
    }
}