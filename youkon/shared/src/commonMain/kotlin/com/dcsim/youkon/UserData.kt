package com.dcsim.youkon

/// Holds the user's name and multiple projects
class YkUser {
    var name = "New User"
    var projects = mutableListOf<YkProject>()

    /// Add a project with `name` and `about` strings, but empty `measurements` and `image`
    fun addProject(name: String = "", description: String = "") {
        val project = YkProject()
        project.name = name
        project.about = description
        projects.add(project)
    }

    /// Remove a project from the `projects` list
    fun removeProject(project: YkProject) {
        projects.forEachIndexed { idx, p ->
            if (project == p) {
                projects.removeAt(idx)
            }
        }
    }
}

/// For testing, create a generic user
fun testUser(): YkUser {
    val data = YkUser()
    data.name = "Eliott"
    data.projects.add(wembyProject())
    data.projects.add(spaceProject())
    return data
}