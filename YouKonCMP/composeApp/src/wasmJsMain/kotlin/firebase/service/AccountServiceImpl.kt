package firebase.service

import AccountService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import model.YkUser

class AccountServiceImpl : AccountService {
    override val currentUserId: String = "wasm-user-id"
    override val currentUserName: String = "Web User"
    override val hasUser: Boolean = true
    override val currentUser: Flow<YkUser> = flowOf(
        YkUser(
            name = currentUserName,
            id = currentUserId,
            isAnonymous = false
        )
    )

    override suspend fun authenticate(email: String, password: String) { /* no-op */ }
    override suspend fun sendRecoveryEmail(email: String) { /* no-op */ }
    override suspend fun createAnonymousAccount() { /* no-op */ }
    override suspend fun createUser(email: String, password: String) { /* no-op */ }
    override suspend fun linkAccount(email: String, password: String) { /* no-op */ }
    override suspend fun deleteAccount() { /* no-op */ }
    override suspend fun signOut() { /* no-op */ }
} 