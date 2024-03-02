package viewmodel

import androidx.compose.runtime.mutableStateOf
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import isValidEmail
import kotlinx.coroutines.flow.Flow
import model.YkUser
import view.SnackbarManager
import view.SnackbarMessage

data class LoginUiState(
    val email: String = "",
    val password: String = ""
)

class LoginViewModel(
    private val accountService: AccountService,
    //logService: LogService
) : ViewModel() {
    var uiState = mutableStateOf(LoginUiState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("TODO: get email error resource")) //AppText.email_error)
            return
        }

        if (password.isBlank()) {
            SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("TODO: get password error resource")) //AppText.empty_password_error)
            return
        }

        /*
        launchCatching {
            accountService.authenticate(email, password)
            openAndPopUp(SETTINGS_SCREEN, LOGIN_SCREEN)
        }
         */
    }

    fun onForgotPasswordClick() {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("TODO: get email error resource")) // AppText.email_error)
            return
        }

        /*
        launchCatching {
            accountService.sendRecoveryEmail(email)
            SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("TODO: get recovery email resource")) //AppText.recovery_email_sent)
        }
         */
    }
}

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean

    val currentUser: Flow<YkUser>

    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun createAnonymousAccount()
    suspend fun linkAccount(email: String, password: String)
    suspend fun deleteAccount()
    suspend fun signOut()
}
