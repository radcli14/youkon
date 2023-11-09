package com.dcsim.youkon.android.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dcsim.youkon.ProjectExpansionLevel
import com.dcsim.youkon.YkMeasurement
import com.dcsim.youkon.YkProject
import com.dcsim.youkon.YkSystem
import com.dcsim.youkon.YkUnit

class ProjectViewModel(var project: YkProject = YkProject()): ViewModel() {
    val editedName = mutableStateOf(project.name)
    val editedDescription = mutableStateOf(project.about)
    val convertToSystem = mutableStateOf(YkSystem.SI)
    var measurements: MutableState<Array<YkMeasurement>> = mutableStateOf(
        project.measurements.toTypedArray()
    )
    val expansion = mutableStateOf(ProjectExpansionLevel.COMPACT)
    val isExpanded = mutableStateOf(false)
    val canSubtract = mutableStateOf(false)
    val showSubtractAlert = mutableStateOf(false)
    val measurementToDelete: MutableState<YkMeasurement?> = mutableStateOf(null)

    /// Update the public list of `YkProject` items by assuring that the Kotlin version is Swift formatted
    private fun updateMeasurements() {
        measurements.value = project.measurements.toTypedArray()
    }

    fun refresh(viewModel: ProjectViewModel) {
        project = viewModel.project
        editedName.value = project.name
        editedDescription.value = project.about
        updateMeasurements()
    }

    /*
    var expansionIcon: String {
        switch (expansion) {
            case .compact: return "chevron.down"
            default: return "chevron.up"
        }
    }
     */

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

    fun addMeasurement() {
        project.addMeasurement(
            value = 0.0,
            unit = YkUnit.METERS,
            name = "",
            about = ""
        )
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
            project.removeMeasurement(measurement = it)
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