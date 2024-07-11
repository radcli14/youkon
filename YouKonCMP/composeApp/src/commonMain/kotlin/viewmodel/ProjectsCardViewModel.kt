package viewmodel

import Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import model.ProjectExpansionLevel
import model.YkProject
import model.YkUser

enum class ProjectsCardViews {
    SURFACE, PLUS, MINUS, REORDER, PROJECT
}

class ProjectsCardViewModel(var user: YkUser = YkUser()) : ViewModel() {
    var projects: SnapshotStateList<YkProject> = mutableStateListOf()
    val canSubtract = mutableStateOf(false)
    val showSubtractAlert = mutableStateOf(false)
    val canReorder = mutableStateOf(false)
    val projectToDelete: MutableState<YkProject?> = mutableStateOf(null)
    private var pvcDict = mutableMapOf<String, ProjectViewModel>()

    private val tag = "ProjectsCardViewModel"

    /// Initialize with a generic user
    init {
        updateProjects()
    }

    /// Change the state of the project from EDITABLE back to STATIC when the user closes the sheet
    fun stopEditing(project: YkProject) {
        projectViewModel(project).expansion.value = ProjectExpansionLevel.STATIC
    }

    /// Update the public list of `YkProject` items by assuring that the Kotlin version is Swift formatted
    fun updateProjects() {
        user.projects.forEachIndexed { idx, project ->
            val idxInState = projects.indexOf(project)
            if (idxInState == -1) {
                // Did not find this project in the state list of projects, add it here
                projects.add(idx, project)
            } else if (idx != idxInState) {
                // Found this project, but it was at the wrong location, move it here
                projects.removeAt(idxInState)
                projects.add(idx, project)
            }
        }

        // Remove any projects from the state list that don't exist in the user
        projects.removeAll { project -> user.projects.indexOf(project) == -1 }
        //projects.value = user.projects.toTypedArray()
    }

    /// Add a new, blank, `YkProject` to the `YkUser`
    fun addProject() {
        user.addProject()
        updateProjects()
        Log.d(tag, "added a new project: ${projects}")
    }

    /// To persist the `ProjectViewModel` inside the project card, it is retained in the `pvcDict`
    fun projectViewModel(project: YkProject): ProjectViewModel {
        var pvm = pvcDict[project.id]
        if (pvm == null) {
            pvm = ProjectViewModel(project)
            pvcDict[project.id] = pvm
        }
        return pvm
    }

    /// Used in the onboarding screen, requesting `projectViewModel()` without arguments gives the first as a default
    fun projectViewModel(): ProjectViewModel {
        return if (projects.isNotEmpty()) {
            projectViewModel(projects.first())
        } else ProjectViewModel()
    }

    /// Make the button to remove any of the `YkProject`s visible
    fun onSubtractButtonTap() {
        if (!canReorder.value) {
            canSubtract.value = !canSubtract.value
        }
    }

    /// Remove the specified `YkProject` from the `YkUser`
    fun subtract(project: YkProject) {
        projectToDelete.value = project
        showSubtractAlert.value = true
    }

    fun confirmDelete() {
        projectToDelete.value?.let {
            user.removeProject(it)
            updateProjects()
        }
        cleanupDelete()
    }

    fun cancelDelete() {
        cleanupDelete()
    }

    /// Reset the variables associated with showing an alert and subtracting a project to their defaults
    private fun cleanupDelete() {
        showSubtractAlert.value = false
        projectToDelete.value = null
        canSubtract.value = false
    }

    /// When tapping the swap button, this will open the controls to allow user to reorder projects
    fun onReorderButtonTap() {
        if (!canSubtract.value) {
            canReorder.value = !canReorder.value
        }
    }

    /// The controls to move a project "up" or "down"
    fun onReorderControlButtonTap(project: YkProject, direction: String) {
        Log.d(tag, "onReorderControlButtonTap: move ${project.name} $direction")
        user.moveProject(project, direction)
        updateProjects()
    }

    /// When viewing the onboard screen, this modifies which view is highlighted
    var highlightedView: MutableState<ProjectsCardViews?> = mutableStateOf(null)
    fun highlight(view: ProjectsCardViews?) {
        highlightedView.value = view
    }
    fun highlight(viewInt: Int?) {
        when (viewInt) {
            0 -> highlight(ProjectsCardViews.SURFACE)
            1 -> highlight(ProjectsCardViews.PLUS)
            2 -> highlight(ProjectsCardViews.MINUS)
            3 -> highlight(ProjectsCardViews.PROJECT)
            else -> highlight(view = null)
        }
    }
}
