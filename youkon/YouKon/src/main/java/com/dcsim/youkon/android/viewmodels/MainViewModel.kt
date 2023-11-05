package com.dcsim.youkon.android.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dcsim.youkon.YkProject
import com.dcsim.youkon.YkUser

class MainViewModel: ViewModel() {
    var isEditingProject by mutableStateOf(false)
    var project: YkProject? = null
    var user = YkUser()

    private val tag = "MainViewModel"

    init {
        user = savedUser
        Log.d(tag, "Initial User State\n==================\n\n" + user.asJsonString() + "\n\n")
        //saveUserToJson()
    }

    /// The default user for someone opening the app for the first time is stored in `resources/defaultuser.json`
    val defaultUser: YkUser
        get() {
            /*if let path = Bundle.main.path(forResource: "defaultuser", ofType: "json"),
                let contents = try? String(contentsOfFile: path),
                let defaultUser = user.fromJsonString(jsonString: contents) {
                return defaultUser
            } else {
                print("Failed to load the default YkUser, falling back to an empty YkUser()")
                return YkUser()
            }*/
            return YkUser()
        }

    /// The `YkUser` that is saved from a previous session
    val savedUser: YkUser
        get() {
            /*if let contents = try ? String(contentsOf: workingFile),
                let savedUser = user . fromJsonString (jsonString: contents)  {
                    return savedUser
                } else {
                    print("Failed to load the saved YkUser, falling back to a default user")
                    return defaultUser
                }
             */
            return YkUser()
        }
    /*
    /// The URL where the user data file will be stored
    var documentsUrl: URL {
        // find all possible documents directories for this user
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)

        // just send back the first one, which ought to be the only one
        return paths[0]
    }
    */

    /*
    /// The working file, which will be imported on startup, and saved any time the user modifies the data
    var workingFile: URL {
        return documentsUrl.appendingPathComponent("userdata.json")
    }
    */

    /*
    /// Save the current `YkUser` to a `.json` file
    func saveUserToJson() {
        let str = user.asJsonString()
        do {
            try str.write(to: workingFile, atomically: true, encoding: .utf8)
        } catch {
            print(error.localizedDescription)
        }
    }
    */

    /*
    /// The user tapped the measuments in a project's disclosure group, toggle editable measurements sheet
    func toggleEdit(to project: YkProject) {
        isEditingProject.toggle()
        self.project = isEditingProject ? project : nil
    }
    */

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
