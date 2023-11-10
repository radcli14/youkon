package com.dcsim.youkon.android.viewmodels

import android.os.Environment
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dcsim.youkon.YkProject
import com.dcsim.youkon.YkUser
import java.io.File

class MainViewModel: ViewModel() {
    var isEditingProject by mutableStateOf(false)
    var project: YkProject? = null
    var user = YkUser()

    private val tag = "MainViewModel"

    init {
        user = defaultUser // savedUser
        Log.d(tag, "Initial User State\n==================\n\n" + user.asJsonString() + "\n\n")
        //saveUserToJson()
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
            val contents = workingFile.readText()
            YkUser().fromJsonString(contents)?.let { savedUser ->
                return savedUser
            }
            Log.d(tag, "Failed to load the saved YkUser, falling back to a default user")
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

    /// Save the current `YkUser` to a `.json` file
    fun saveUserToJson() {
        val jsonString = user.asJsonString()
        try {
            workingFile.writeText(jsonString)
        } catch(exception: Exception) {
            Log.d(tag,"Save failed because $exception")
            exception.printStackTrace()
        }
    }

    /// The user tapped the measurements in a project's disclosure group, toggle editable measurements sheet
    fun toggleEdit(project: YkProject) {
        Log.d(tag, "toggled edit to $project")
        isEditingProject = !isEditingProject
        this.project = if (isEditingProject) project else null
    }

    /*
    /// The `ProjectsCardController` is retained to persist the states of the individual projects
    var projectsCardController: ProjectsCardController {
        if let _projectsCardController {
            return _projectsCardController
        } else {
            _projectsCardController = ProjectsCardController(with: user)
            return self.projectsCardController
        }
    }
    private var _projectsCardController: ProjectsCardController? = nil
     */
}
