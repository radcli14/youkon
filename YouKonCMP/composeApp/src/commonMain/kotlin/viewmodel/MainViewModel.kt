package viewmodel

import AccountService
import LOGIN_SCREEN
import Log
import SETTINGS_SCREEN
import SIGN_UP_SCREEN
import Storage
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import firebase.service.StorageService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.YkProject
import model.YkUser

enum class SettingsScreenState {
    HIDDEN, SETTINGS, SIGN_IN, CREATE_ACCOUNT
}

class MainViewModel(
    private val account: AccountService? = null,
    private val localStorage: Storage? = null,
    private val cloudStorage: StorageService? = null,
    verbose: Boolean = false
) : ViewModel() {

    private val _isEditingProject = MutableStateFlow(false)
    val isEditingProject: StateFlow<Boolean> = _isEditingProject

    val settingsScreenState = mutableStateOf(SettingsScreenState.HIDDEN)
    var project: MutableStateFlow<YkProject?> = MutableStateFlow(null)
    var user: MutableStateFlow<YkUser> = MutableStateFlow(YkUser())

    private val tag = "MainViewModel"

    init {
        // Initialize the user, which will originally derive from the local JSON file
        user.value = savedUser
        if (verbose) {
            Log.d(tag, "Initial User State\n==================\n\n${user.value.asJsonString()}\n\n")
        } else {
            Log.d(tag, "Initialized ViewModel for User: ${user.value.name}, with ID: ${user.value.id}")
        }

        // Set up the listeners for changes in the account login or storage states
        viewModelScope.launch {
            collectAccountService()
            collectStorageService()
        }
    }

    fun showSettings() {
        Log.d(tag, "Show Settings")
        settingsScreenState.value = SettingsScreenState.SETTINGS
    }

    fun openScreenFromSettingsScreen(route: String) {
        Log.d(tag, "openScreenFromSettingsScreen, route = $route")
        settingsScreenState.value = when (route) {
            SETTINGS_SCREEN -> SettingsScreenState.SETTINGS
            LOGIN_SCREEN -> SettingsScreenState.SIGN_IN
            SIGN_UP_SCREEN -> SettingsScreenState.CREATE_ACCOUNT
            else -> settingsScreenState.value
        }
    }

    fun openAndPopupFromLoginScreen(open: String, popup: String) {
        Log.d(tag, "openAndPopupFromLoginScreen, open = $open, popup = $popup")
        settingsScreenState.value = when (open) {
            SETTINGS_SCREEN -> SettingsScreenState.SETTINGS
            LOGIN_SCREEN -> SettingsScreenState.SIGN_IN
            SIGN_UP_SCREEN -> SettingsScreenState.CREATE_ACCOUNT
            else -> settingsScreenState.value
        }
    }

    fun restartAppFromSettingsScreen(route: String) {
        Log.d(tag, route)
    }

    fun hideSettings() {
        Log.d(tag, "Hide Settings")
        settingsScreenState.value = SettingsScreenState.HIDDEN
    }

    private suspend fun collectAccountService() {
        try {
            // When a user is emitted by Firebase, update our user with that list
            account?.currentUser?.collect { accountUser ->
                try {
                    // Update ID and name using what was emitted by Firebase
                    Log.d(tag, "collectAccountService, accountUser = $accountUser")
                    
                    // Create a new user state
                    val newUser = YkUser(
                        id = account.currentUserId,
                        name = account.currentUserName,
                        isAnonymous = accountUser.isAnonymous,
                        projects = mutableListOf()
                    )
                    
                    // Update user state
                    user.value = newUser
                    
                    // If available, update with projects from the Firebase Firestore
                    if (!accountUser.isAnonymous) {
                        try {
                            Log.d(tag, "user is not anonymous, trying cloudStorage.getUser")
                            val storageUser = cloudStorage?.getUser(accountUser.id)
                            if (storageUser != null) {
                                Log.d(tag, "Got storage user: ${storageUser.summary}")
                                user.value = storageUser
                                
                                // Create a new ProjectsCardViewModel with the updated user
                                projectsCardViewModel.value = ProjectsCardViewModel(
                                    user = user,
                                    onSaveUserToJson = { saveUserToJson() },
                                    onUpdateMainProject = { updatedProject ->
                                        if (project.value?.id == updatedProject.id) {
                                            project.value = updatedProject
                                        }
                                    }
                                )
                                
                                // Force update the projects list
                                projectsCardViewModel.value.updateProjects()
                                saveUserToJson()
                            } else {
                                Log.e(tag, "Failed to get user from cloud storage - null result")
                            }
                        } catch (e: Exception) {
                            Log.e(tag, "Failed to get user from cloud storage: ${e.message}")
                            // Create a new ProjectsCardViewModel even for local data
                            projectsCardViewModel.value = ProjectsCardViewModel(
                                user = user,
                                onSaveUserToJson = { saveUserToJson() },
                                onUpdateMainProject = { updatedProject ->
                                    if (project.value?.id == updatedProject.id) {
                                        project.value = updatedProject
                                    }
                                }
                            )
                            projectsCardViewModel.value.updateProjects()
                        }
                    } else {
                        Log.d(tag, "user is anonymous")
                        // Create a new ProjectsCardViewModel for anonymous user
                        projectsCardViewModel.value = ProjectsCardViewModel(
                            user = user,
                            onSaveUserToJson = { saveUserToJson() },
                            onUpdateMainProject = { updatedProject ->
                                if (project.value?.id == updatedProject.id) {
                                    project.value = updatedProject
                                }
                            }
                        )
                        projectsCardViewModel.value.updateProjects()
                    }
                } catch (e: Exception) {
                    Log.e(tag, "Error processing account user: ${e.message}")
                    // Continue with local data
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Error in collectAccountService: ${e.message}")
            // Continue with local data
        }
    }

    private suspend fun collectStorageService() {
        cloudStorage?.user?.collect {
            Log.d(tag, "storageUser = $it")
        }
    }

    private val defaultUser: YkUser get() = Storage().defaultUser

    /// Get the initial user, either from JSON, or from defaults
    private val savedUser: YkUser get() = localStorage?.savedUser ?: defaultUser

    fun saveUserToAll() {
        viewModelScope.launch {
            if (user.value != savedUser) {
                saveUserToJson()
                saveUserToCloud()
            }
        }
    }

    fun saveUserToJson() {
        localStorage?.saveUserToJson(user.value)
    }

    private fun saveUserToCloud() {
        viewModelScope.launch {
            if (cloudStorage?.userExists(user.value.id) == true) {
                Log.d(tag, "Updating cloud user")
                cloudStorage.update(user.value)
            } else if (!user.value.isAnonymous) {
                Log.d(tag, "Saving cloud user")
                cloudStorage?.save(user.value)
            }
        }
    }

    private fun saveProjectToCloud(project: YkProject) {
        cloudStorage?.let { storage ->
            viewModelScope.launch {
                Log.d(tag, "Saving ${project.name} to cloud")
                storage.update(user.value, project)
            }
        }
    }

    fun deleteProjectFromCloud(project: YkProject) {
        cloudStorage?.let { storage ->
            viewModelScope.launch {
                try {
                    // Only try to delete from cloud if the project has a userId (meaning it's been synced)
                    if (project.userId.isNotEmpty()) {
                        Log.d(tag, "Deleting ${project.name} from cloud")
                        storage.delete(user.value, project.id)
                    } else {
                        Log.e(tag, "Project ${project.name} not yet synced to cloud, skipping cloud delete")
                    }
                } catch (e: Exception) {
                    Log.d(tag, "Failed to delete project from cloud: ${e.message}")
                } finally {
                    // Always remove from local state
                    val newProjects = user.value.projects.toMutableList()
                    newProjects.removeAll { p -> p.id == project.id }
                    user.value = user.value.copy(projects = newProjects)
                    saveUserToJson()
                }
            }
        }
    }

    /// The user tapped the measurements in a project's disclosure group, toggle editable measurements sheet
    fun startEditing(projectToEdit: YkProject) {
        _isEditingProject.value = isEditingProject.value == false
        project.value = if (isEditingProject.value) projectToEdit else null
        Log.d(tag, "started editing to $project")
    }

    /// The user exited the bottom sheet, stop editing the project
    fun stopEditing(saveAfterStopping: Boolean = false) {
        Log.d(tag, "stopped editing ${project.value?.name}")
        project.value?.let { projectsCardViewModel.value.stopEditing(it) }
        _isEditingProject.value = false
        if (saveAfterStopping) {
            saveUserToJson()
            project.value?.let { saveProjectToCloud(it) }
        }
        project.value = null
    }

    /// The `ProjectsCardViewModel` is retained to persist the states of the individual projects
    var projectsCardViewModel = MutableStateFlow(ProjectsCardViewModel(
        user = user,
        onSaveUserToJson = { saveUserToJson() },
        onUpdateMainProject = { updatedProject ->
            if (project.value?.id == updatedProject.id) {
                project.value = updatedProject
            }
        }
    ))
}
