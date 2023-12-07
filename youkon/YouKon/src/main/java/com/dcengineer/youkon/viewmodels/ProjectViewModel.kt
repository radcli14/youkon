package com.dcengineer.youkon.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.dcengineer.youkon.ProjectExpansionLevel
import com.dcengineer.youkon.YkMeasurement
import com.dcengineer.youkon.YkProject
import com.dcengineer.youkon.YkSystem
import kotlinx.coroutines.flow.MutableStateFlow

class ProjectViewModel(initialProject: YkProject = YkProject()): ViewModel() {
    val project = MutableStateFlow(initialProject)
    val editedName = mutableStateOf(initialProject.name)
    val editedDescription = mutableStateOf(initialProject.about)
    val convertToSystem = mutableStateOf(YkSystem.SI)
    var measurements: MutableState<Array<YkMeasurement>> = mutableStateOf(
        initialProject.measurements.toTypedArray()
    )
    val expansion = mutableStateOf(ProjectExpansionLevel.COMPACT)
    val isExpanded = mutableStateOf(false)
    val canSubtract = mutableStateOf(false)
    val showSubtractAlert = mutableStateOf(false)
    val measurementToDelete: MutableState<YkMeasurement?> = mutableStateOf(null)

    val imageSize: Dp
        get() = if (expansion.value == ProjectExpansionLevel.EDITABLE) 48.dp else 36.dp

    val divTopPadding
        get() = if (expansion.value == ProjectExpansionLevel.STATIC) 0.dp else 16.dp

    /// Update the public list of `YkProject` items by assuring that the Kotlin version is Swift formatted
    private fun updateMeasurements() {
        measurements.value = project.value.measurements.toTypedArray()
    }

    fun refresh(viewModel: ProjectViewModel) {
        project.value = viewModel.project.value
        editedName.value = project.value.name
        editedDescription.value = project.value.about
        updateMeasurements()
    }

    fun updateName(name: String) {
        project.value.name = name
        editedName.value = name
    }

    fun updateDescription(description: String) {
        project.value.about = description
        editedDescription.value = description
    }

    fun toggleExpansion() {
        expansion.value = when(expansion.value) {
            ProjectExpansionLevel.COMPACT ->  ProjectExpansionLevel.STATIC
            ProjectExpansionLevel.STATIC -> ProjectExpansionLevel.COMPACT
            ProjectExpansionLevel.EDITABLE -> ProjectExpansionLevel.STATIC
            else -> ProjectExpansionLevel.COMPACT
        }
    }

    fun toggleEdit() {
        expansion.value = when(expansion.value) {
            ProjectExpansionLevel.EDITABLE -> ProjectExpansionLevel.STATIC
            else -> ProjectExpansionLevel.EDITABLE
        }
    }

    fun toggleSystem() {
        convertToSystem.value = when(convertToSystem.value) {
            YkSystem.SI -> YkSystem.IMPERIAL
            YkSystem.IMPERIAL -> YkSystem.SI
            else -> YkSystem.SI
        }
    }

    fun toggleSystem(toSystem: YkSystem) {
        convertToSystem.value = toSystem
    }

    fun addMeasurement() {
        /*project.update { currentProject ->
            currentProject.addMeasurement()
            project.value = currentProject.copy()
            return
        }*/
        project.value.addMeasurement()
        /*
            value = 0.0,
            unit = YkUnit.METERS,
            name = "",
            about = ""
        )*/
        updateMeasurements()
    }

    fun subtractMeasurement() {
        canSubtract.value = !canSubtract.value
    }

    fun subtract(measurement: YkMeasurement) {
        measurementToDelete.value = measurement
        showSubtractAlert.value = true
    }

    fun confirmDelete() {
        measurementToDelete.value?.let {
            project.value.removeMeasurement(measurement = it)
            updateMeasurements()
        }
        cleanupDelete()
    }

    fun cancelDelete() {
        cleanupDelete()
    }

    /// Reset the variables associated with showing an alert and subtracting a project to their defaults
    private fun cleanupDelete() {
        showSubtractAlert.value = false
        measurementToDelete.value = null
        canSubtract.value = false
    }
}