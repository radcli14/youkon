import kotlinx.coroutines.flow.Flow
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

