package viewmodel

//import android.os.Environment
import Storage
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import model.YkProject
import model.YkUser
//import java.io.File

class MainViewModel(loadDefault: Boolean = false) : ViewModel() {
    private val _isEditingProject = MutableLiveData(false)
    val isEditingProject: LiveData<Boolean> = _isEditingProject
    var project: YkProject? = null
    var user = YkUser()

    private val tag = "MainViewModel"

    init {
        user = if (loadDefault) defaultUser else savedUser
        Log.d(tag, "Initial User State\n==================\n\n" + user.asJsonString() + "\n\n")
        saveUserToJson()
    }

    private val defaultUser: YkUser get() = Storage.defaultUser
    private val savedUser: YkUser get() = Storage.savedUser
    fun saveUserToJson() {
        Storage.saveUserToJson(user)
    }

    /// The user tapped the measurements in a project's disclosure group, toggle editable measurements sheet
    fun toggleEdit(projectToEdit: YkProject) {
        _isEditingProject.value = isEditingProject.value == false
        project = if (isEditingProject.value == true) projectToEdit else null
        Log.d(tag, "toggled edit to $project")
    }

    /// The user exited the bottom sheet, stop editing the project
    fun stopEditing() {
        Log.d(tag, "stopped editing ${project?.name}")
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
