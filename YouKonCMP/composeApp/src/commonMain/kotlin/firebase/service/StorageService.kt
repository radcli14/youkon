package firebase.service

import AccountService
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override val projects: Flow<List<YkProject>>
        get() =
            auth.currentUser.flatMapLatest { user ->
                firestore.collection(PROJECT_COLLECTION)
                    .where { USER_ID_FIELD equalTo user.id }
                    .snapshots
                    .map { snapshot ->
                        snapshot.documents.map { document ->
                            document.data(YkProject.serializer())
                        }
                    }
            }

    override suspend fun getProject(projectId: String): YkProject? =
        firestore.collection(PROJECT_COLLECTION).document(projectId).get().data(YkProject.serializer())

    override suspend fun save(project: YkProject): String =
        trace(SAVE_PROJECT_TRACE) {
            firestore.collection(PROJECT_COLLECTION).add(project).id
        }

    override suspend fun update(project: YkProject): Unit =
        trace(UPDATE_PROJECT_TRACE) {
            firestore.collection(PROJECT_COLLECTION).document(project.id).set(project)
        }

    override suspend fun delete(taskId: String) {
        firestore.collection(PROJECT_COLLECTION).document(taskId).delete()
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val USER_DATA_COLLECTION = "userData"
        private const val PROJECT_COLLECTION = "projects"
        private const val SAVE_PROJECT_TRACE = "saveProject"
        private const val UPDATE_PROJECT_TRACE = "updateProject"
    }
}
