package firebase.sign_up

import AccountService
import LaunchCatchingViewModel
import SETTINGS_SCREEN
import SIGN_UP_SCREEN
import androidx.compose.runtime.mutableStateOf
import firebase.service.LogService
import isValidEmail
import isValidPassword
import org.jetbrains.compose.resources.ExperimentalResourceApi
import passwordMatches
import view.SnackbarManager
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.email_error
import youkon.composeapp.generated.resources.password_error
import youkon.composeapp.generated.resources.password_match_error


data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = ""
)


class SignUpViewModel(
    private val accountService: AccountService,
    logService: LogService
) : LaunchCatchingViewModel(logService) {
    var uiState = mutableStateOf(SignUpUiState())
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

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    @OptIn(ExperimentalResourceApi::class)
    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(Res.string.email_error)
            return
        }

        if (!password.isValidPassword()) {
            SnackbarManager.showMessage(Res.string.password_error)
            return
        }

        if (!password.passwordMatches(uiState.value.repeatPassword)) {
            SnackbarManager.showMessage(Res.string.password_match_error)
            return
        }

        launchCatching {
            accountService.linkAccount(email, password)
            openAndPopUp(SETTINGS_SCREEN, SIGN_UP_SCREEN)
        }
    }
}
