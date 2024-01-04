package com.dcengineer.youkon.viewmodels

import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dcengineer.youkon.YkProject
import com.dcengineer.youkon.YkUser
import java.io.File

class MainViewModel(loadDefault: Boolean = false): ViewModel() {
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

    /// The default user for someone opening the app for the first time is stored in `resources/defaultuser.json`
    val defaultUser: YkUser
        get() {
            // TODO: load from JSON file in resources directory
            /*
            if let path = Bundle.main.path(forResource: "defaultuser", ofType: "json"),
                let contents = try? String(contentsOfFile: path),
                let defaultUser = user.fromJsonString(jsonString: contents) {
                return defaultUser
            } else {
                print("Failed to load the default YkUser, falling back to an empty YkUser()")
                return YkUser()
            }
            */
            val defaultUser = YkUser()
            defaultUser.setAsTestUser()
            return defaultUser
        }

    /// The `YkUser` that is saved from a previous session
    val savedUser: YkUser
        get() {
            try {
                val contents = workingFile.readText()
                YkUser().fromJsonString(contents)?.let { savedUser ->
                    return savedUser
                }
            } catch (err: Exception) {
                Log.d(tag, "Failed to load the saved YkUser, falling back to a default user")
            }
            return defaultUser
        }

    /// The URL where the user data file will be stored
    val documentsUrl: File
        get() {
            return File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                "YouKon"
            )
        }

    /// The working file, which will be imported on startup, and saved any time the user modifies the data
    val workingFile: File
        get() = File("${documentsUrl.absolutePath}/userdata.json")

    fun setAsDefaultUser() {
        user = defaultUser
    }

    /// Save the current `YkUser` to a `.json` file
    fun saveUserToJson() {
        val jsonString = user.asJsonString()
        try {
            if (!workingFile.exists()) {
                // If the file doesn't exist, try creating it along with the necessary directories
                workingFile.parentFile?.mkdirs()
                workingFile.createNewFile()
            }
            workingFile.writeText(jsonString)
        } catch(exception: Exception) {
            Log.d(tag,"Save failed because $exception")
            exception.printStackTrace()
        }
    }

    /// The user tapped the measurements in a project's disclosure group, toggle editable measurements sheet
    fun toggleEdit(projectToEdit: YkProject) {
        Log.d(tag, "toggled edit to $project")
        _isEditingProject.value = isEditingProject.value == false
        project = if (isEditingProject.value == true) projectToEdit else null
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
