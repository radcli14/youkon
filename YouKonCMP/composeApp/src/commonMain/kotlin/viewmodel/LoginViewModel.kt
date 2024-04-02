package viewmodel

import AccountService
import LOGIN_SCREEN
import LaunchCatchingViewModel
import SETTINGS_SCREEN
import androidx.compose.runtime.mutableStateOf
import isValidEmail
import model.LogService
import org.jetbrains.compose.resources.ExperimentalResourceApi
import view.SnackbarManager
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.email_error
import youkon.composeapp.generated.resources.empty_password_error
import youkon.composeapp.generated.resources.recovery_email_sent

data class LoginUiState(
    val email: String = "",
    val password: String = ""
)

class LoginViewModel(
    private val accountService: AccountService,
    logService: LogService
) : LaunchCatchingViewModel(logService) {
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

    @OptIn(ExperimentalResourceApi::class)
    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(Res.string.email_error)
            return
        }

        if (password.isBlank()) {
            SnackbarManager.showMessage(Res.string.empty_password_error)
            return
        }

        launchCatching {
            accountService.authenticate(email, password)
            openAndPopUp(SETTINGS_SCREEN, LOGIN_SCREEN)
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    fun onForgotPasswordClick() {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(Res.string.email_error)
            return
        }

        launchCatching {
            accountService.sendRecoveryEmail(email)
            SnackbarManager.showMessage(Res.string.recovery_email_sent)
        }
    }
}
