package firebase.service

import AccountService
import Log
import dev.gitlive.firebase.firestore.CollectionReference
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

    suspend fun getProject(user: YkUser, projectId: String): YkProject?
    suspend fun userExists(userId: String): Boolean
    suspend fun getUser(userId: String): YkUser?
    suspend fun getUserProjects(userId: String): MutableList<YkProject>
    suspend fun save(user: YkUser, project: YkProject): String
    suspend fun save(user: YkUser): String
    suspend fun update(user: YkUser, project: YkProject)
    suspend fun update(user: YkUser)
    suspend fun delete(user: YkUser, projectId: String)
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
                firestore
                    .collection(USER_DATA_COLLECTION).document(user.name)
                    .collection(PROJECT_COLLECTION)
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

    private fun projectCollection(userName: String): CollectionReference =
        firestore
            .collection(USER_DATA_COLLECTION).document(userName)
            .collection(PROJECT_COLLECTION)

    override suspend fun getProject(user: YkUser, projectId: String): YkProject =
        getProject(user.name, projectId)

    private suspend fun getProject(userName: String, projectId: String): YkProject =
        projectCollection(userName)
            .document(projectId)
            .get().data(YkProject.serializer())

    private suspend fun userCollectionDocument(userId: String): DocumentSnapshot? =
        try {
            val documents = firestore.collection(USER_DATA_COLLECTION)
                .where { ID_FIELD equalTo userId }
                .get().documents
            println("userCollectionDocument documents = $documents")
            documents.last()
        } catch(e: NoSuchElementException) {
            Log.d(tag, "Failed getting user data from storage service, error was ${e.message}")
            null
        } catch(e: FirebaseFirestoreException) {
            Log.d(tag, "Failed getting user data from storage service, error was ${e.message}")
            null
        }

    override suspend fun userExists(userId: String): Boolean =
        userCollectionDocument(userId)?.exists ?: false

    override suspend fun getUser(userId: String): YkUser {
        Log.d(tag, "getUser called for userId: $userId")
        try {
            val userDoc = userCollectionDocument(userId)
            if (userDoc == null) {
                Log.e(tag, "No user document found for userId: $userId")
                return YkUser(name = "", projects = mutableListOf(), isAnonymous = true, id = userId)
            }

            val name = userDoc.data(YkUser.Compact.serializer())?.name ?: ""
            Log.d(tag, "Found user document with name: $name")
            
            val projects = getUserProjects(userId)
            Log.d(tag, "Retrieved ${projects.size} projects for user")
            
            return YkUser(name, projects, isAnonymous = name.isEmpty(), userId)
        } catch (e: Exception) {
            Log.e(tag, "Error getting user data: ${e.message}")
            return YkUser(name = "", projects = mutableListOf(), isAnonymous = true, id = userId)
        }
    }

    override suspend fun getUserProjects(userId: String): MutableList<YkProject> {
        Log.d(tag, "getUserProjects called for userId: $userId")
        try {
            val userDoc = userCollectionDocument(userId)
            if (userDoc == null) {
                Log.e(tag, "No user document found for userId: $userId")
                return mutableListOf()
            }

            val compactUser = userDoc.data(YkUser.Compact.serializer())
            if (compactUser == null) {
                Log.e(tag, "Could not parse user document for userId: $userId")
                return mutableListOf()
            }

            Log.d(tag, "Found ${compactUser.projectIds.size} project IDs for user")
            return compactUser.projectIds.mapNotNull { projectId ->
                try {
                    getProject(compactUser.name, projectId)
                } catch (e: Exception) {
                    Log.e(tag, "Error getting project $projectId: ${e.message}")
                    null
                }
            }.toMutableList()
        } catch (e: Exception) {
            Log.e(tag, "Error getting user projects: ${e.message}")
            return mutableListOf()
        }
    }

    override suspend fun save(user: YkUser, project: YkProject): String =
        trace(SAVE_PROJECT_TRACE) {
            //firestore.collection(PROJECT_COLLECTION).add(project).id
            update(user, project)
            "update project from the save() function, TODO: don't do this"
        }

    override suspend fun save(user: YkUser): String =
        trace(SAVE_USER_TRACE) {
            //firestore.collection(USER_DATA_COLLECTION).add(user).id
            update(user)
            "update user from the save() function, TODO: don't do this, "
        }

    override suspend fun update(user: YkUser, project: YkProject): Unit =
        trace(UPDATE_PROJECT_TRACE) {
            projectCollection(user.name).document(project.id).set(project)
        }

    override suspend fun update(user: YkUser): Unit =
        trace(UPDATE_USER_TRACE) {
            firestore.collection(USER_DATA_COLLECTION).document(user.name).set(user.compact)
            user.projects.forEach { project ->
                update(user, project)
            }
            /*userCollectionDocument(user.id)?.let {
                firestore.collection(USER_DATA_COLLECTION).document(it.id).set(user)
            }*/
        }

    override suspend fun delete(user: YkUser, projectId: String) {
        projectCollection(user.name).document(projectId).delete()
        //firestore.collection(PROJECT_COLLECTION).document(projectId).delete()
    }

    override suspend fun delete(user: YkUser) {
        firestore.collection(USER_DATA_COLLECTION).document(user.id).delete()
    }

    companion object {
        private const val ID_FIELD = "id"
        private const val USER_ID_FIELD = "userId"
        private const val USER_DATA_COLLECTION = "users"
        private const val PROJECT_COLLECTION = "projects"
        private const val SAVE_PROJECT_TRACE = "saveProject"
        private const val SAVE_USER_TRACE = "saveUser"
        private const val UPDATE_PROJECT_TRACE = "updateProject"
        private const val UPDATE_USER_TRACE = "updateUser"
    }
}
