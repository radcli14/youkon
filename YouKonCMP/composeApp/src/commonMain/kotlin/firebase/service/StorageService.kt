package firebase.service

import AccountService
import Log
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import model.YkProject
import model.YkUser
import trace

interface StorageService {
    val projects: Flow<List<YkProject>>
    val user: Flow<YkUser>

    suspend fun getProject(projectId: String): YkProject?
    suspend fun userExists(userId: String): Boolean
    suspend fun getUser(userId: String): YkUser?
    suspend fun save(project: YkProject): String
    suspend fun save(user: YkUser): String
    suspend fun update(project: YkProject)
    suspend fun update(user: YkUser)
    suspend fun delete(projectId: String)
    suspend fun delete(user: YkUser)
}


class StorageServiceImpl(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService
) : StorageService {
    private val tag = "StorageServiceImpl"

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

    @OptIn(ExperimentalCoroutinesApi::class)
    override val user: Flow<YkUser>
        get() = auth.currentUser.flatMapLatest { user ->
            firestore.collection(USER_DATA_COLLECTION)
                .where { ID_FIELD equalTo user.id}
                .snapshots.map { snapshot ->
                    snapshot.documents.first().data(YkUser.serializer())
                }
        }

    override suspend fun getProject(projectId: String): YkProject? =
        firestore.collection(PROJECT_COLLECTION).document(projectId).get().data(YkProject.serializer())

    private suspend fun userCollectionDocument(userId: String): DocumentSnapshot? =
        try {
            firestore.collection(USER_DATA_COLLECTION)
                .where { ID_FIELD equalTo userId }
                .get().documents.last()
        } catch(e: NoSuchElementException) {
            Log.d(tag, "Failed getting user data from storage service, error was ${e.message}")
            null
        } catch(e: FirebaseFirestoreException) {
            Log.d(tag, "Failed getting user data from storage service, error was ${e.message}")
            null
        }

    override suspend fun userExists(userId: String): Boolean =
        userCollectionDocument(userId)?.exists ?: false

    override suspend fun getUser(userId: String): YkUser? =
        userCollectionDocument(userId)?.data(YkUser.serializer())

    override suspend fun save(project: YkProject): String =
        trace(SAVE_PROJECT_TRACE) {
            firestore.collection(PROJECT_COLLECTION).add(project).id
        }

    override suspend fun save(user: YkUser): String =
        trace(SAVE_PROJECT_TRACE) {
            firestore.collection(USER_DATA_COLLECTION).add(user).id
        }

    override suspend fun update(project: YkProject): Unit =
        trace(UPDATE_PROJECT_TRACE) {
            firestore.collection(PROJECT_COLLECTION).document(project.id).set(project)
        }

    override suspend fun update(user: YkUser): Unit =
        trace(UPDATE_PROJECT_TRACE) {
            userCollectionDocument(user.id)?.let {
                firestore.collection(USER_DATA_COLLECTION).document(it.id).set(user)
            }
        }

    override suspend fun delete(projectId: String) {
        firestore.collection(PROJECT_COLLECTION).document(projectId).delete()
    }

    override suspend fun delete(user: YkUser) {
        firestore.collection(USER_DATA_COLLECTION).document(user.id).delete()
    }

    companion object {
        private const val ID_FIELD = "id"
        private const val USER_ID_FIELD = "userId"
        private const val USER_DATA_COLLECTION = "userData"
        private const val PROJECT_COLLECTION = "projects"
        private const val SAVE_PROJECT_TRACE = "saveProject"
        private const val UPDATE_PROJECT_TRACE = "updateProject"
    }
}
