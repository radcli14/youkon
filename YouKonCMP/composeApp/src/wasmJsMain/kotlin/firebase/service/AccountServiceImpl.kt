package firebase.service

import AccountService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import model.YkUser

class AccountServiceImpl : AccountService {
    override val currentUserId: String = ""
    override val currentUserName: String = ""
    override val hasUser: Boolean = false
    override val currentUser: Flow<YkUser> = flowOf(YkUser())

    override suspend fun authenticate(email: String, password: String) { /* no-op */ }
    override suspend fun sendRecoveryEmail(email: String) { /* no-op */ }
    override suspend fun createAnonymousAccount() { /* no-op */ }
    override suspend fun createUser(email: String, password: String) { /* no-op */ }
    override suspend fun linkAccount(email: String, password: String) { /* no-op */ }
    override suspend fun deleteAccount() { /* no-op */ }
    override suspend fun signOut() { /* no-op */ }
} 