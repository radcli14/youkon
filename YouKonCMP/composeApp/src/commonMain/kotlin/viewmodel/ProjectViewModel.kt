package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
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

class ProjectViewModel(
    initialProject: YkProject = YkProject(),
    private val onProjectUpdated: (YkProject) -> Unit = {}
) : ViewModel() {
    val project = MutableStateFlow(initialProject)
    val editedName = mutableStateOf(initialProject.name)
    val editedDescription = mutableStateOf(initialProject.about)
    val convertToSystem = mutableStateOf(YkSystem.SI)
    var measurements: MutableStateFlow<List<YkMeasurement>> = MutableStateFlow(
        initialProject.measurements.toList()
    )
    val expansion = mutableStateOf(ProjectExpansionLevel.COMPACT)
    val canSubtract = mutableStateOf(false)
    val showSubtractAlert = mutableStateOf(false)
    val measurementToDelete: MutableState<YkMeasurement?> = mutableStateOf(null)

    val canReorder = mutableStateOf(false)

    private val tag = "ProjectsCardViewModel"

    /// Update the public list of `YkProject` items by assuring that the Kotlin version is Swift formatted
    private fun updateMeasurements() {
        project.value = project.value.copy(
            measurements = project.value.measurements.toMutableList()
        )
        measurements.value = project.value.measurements.toList()
        onProjectUpdated(project.value)
    }

    fun updateName(name: String) {
        project.value = project.value.copy(name = name)
        editedName.value = name
        onProjectUpdated(project.value)
    }

    fun updateDescription(description: String) {
        project.value = project.value.copy(about = description)
        editedDescription.value = description
        onProjectUpdated(project.value)
    }

    fun updateMeasurement(updatedMeasurement: YkMeasurement) {
        val currentProjectMeasurements = project.value.measurements.toMutableList()
        val index = currentProjectMeasurements.indexOfFirst { it.id == updatedMeasurement.id }
        if (index != -1) {
            currentProjectMeasurements[index] = updatedMeasurement
            project.value = project.value.copy(measurements = currentProjectMeasurements)
            measurements.value = currentProjectMeasurements.toList()
            onProjectUpdated(project.value)
        }
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
            name = "New Measurement",
            about = "A new measurement"
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

    /// When tapping the swap button, this will open the controls to allow user to reorder projects
    fun onReorderButtonTap() {
        if (!canSubtract.value) {
            canReorder.value = !canReorder.value
        }
    }

    /// The controls to move a project "up" or "down"
    fun onReorderControlButtonTap(measurement: YkMeasurement, direction: String) {
        val newProjects = project.value.measurements.toMutableList()
        val idx = newProjects.indexOf(measurement)
        val toIndex = when {
            direction == "up" && idx > 0 -> idx - 1
            direction == "down" && idx < newProjects.count()-1 -> idx + 1
            else -> null
        }
        toIndex?.let {
            val movedProject = newProjects.removeAt(idx)
            newProjects.add(toIndex, movedProject)
            project.value = project.value.copy(measurements = newProjects)
            measurements.value = newProjects.toList()
            onProjectUpdated(project.value)
        }
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

    fun refreshProject(newProject: YkProject) {
        project.value = newProject
        editedName.value = newProject.name
        editedDescription.value = newProject.about
        measurements.value = newProject.measurements.toList()
    }
}
