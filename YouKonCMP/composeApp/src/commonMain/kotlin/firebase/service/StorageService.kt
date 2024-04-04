package firebase.service

import AccountService
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import model.YkProject
import trace

interface StorageService {
    val projects: Flow<List<YkProject>>
    suspend fun getProject(projectId: String): YkProject?
    suspend fun save(project: YkProject): String
    suspend fun update(project: YkProject)
    suspend fun delete(projectId: String)
}


class StorageServiceImpl(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService
) : StorageService {
    /*
    @OptIn(ExperimentalCoroutinesApi::class)
    override val projects: Flow<List<YkProject>>
        get() =
            auth.currentUser.flatMapLatest { user ->
                firestore.collection(PROJECT_COLLECTION) //.whereEqualTo(USER_ID_FIELD, user.id).dataObjects()
            }

    override suspend fun getProject(projectId: String): YkProject? =
        firestore.collection(PROJECT_COLLECTION).document(projectId).get() //.await().toObject()
*/
    override val projects: Flow<List<YkProject>>
        get() = callbackFlow {
            //TODO("Not yet implemented")
        }

    override suspend fun getProject(projectId: String): YkProject? {
        //TODO("Not yet implemented")
        return null
    }

    override suspend fun save(project: YkProject): String =
        trace(SAVE_PROJECT_TRACE) {
            val projectWithUserId = project //.copy(userId = auth.currentUserId)
            //firestore.collection(PROJECT_COLLECTION).add(projectWithUserId).id //.await().id
            "TODO: Not Yet Implemented"
        }

    override suspend fun update(project: YkProject): Unit =
        trace(UPDATE_PROJECT_TRACE) {
            //firestore.collection(PROJECT_COLLECTION).document(project.id).set(project) //.await()
        }

    override suspend fun delete(taskId: String) {
        firestore.collection(PROJECT_COLLECTION).document(taskId).delete() //.await()
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val PROJECT_COLLECTION = "projects"
        private const val SAVE_PROJECT_TRACE = "saveProject"
        private const val UPDATE_PROJECT_TRACE = "updateProject"
    }
}
