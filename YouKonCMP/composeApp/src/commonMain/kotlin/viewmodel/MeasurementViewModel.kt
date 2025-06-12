package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.ViewModel
import model.YkMeasurement
import model.YkUnit

class MeasurementViewModel(
    private val measurement: YkMeasurement,
    private val onMeasurementUpdated: (YkMeasurement) -> Unit,
    val isFocused: MutableState<Boolean>
) : ViewModel() {
    var measurementName = mutableStateOf(measurement.name)
    var measurementDescription = mutableStateOf(measurement.about)
    var value = mutableStateOf(measurement.value)
    var unit = mutableStateOf(measurement.unit)

    private val tag = "MeasurementViewModel"

    fun updateName(name: String) {
        //Log.d(tag, "Updating name from ${measurement.name} to $name")
        measurement.name = name
        measurementName.value = name
        onMeasurementUpdated(measurement)
    }

    fun updateDescription(description: String) {
        //Log.d(tag, "Updating description from ${measurement.about} to $description")
        measurement.about = description
        measurementDescription.value = description
        onMeasurementUpdated(measurement)
    }

    fun updateValue(newValue: Double) {
        //Log.d(tag, "Updating value from ${measurement.value} to $newValue")
        measurement.value = newValue
        value.value = newValue
        onMeasurementUpdated(measurement)
    }

    fun updateUnit(newUnit: YkUnit) {
        //Log.d(tag, "Updating unit from ${measurement.unit} to $newUnit")
        measurement.unit = newUnit
        unit.value = newUnit
        onMeasurementUpdated(measurement)
    }

    fun handleFocusStateChange(focusState: FocusState) {
        //Log.d(tag, "Focus state changed for value field: ${focusState.hasFocus}")
        isFocused.value = focusState.hasFocus
    }

    fun switchSign() {
        //Log.d(tag, "Switching sign of ${measurement.value}")
        updateValue(measurement.value * -1)
    }

    fun multiplyByTen() {
        //Log.d(tag, "Multiplying ${measurement.value} by ten")
        updateValue(measurement.value * 10)
    }

    fun divideByTen() {
        //Log.d(tag, "Dividing ${measurement.value} by ten")
        updateValue(measurement.value / 10)
    }

    fun clearValue() {
        //Log.d(tag, "Clearing value from ${measurement.value} to 0.0")
        updateValue(0.0)
    }
}
