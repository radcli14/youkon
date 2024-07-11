package model
import UUIDGenerator
import kotlinx.serialization.*
import kotlinx.serialization.json.*

/// Holds the user's name and multiple projects
@Serializable
data class YkUser(
    var name: String = "Anonymous User",
    var projects: MutableList<YkProject> = mutableListOf(),
    var isAnonymous: Boolean = true,
    var id: String = UUIDGenerator().generateUUID()
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
        val project = YkProject(
            name = name,
            about = description,
            userId = id
        )
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

    /// Move a project in the up or down direction
    fun moveProject(project: YkProject, direction: String) {
        val idx = projects.indexOf(project)
        val toIndex = when {
            direction == "up" && idx > 0 -> idx - 1
            direction == "down" && idx < projects.count()-1 -> idx + 1
            else -> null
        }
        toIndex?.let { newIdx -> moveProject(project, newIdx) }
    }

    /// Move a project to a new index
    fun moveProject(project: YkProject, toIndex: Int) {
        val idx = projects.indexOf(project)
        projects.removeAt(idx)
        projects.add(toIndex, project)
    }

    /// List of project ID's to maintain their order in the cloud database
    val projectIds: List<String> get() = projects.map { project -> project.id }

    /// List of project names to use in a shortened print
    private val projectNames: List<String> get() = projects.map { project -> project.name }

    /// Shortened string for this user showing just its name and list of project names
    val summary: String get() = "name: $name, projectNames: $projectNames"

    /// Compact form of this user for storage in the cloud
    val compact: Compact get() = Compact(name, id, projectNames, projectIds)

    @Serializable
    data class Compact(
        val name: String,
        val id: String,
        val projectNames: List<String>,
        val projectIds: List<String>
    )
}

