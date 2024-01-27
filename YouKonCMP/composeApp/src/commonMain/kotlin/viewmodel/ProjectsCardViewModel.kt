package viewmodel

//import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
//import androidx.lifecycle.ViewModel
import model.YkProject
import model.YkUser

enum class ProjectsCardViews {
    SURFACE, PLUS, MINUS, PROJECT
}

class ProjectsCardViewModel(var user: YkUser = YkUser()) {//: ViewModel() {
    var projects: MutableState<Array<YkProject>> = mutableStateOf(user.projects.toTypedArray())
    val canSubtract = mutableStateOf(false)
    val showSubtractAlert = mutableStateOf(false)
    val projectToDelete: MutableState<YkProject?> = mutableStateOf(null)
    private var pvcDict = mutableMapOf<String, ProjectViewModel>()

    private val tag = "ProjectsCardViewModel"

    /// Initialize with a generic user
    init {
        updateProjects()
    }

    /// Update the public list of `YkProject` items by assuring that the Kotlin version is Swift formatted
    private fun updateProjects() {
        projects.value = user.projects.toTypedArray()
    }

    /// Add a new, blank, `YkProject` to the `YkUser`
    fun addProject() {
        user.addProject()
        updateProjects()
        //Log.d(tag, "added a new project: ${projects.value}")
    }

    /// To persist the `ProjectViewModel` inside the project card, it is retained in the `pvcDict`
    fun projectViewModel(project: YkProject): ProjectViewModel {
        var pvc = pvcDict[project.id]
        if (pvc == null) {
            pvc = ProjectViewModel(project)
            pvcDict[project.id] = pvc
        }
        return pvc
    }

    /// Used in the onboarding screen, requesting `projectViewModel()` without arguments gives the first as a default
    fun projectViewModel(): ProjectViewModel {
        return if (projects.value.isNotEmpty()) {
            projectViewModel(projects.value.first())
        } else ProjectViewModel()
    }

    /// Make the button to remove any of the `YkProject`s visible
    fun onSubtractButtonTap() {
        canSubtract.value = !canSubtract.value
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