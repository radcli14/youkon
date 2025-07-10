package firebase.service

import kotlinx.coroutines.flow.Flow
import model.YkProject
import model.YkUser

interface StorageService {
    val projects: Flow<List<YkProject>>
    val user: Flow<YkUser>

    suspend fun getProject(user: YkUser, projectId: String): YkProject?
    suspend fun userExists(userId: String): Boolean
    suspend fun getUser(userId: String, email: String? = null): YkUser
    suspend fun getUserProjects(userId: String): MutableList<YkProject>
    suspend fun save(user: YkUser, project: YkProject): String
    suspend fun save(user: YkUser): String
    suspend fun update(user: YkUser, project: YkProject)
    suspend fun update(user: YkUser)
    suspend fun delete(user: YkUser, projectId: String)
    suspend fun delete(user: YkUser)
}
