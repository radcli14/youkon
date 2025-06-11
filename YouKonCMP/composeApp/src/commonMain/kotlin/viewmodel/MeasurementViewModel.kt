package viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.ViewModel
import model.YkMeasurement
import model.YkUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.absoluteValue

class MeasurementViewModel(initialMeasurement: YkMeasurement) : ViewModel() {
    val measurement = MutableStateFlow(initialMeasurement)

    private val tag = "MeasurementViewModel"

    var measurementName by mutableStateOf(initialMeasurement.name)
    var measurementDescription by mutableStateOf(initialMeasurement.about)
    var value by mutableDoubleStateOf(initialMeasurement.value)
    var unit by mutableStateOf(initialMeasurement.unit)

    var isFocused = MutableStateFlow(false)

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

    fun handleFocusStateChange(newState: FocusState) {
        Log.d(tag, "handleFocusStateChange($newState)")
        isFocused.value = newState.isFocused
    }

    fun switchSign() {
        if (value.absoluteValue > 0) {
            updateValue(-value)
        }
    }

    fun multiplyByTen() {
        updateValue(value * 10)
    }

    fun divideByTen() {
        updateValue(value * 0.1)
    }

    fun clearValue() {
        updateValue(0.0)
    }
}
