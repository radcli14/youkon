import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.EmailAuthProvider
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.perf.metrics.Trace
import dev.gitlive.firebase.perf.performance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import model.YkUser

interface AccountService {
    val currentUserId: String
    val currentUserName: String
    val hasUser: Boolean

    val currentUser: Flow<YkUser>

    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun createAnonymousAccount()
    suspend fun createUser(email: String, password: String)
    suspend fun linkAccount(email: String, password: String)
    suspend fun deleteAccount()
    suspend fun signOut()
}


class AccountServiceImpl(private val auth: FirebaseAuth) : AccountService {
    private val tag = "AccountServiceImpl"

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val currentUserName: String
        get() = auth.currentUser?.email ?: "Anonymous User"

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override val currentUser: Flow<YkUser>
        get() = auth.authStateChanged.map { firebaseUser ->
            firebaseUser?.let { it ->
                YkUser(
                    name = it.email ?: "Anonymous User",
                    id = it.uid,
                    isAnonymous = it.isAnonymous
                )
            } ?: YkUser()
        }
        /*get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { YkUser(it.uid, it.isAnonymous) } ?: User())
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }*/

    override suspend fun authenticate(email: String, password: String) {
        val result = auth.signInWithEmailAndPassword(email, password) //.await()
        Log.d(tag, "authenticate result = ${result.user?.email ?: "null"} ${result.user?.uid}")
    }

    override suspend fun sendRecoveryEmail(email: String) {
        auth.sendPasswordResetEmail(email) //.await()
    }

    override suspend fun createAnonymousAccount() {
        auth.signInAnonymously() //.await()
    }

    override suspend fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
    }

    override suspend fun linkAccount(email: String, password: String) {
        trace(LINK_ACCOUNT_TRACE) {
            val credential = EmailAuthProvider.credential(email, password)
            auth.currentUser!!.linkWithCredential(credential)//.await()
        }
    }

    override suspend fun deleteAccount() {
        auth.currentUser!!.delete()//.await()
    }

    override suspend fun signOut() {
        if (auth.currentUser!!.isAnonymous) {
            auth.currentUser!!.delete()
        }
        auth.signOut()

        // Sign the user back in anonymously.
        createAnonymousAccount()
    }

    companion object {
        private const val LINK_ACCOUNT_TRACE = "linkAccount"
    }
}

/**
 * Trace a block with Firebase performance.
 *
 * Supports both suspend and regular methods.
 */
inline fun <T> trace(name: String, block: Trace.() -> T): T {
    // Former implementation was: Trace.create(name).trace(block)
    val myTrace = Firebase.performance.newTrace(name)
    myTrace.start()
    val result = block(myTrace)
    myTrace.stop()
    return result
}
