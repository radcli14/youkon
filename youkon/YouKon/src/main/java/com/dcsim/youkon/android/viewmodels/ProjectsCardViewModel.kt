package com.dcsim.youkon.android.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dcsim.youkon.YkProject
import com.dcsim.youkon.YkUser

class ProjectsCardViewModel(var user: YkUser = YkUser()): ViewModel() {
    var projects: MutableState<List<YkProject>> = mutableStateOf(mutableListOf())
    val canSubtract = mutableStateOf(false)
    val showSubtractAlert = mutableStateOf(false)
    val projectToDelete: MutableState<YkProject?> = mutableStateOf(null)

    //var pvcDict = mutableMapOf<String, ProjectViewController>()
    private val tag = "ProjectsCardViewModel"

    /// Initialize with a generic user
    init {
        updateProjects()
    }

    /// Update the public list of `YkProject` items by assuring that the Kotlin version is Swift formatted
    private fun updateProjects() {
        projects.value = user.projects
    }

    /// Add a new, blank, `YkProject` to the `YkUser`
    fun addProject() {
        user.addProject()
        updateProjects()
        //Log.d(tag, projects)
    }

    /// To persist the `ProjectViewController` inside the project card, it is retained in the `pvcDict`
    /*
    fun projectViewController(project: YkProject) -> ProjectViewController {
        guard let pvc = pvcDict[project.id] else {
            let pvc = ProjectViewController(for: project)
            pvcDict[project.id] = pvc
            return pvc
        }
        return pvc
    }
    */

    /// Make the button to remove any of the `YkProject`s visible
    fun onSubtractButtonTap() {
        canSubtract.value = !canSubtract.value
    }

    /// Remove the specified `YkProject` from the `YkUser`
    fun subtract(project: YkProject) {
        projectToDelete.value = project
        showSubtractAlert.value = true
    }

    /*
    func confirmDelete() {
        if let projectToDelete {
            user.removeProject(project: projectToDelete)
            updateProjects()
        }
        cleanupDelete()
    }

    func cancelDelete() {
        cleanupDelete()
    }

    /// Reset the variables associated with showing an alert and subtracting a project to their defaults
    private func cleanupDelete() {
        showSubtractAlert = false
        projectToDelete = nil
        canSubtract = false
    }
     */
}