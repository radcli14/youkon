package viewmodel

import AccountService
import Log
import Storage
import androidx.compose.runtime.mutableStateOf
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import firebase.service.StorageService
import kotlinx.coroutines.launch
import model.YkProject
import model.YkUser

enum class SettingsScreenState {
    HIDDEN, SETTINGS, SIGN_IN, CREATE_ACCOUNT
}

class MainViewModel(
    private val account: AccountService? = null,
    private val storage: Storage? = null,
    private val storageService: StorageService? = null,
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
            Log.d(tag, "Initialized ViewModel for User: ${user.name}, with ID: ${user.id}")
        }
    }

    private val defaultUser: YkUser get() = Storage().defaultUser
    private val savedUser: YkUser
        get() {
            // Get the initial user, either from JSON, or from defaults
            val user = storage?.savedUser ?: defaultUser

            // If Firebase did its job, update ID and name
            account?.currentUserId?.let { currentUserId ->
                user.id = currentUserId
                user.projects.forEach { project -> project.userId = currentUserId }
            }
            account?.currentUserName?.let { currentUserName -> user.name = currentUserName }

            // When a listOfProjects is emitted by Firebase, update our user with that list
            viewModelScope.launch {
                try {
                    storageService?.projects?.collect { listOfProjects ->
                        listOfProjects.forEachIndexed { idx, project ->
                            Log.d(tag, "  $idx. $project")
                        }
                        if (listOfProjects.isNotEmpty()) {
                            projectsCardViewModel.updateProjects(listOfProjects)
                        }
                        // TODO: find an alternate place to put this, this is temporary
                        /*if (listOfProjects.isEmpty()) {
                            user.projects.forEach { project ->
                                storageService?.save(project)
                            }
                        }*/
                    }
                } catch(e: Exception) {
                    Log.d(tag, "Failed getting projects from storage service, error was ${e.message}")
                }
            }

            return user
        }

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
        settingsScreenState.value = when (route) {
            "SettingsScreen" -> SettingsScreenState.SETTINGS
            "LoginScreen" -> SettingsScreenState.SIGN_IN
            "SignUpScreen" -> SettingsScreenState.CREATE_ACCOUNT
            else -> settingsScreenState.value
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
