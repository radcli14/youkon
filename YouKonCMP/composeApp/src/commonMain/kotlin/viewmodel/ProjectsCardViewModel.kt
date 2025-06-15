package viewmodel

import Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import model.ProjectExpansionLevel
import model.YkProject
import model.YkUser
import kotlinx.coroutines.flow.MutableStateFlow

enum class ProjectsCardViews {
    SURFACE, PLUS, MINUS, REORDER, PROJECT
}

class ProjectsCardViewModel(
    var user: MutableStateFlow<YkUser> = MutableStateFlow(YkUser()),
    private val onSaveUserToJson: () -> Unit = {},
    private val onUpdateMainProject: (YkProject) -> Unit = {}
) : ViewModel() {
    var projects: SnapshotStateList<YkProject> = mutableStateListOf()
    val canSubtract = mutableStateOf(false)
    val showSubtractAlert = mutableStateOf(false)
    val canReorder = mutableStateOf(false)
    val projectToDelete: MutableState<YkProject?> = mutableStateOf(null)
    private var pvcDict = mutableMapOf<String, ProjectViewModel>()

    private val tag = "ProjectsCardViewModel"

    /// Initialize with a generic user
    init {
        Log.d(tag, "ProjectsCardViewModel init: user.value = ${user.value.summary}")
        updateProjects()
        Log.d(tag, "ProjectsCardViewModel init: projects = ${projects.map { it.name }}")
    }

    /// Change the state of the project from EDITABLE back to STATIC when the user closes the sheet
    fun stopEditing(project: YkProject) {
        Log.d(tag, "stopEditing called for project: ${project.name}")
        projectViewModel(project).expansion.value = ProjectExpansionLevel.STATIC
        Log.d(tag, "stopEditing: projectViewModel(${project.name}).expansion.value = ${projectViewModel(project).expansion.value}")
    }

    /// Update a `YkProject` in the list with a modified version
    fun updateProject(updatedProject: YkProject) {
        Log.d(tag, "updateProject called for project: ${updatedProject.name}, ID: ${updatedProject.id}")
        val currentProjects = user.value.projects.toMutableList()
        val index = currentProjects.indexOfFirst { it.id == updatedProject.id }
        if (index != -1) {
            Log.d(tag, "updateProject: Found project at index $index, old project = ${currentProjects[index].name}")
            currentProjects[index] = updatedProject
            user.value = user.value.copy(projects = currentProjects)
            updateProjects()
            onSaveUserToJson()
            onUpdateMainProject(updatedProject)
            Log.d(tag, "updateProject: After update, user.value.projects = ${user.value.projects.map { it.name }}")
            Log.d(tag, "updateProject: After update, projects (SnapshotStateList) = ${projects.map { it.name }}")
        } else {
            Log.d(tag, "updateProject: Project with ID ${updatedProject.id} not found in user.value.projects")
        }
    }

    /// Update the public list of `YkProject` items by assuring that the Kotlin version is Swift formatted
    fun updateProjects() {
        Log.d(tag, "updateProjects called in ProjectsCardViewModel")
        // Clear existing projects
        projects.clear()
        
        // Add all projects from user
        user.value.projects.forEach { project ->
            projects.add(project)
        }
        
        Log.d(tag, "updateProjects: Final projects (SnapshotStateList) = ${projects.map { it.name }}")
    }

    /// Add a new, blank, `YkProject` to the `YkUser`
    fun addProject() {
        Log.d(tag, "addProject called")
        val newProjects = user.value.projects.toMutableList()
        val newProject = YkProject()
        newProjects.add(newProject)
        user.value = user.value.copy(projects = newProjects)
        updateProjects()
        Log.d(tag, "addProject: added a new project: ${newProject.name}, user.value.projects = ${user.value.projects.map { it.name }}")
        onSaveUserToJson()
    }

    /// To persist the `ProjectViewModel` inside the project card, it is retained in the `pvcDict`
    fun projectViewModel(project: YkProject, onProjectUpdated: (YkProject) -> Unit = {}): ProjectViewModel {
        Log.d(tag, "projectViewModel called for project: ${project.name}, ID: ${project.id}")
        var pvm = pvcDict[project.id]
        if (pvm == null) {
            Log.d(tag, "projectViewModel: Creating new ProjectViewModel for ${project.name}")
            pvm = ProjectViewModel(project) { updatedProject ->
                updateProject(updatedProject)
            }
            pvcDict[project.id] = pvm
        } else {
            Log.d(tag, "projectViewModel: Reusing existing ProjectViewModel for ${project.name}, refreshing it.")
            pvm.refreshProject(project)
        }
        return pvm
    }

    /// Used in the onboarding screen, requesting `projectViewModel()` without arguments gives the first as a default
    fun projectViewModel(): ProjectViewModel {
        Log.d(tag, "projectViewModel (default) called")
        return if (projects.isNotEmpty()) {
            projectViewModel(projects.first())
        } else ProjectViewModel()
    }

    /// Make the button to remove any of the `YkProject`s visible
    fun onSubtractButtonTap() {
        Log.d(tag, "onSubtractButtonTap called: current canSubtract = ${canSubtract.value}")
        if (!canReorder.value) {
            canSubtract.value = !canSubtract.value
        }
        Log.d(tag, "onSubtractButtonTap: new canSubtract = ${canSubtract.value}")
    }

    /// Remove the specified `YkProject` from the `YkUser`
    fun subtract(project: YkProject) {
        Log.d(tag, "subtract called for project: ${project.name}")
        projectToDelete.value = project
        showSubtractAlert.value = true
        Log.d(tag, "subtract: showSubtractAlert.value = ${showSubtractAlert.value}")
    }

    fun confirmDelete() {
        Log.d(tag, "confirmDelete called for project: ${projectToDelete.value?.name}")
        projectToDelete.value?.let {
            Log.d(tag, "confirmDelete: Deleting project ${it.name}")
            val newProjects = user.value.projects.toMutableList()
            newProjects.removeAll { p -> p.id == it.id }
            user.value = user.value.copy(projects = newProjects)
            updateProjects()
            onSaveUserToJson()
        }
        cleanupDelete()
        Log.d(tag, "confirmDelete: After cleanup, showSubtractAlert.value = ${showSubtractAlert.value}")
    }

    fun cancelDelete() {
        Log.d(tag, "cancelDelete called")
        cleanupDelete()
        Log.d(tag, "cancelDelete: After cleanup, showSubtractAlert.value = ${showSubtractAlert.value}")
    }

    /// Reset the variables associated with showing an alert and subtracting a project to their defaults
    private fun cleanupDelete() {
        Log.d(tag, "cleanupDelete called in ProjectsCardViewModel")
        showSubtractAlert.value = false
        projectToDelete.value = null
        canSubtract.value = false
        Log.d(tag, "cleanupDelete: showSubtractAlert = ${showSubtractAlert.value}, projectToDelete = ${projectToDelete.value}, canSubtract = ${canSubtract.value}")
    }

    /// When tapping the swap button, this will open the controls to allow user to reorder projects
    fun onReorderButtonTap() {
        Log.d(tag, "onReorderButtonTap called in ProjectsCardViewModel: current canSubtract = ${canSubtract.value}, current canReorder = ${canReorder.value}")
        if (!canSubtract.value) {
            canReorder.value = !canReorder.value
        }
        Log.d(tag, "onReorderButtonTap: new canReorder = ${canReorder.value}")
    }

    /// The controls to move a project "up" or "down"
    fun onReorderControlButtonTap(project: YkProject, direction: String) {
        Log.d(tag, "onReorderControlButtonTap called for project: ${project.name}, direction: $direction")
        val newProjects = user.value.projects.toMutableList()
        val idx = newProjects.indexOf(project)
        val toIndex = when {
            direction == "up" && idx > 0 -> idx - 1
            direction == "down" && idx < newProjects.count()-1 -> idx + 1
            else -> null
        }
        toIndex?.let {
            Log.d(tag, "onReorderControlButtonTap: Moving project ${project.name} from index $idx to $toIndex")
            val movedProject = newProjects.removeAt(idx)
            newProjects.add(toIndex, movedProject)
            user.value = user.value.copy(projects = newProjects)
            updateProjects()
            onSaveUserToJson()
            Log.d(tag, "onReorderControlButtonTap: After move, user.value.projects = ${user.value.projects.map { it.name }}")
        }
    }

    /// When viewing the onboarding screen, this modifies which view is highlighted
    var highlightedView: MutableState<ProjectsCardViews?> = mutableStateOf(null)
    fun highlight(view: ProjectsCardViews?) {
        Log.d(tag, "highlight called: view = $view")
        highlightedView.value = view
    }

    fun highlightInProjectsCard(viewInt: Int?) {
        Log.d(tag, "highlightInProjectsCard called: viewInt = $viewInt")
        when (viewInt) {
            0 -> highlight(ProjectsCardViews.SURFACE)
            1 -> highlight(ProjectsCardViews.PLUS)
            2 -> highlight(ProjectsCardViews.MINUS)
            3 -> highlight(ProjectsCardViews.REORDER)
            4 -> highlight(ProjectsCardViews.PROJECT)
            else -> highlight(null)
        }
    }
}
