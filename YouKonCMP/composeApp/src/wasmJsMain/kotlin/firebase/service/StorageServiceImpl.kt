package firebase.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import model.YkProject
import model.YkUser

class StorageServiceImpl : StorageService {
    override val projects: Flow<List<YkProject>> = flowOf(emptyList())
    override val user: Flow<YkUser> = flowOf(YkUser())

    override suspend fun getProject(user: YkUser, projectId: String): YkProject? = null
    override suspend fun userExists(userId: String): Boolean = false
    override suspend fun getUser(userId: String, email: String?): YkUser = YkUser()
    override suspend fun getUserProjects(userId: String): MutableList<YkProject> = mutableListOf()
    override suspend fun save(user: YkUser, project: YkProject): String = ""
    override suspend fun save(user: YkUser): String = ""
    override suspend fun update(user: YkUser, project: YkProject) { /* no-op */ }
    override suspend fun update(user: YkUser) { /* no-op */ }
    override suspend fun delete(user: YkUser, projectId: String) { /* no-op */ }
    override suspend fun delete(user: YkUser) { /* no-op */ }
} 