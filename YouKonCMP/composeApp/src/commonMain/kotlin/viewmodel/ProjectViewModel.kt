package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import model.ProjectExpansionLevel
import model.YkMeasurement
import model.YkProject
import model.YkSystem
import model.YkUnit

enum class ProjectViewViews {
    COMPACT, STATIC, SYSTEM_PICKER, STATIC_MEASUREMENTS,
    EDITABLE, LABEL_STACK, PLUS_MINUS, MEASUREMENT_LABEL, MEASUREMENT_FIELDS
}

class ProjectViewModel(initialProject: YkProject = YkProject()) : ViewModel() {
    val project = MutableStateFlow(initialProject)
    val editedName = mutableStateOf(initialProject.name)
    val editedDescription = mutableStateOf(initialProject.about)
    val convertToSystem = mutableStateOf(YkSystem.SI)
    var measurements: MutableState<Array<YkMeasurement>> = mutableStateOf(
        initialProject.measurements.toTypedArray()
    )
    val expansion = mutableStateOf(ProjectExpansionLevel.COMPACT)
    val canSubtract = mutableStateOf(false)
    val showSubtractAlert = mutableStateOf(false)
    val measurementToDelete: MutableState<YkMeasurement?> = mutableStateOf(null)

    /// Update the public list of `YkProject` items by assuring that the Kotlin version is Swift formatted
    private fun updateMeasurements() {
        measurements.value = project.value.measurements.toTypedArray()
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
        }
    }

    fun toggleSystem(toSystem: YkSystem) {
        convertToSystem.value = toSystem
    }

    fun addMeasurement() {
        project.value.addMeasurement(
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

    /// When viewing the onboard screen, this modifies which view is highlighted
    var highlightedView: MutableState<ProjectViewViews?> = mutableStateOf(null)
    fun highlight(view: ProjectViewViews?) {
        highlightedView.value = view
        if (view == null) {
            expansion.value = ProjectExpansionLevel.COMPACT
        }
    }
    fun highlightInProjectView(viewInt: Int?) {
        when (viewInt) {
            0 -> highlight(ProjectViewViews.COMPACT)
            1 -> {
                highlight(ProjectViewViews.STATIC)
                expansion.value = ProjectExpansionLevel.STATIC
            }
            2 -> highlight(ProjectViewViews.SYSTEM_PICKER)
            3 -> highlight(ProjectViewViews.STATIC_MEASUREMENTS)
            else -> highlight(null)
        }
    }
    fun highlightInEditableView(viewInt: Int?) {
        when (viewInt) {
            0 -> highlight(ProjectViewViews.EDITABLE)
            1 -> highlight(ProjectViewViews.LABEL_STACK)
            2 -> highlight(ProjectViewViews.PLUS_MINUS)
            3 -> highlight(ProjectViewViews.MEASUREMENT_LABEL)
            4 -> highlight(ProjectViewViews.MEASUREMENT_FIELDS)
            else -> highlight(null)
        }
    }
}
