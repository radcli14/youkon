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
    suspend fun getUser(userId: String, email: String? = null): YkUser
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
                    .collection(USER_DATA_COLLECTION).document(user.id)
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

    private fun projectCollection(userId: String): CollectionReference {
        if (userId.isEmpty()) {
            Log.e(tag, "Attempted to access project collection with empty userId. Cannot proceed.")
            throw IllegalArgumentException("User ID cannot be empty for Firestore document path.")
        }
        return firestore
            .collection(USER_DATA_COLLECTION).document(userId)
            .collection(PROJECT_COLLECTION)
    }

    override suspend fun getProject(user: YkUser, projectId: String): YkProject =
        getProject(user.id, projectId)

    private suspend fun getProject(userId: String, projectId: String): YkProject {
        if (projectId.isEmpty()) {
            Log.e(tag, "Attempted to fetch project with empty projectId for user $userId")
            throw IllegalArgumentException("Project ID cannot be empty")
        }
        return projectCollection(userId)
            .document(projectId)
            .get().data(YkProject.serializer())
    }

    private suspend fun userCollectionDocument(userId: String): DocumentSnapshot? =
        try {
            val document = firestore.collection(USER_DATA_COLLECTION).document(userId).get()
            Log.d(tag, "userCollectionDocument document = ${document}")
            document
        } catch(e: NoSuchElementException) {
            Log.d(tag, "Failed getting user data from storage service for $userId, NoSuchElementException was ${e.message}")
            null
        } catch(e: FirebaseFirestoreException) {
            Log.d(tag, "Failed getting user data from storage service for $userId, FirebaseFirestoreException was ${e.message}")
            null
        }

    override suspend fun userExists(userId: String): Boolean =
        userCollectionDocument(userId)?.exists ?: false

    override suspend fun getUser(userId: String, email: String?): YkUser {
        Log.d(tag, "getUser called for userId: $userId")
        try {
            val userDoc = userCollectionDocument(userId)
            if (userDoc == null) {
                Log.e(tag, "No user document found for userId: $userId")
                return YkUser(name = email ?: "Anonymous User", projects = mutableListOf(), isAnonymous = true, id = userId)
            }

            var name = userDoc.data(YkUser.Compact.serializer())?.name ?: ""
            if (name.isBlank()) {
                Log.e(tag, "User document for userId: $userId has blank or missing name. Falling back to email or 'Anonymous User'.")
                name = email ?: "Anonymous User"
            }
            Log.d(tag, "Found user document with name: $name")

            val projects = getUserProjects(userId)
            Log.d(tag, "Retrieved ${projects.size} projects for user")

            return YkUser(name, projects, isAnonymous = name == "Anonymous User", id = userId)
        } catch (e: Exception) {
            Log.e(tag, "Error getting user data: ${e.message}")
            return YkUser(name = email ?: "Anonymous User", projects = mutableListOf(), isAnonymous = true, id = userId)
        }
    }

    override suspend fun getUserProjects(userId: String): MutableList<YkProject> {
        Log.d(tag, "getUserProjects called for userId: $userId")
        try {
            val userDoc = userCollectionDocument(userId)
            if (userDoc == null) {
                Log.e(tag, "No user document found for userId: $userId to get projects from.")
                return mutableListOf()
            }
            val userNameForPath = userDoc.data(YkUser.Compact.serializer())?.name

            return firestore.collection(USER_DATA_COLLECTION)
                .document(userId)
                .collection(PROJECT_COLLECTION)
                .get()
                .documents
                .mapNotNull { document ->
                    document.data(YkProject.serializer())
                }.toMutableList()
        } catch (e: Exception) {
            Log.e(tag, "Error getting user projects: ${e.message}")
            return mutableListOf()
        }
    }

    override suspend fun save(user: YkUser, project: YkProject): String =
        trace(SAVE_PROJECT_TRACE) {
            update(user, project)
            "update project from the save() function, TODO: don't do this"
        }

    override suspend fun save(user: YkUser): String =
        trace(SAVE_USER_TRACE) {
            update(user)
            "update user from the save() function, TODO: don't do this, "
        }

    override suspend fun update(user: YkUser, project: YkProject): Unit =
        trace(UPDATE_PROJECT_TRACE) {
            val projectWithUserId = if (project.userId == user.id) project else project.copy(userId = user.id)
            Log.d(tag, "Saving/updating project \${project.id} for user \${user.id} with projectUserId \${projectWithUserId.userId}")
            firestore.collection(USER_DATA_COLLECTION).document(user.id).collection(PROJECT_COLLECTION).document(project.id).set(projectWithUserId)
        }

    override suspend fun update(user: YkUser): Unit =
        trace(UPDATE_USER_TRACE) {
            Log.d(tag, "Saving/updating user \${user.id}")
            firestore.collection(USER_DATA_COLLECTION).document(user.id).set(user.compact)
            user.projects.forEach { project ->
                update(user, project)
            }
        }

    override suspend fun delete(user: YkUser, projectId: String) {
        projectCollection(user.id).document(projectId).delete()
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
