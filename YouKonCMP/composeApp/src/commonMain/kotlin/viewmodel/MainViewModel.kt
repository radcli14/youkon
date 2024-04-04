package viewmodel

import Log
import Storage
import androidx.compose.runtime.mutableStateOf
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import model.YkProject
import model.YkUser

enum class SettingsScreenState {
    HIDDEN, SETTINGS, SIGN_IN, CREATE_ACCOUNT
}

class MainViewModel(
    private val storage: Storage? = null,
    verbose: Boolean = false
) : ViewModel() {

    private val _isEditingProject = MutableLiveData(false)
    val isEditingProject: LiveData<Boolean> = _isEditingProject

    val settingsScreenState = mutableStateOf(SettingsScreenState.HIDDEN)

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
    fun stopEditing(saveAfterStopping: Boolean = false) {
        Log.d(tag, "stopped editing ${project?.name}")
        project?.let { projectsCardViewModel.stopEditing(it) }
        _isEditingProject.value = false
        project = null
        if (saveAfterStopping) {
            saveUserToJson()
        }
    }

    fun showSettings() {
        Log.d(tag, "Show Settings")
        settingsScreenState.value = SettingsScreenState.SETTINGS
    }

    fun openScreenFromSettingsScreen(route: String) {
        Log.d(tag, "openScreenFromSettingsScreen, route = $route")
        when (route) {
            "SettingsScreen" -> settingsScreenState.value = SettingsScreenState.SETTINGS
            "LoginScreen" -> settingsScreenState.value = SettingsScreenState.SIGN_IN
        }
    }

    fun openAndPopupFromLoginScreen(open: String, popup: String) {
        Log.d(tag, "openAndPopupFromLoginScreen, open = $open, popup = $popup")
        openScreenFromSettingsScreen(open)
    }

    fun restartAppFromSettingsScreen(route: String) {
        Log.d(tag, route)
    }

    fun hideSettings() {
        Log.d(tag, "Hide Settings")
        settingsScreenState.value = SettingsScreenState.HIDDEN
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
