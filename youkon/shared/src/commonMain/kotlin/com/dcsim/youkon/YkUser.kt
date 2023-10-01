package com.dcsim.youkon
import kotlinx.serialization.*
import kotlinx.serialization.json.*

/// Holds the user's name and multiple projects
@Serializable
class YkUser {
    var name = "New User"
    var projects = mutableListOf<YkProject>()

    fun fromJsonString(jsonString: String): YkUser? {
        return Json.decodeFromString<YkUser>(jsonString)
    }

    fun asJsonString(): String {
        return Json.encodeToString(this)
    }

    /// Add a project with `name` and `about` strings, but empty `measurements` and `image`
    fun addProject(name: String = "", description: String = "") {
        val project = YkProject()
        project.name = name
        project.about = description
        projects.add(project)
    }

    fun addProject() {
        addProject(name = "New Project")
    }

    /// Remove a project from the `projects` list
    fun removeProject(project: YkProject) {
        projects.forEachIndexed { idx, p ->
            if (project == p) {
                projects.removeAt(idx)
            }
        }
    }

    /// For testing, create a generic user
    fun setAsTestUser() {
        name = "Eliott"
        projects.add(wembyProject())
        projects.add(spaceProject())
    }
}
