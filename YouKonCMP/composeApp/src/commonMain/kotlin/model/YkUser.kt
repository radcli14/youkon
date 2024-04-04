package model
import UUIDGenerator
import kotlinx.serialization.*
import kotlinx.serialization.json.*

/// Holds the user's name and multiple projects
@Serializable
class YkUser(
    var name: String = "Anonymous User",
    var projects: MutableList<YkProject> = mutableListOf(),
    val isAnonymous: Boolean = true,
    val id: String = UUIDGenerator().generateUUID()
) {

    constructor() : this(
        name = "Anonymous User",
        projects = mutableListOf(),
        isAnonymous = true,
        id = UUIDGenerator().generateUUID()
    )

    companion object {
        /// For testing, provide a generic user
        val testUser: YkUser
            get() {
                val user = YkUser()
                user.projects.add(wembyProject())
                user.projects.add(spaceProject())
                return user
            }

        fun fromJsonString(jsonString: String): YkUser {
            return Json.decodeFromString<YkUser>(jsonString)
        }
    }

    fun asJsonString(): String {
        return Json { prettyPrint = true }.encodeToString(this)
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
        projects.indexOfFirst { project == it }.let { idx ->
            projects.removeAt(idx)
        }
    }
}
