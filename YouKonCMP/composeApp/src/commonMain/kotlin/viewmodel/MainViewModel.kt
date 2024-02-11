package viewmodel

import Log
import Storage
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import model.YkProject
import model.YkUser

class MainViewModel(
    private val storage: Storage? = null,
    verbose: Boolean = false
) : ViewModel() {
    private val _isEditingProject = MutableLiveData(false)
    val isEditingProject: LiveData<Boolean> = _isEditingProject
    var project: YkProject? = null
    var user = YkUser()

    private val tag = "MainViewModel"

    init {
        user = savedUser
        if (verbose) {
            Log.d(tag, "Initial User State\n==================\n\n${user.asJsonString()}\n\n")
        } else {
            Log.d(tag, "Initialized ViewModel for User: ${user.name}")
        }
    }

    private val defaultUser: YkUser get() = Storage().defaultUser
    private val savedUser: YkUser get() = storage?.savedUser ?: defaultUser
    fun saveUserToJson() {
        storage?.saveUserToJson(user)
    }

    /// The user tapped the measurements in a project's disclosure group, toggle editable measurements sheet
    fun startEditing(projectToEdit: YkProject) {
        _isEditingProject.value = isEditingProject.value == false
        project = if (isEditingProject.value) projectToEdit else null
        Log.d(tag, "started editing to $project")
    }

    /// The user exited the bottom sheet, stop editing the project
    fun stopEditing() {
        Log.d(tag, "stopped editing ${project?.name}")
        project?.let { projectsCardViewModel.stopEditing(it) }
        _isEditingProject.value = false
        project = null
    }

    /// The `ProjectsCardViewModel` is retained to persist the states of the individual projects
    val projectsCardViewModel: ProjectsCardViewModel
        get() {
            _projectsCardViewModel?.let {
                return it
            }
            _projectsCardViewModel = ProjectsCardViewModel(user)
            return _projectsCardViewModel!!
        }
    private var _projectsCardViewModel: ProjectsCardViewModel? = null
}
