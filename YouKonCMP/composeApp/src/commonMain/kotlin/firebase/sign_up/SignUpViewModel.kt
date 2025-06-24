package firebase.sign_up

import AccountService
import LaunchCatchingViewModel
import SETTINGS_SCREEN
import SIGN_UP_SCREEN
import androidx.compose.runtime.mutableStateOf
import firebase.service.LogService
import isValidEmail
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import firebase.login.MessageType
import firebase.login.UserMessage
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.account_created_successfully
import youkon.composeapp.generated.resources.email_error
import youkon.composeapp.generated.resources.password_error
import youkon.composeapp.generated.resources.password_match_error
import youkon.composeapp.generated.resources.generic_error


data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val isLoading: Boolean = false
)


class SignUpViewModel(
    private val accountService: AccountService,
    logService: LogService
) : LaunchCatchingViewModel(logService) {
    var uiState = mutableStateOf(SignUpUiState())
        private set

    private val _message = MutableStateFlow<UserMessage?>(null)
    val message: StateFlow<UserMessage?> = _message.asStateFlow()

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password
    private val repeatPassword
        get() = uiState.value.repeatPassword

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
        clearMessage()
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
        clearMessage()
    }

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
        clearMessage()
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            _message.value = UserMessage(Res.string.email_error, MessageType.ERROR)
            return
        }

        if (password.isBlank()) {
            _message.value = UserMessage(Res.string.password_error, MessageType.ERROR)
            return
        }

        if (password != repeatPassword) {
            _message.value = UserMessage(Res.string.password_match_error, MessageType.ERROR)
            return
        }

        uiState.value = uiState.value.copy(isLoading = true)
        launchCatching(snackbar = false) {
            try {
                accountService.createUser(email, password)
                _message.value = UserMessage(Res.string.account_created_successfully, MessageType.SUCCESS)
                openAndPopUp(SETTINGS_SCREEN, SIGN_UP_SCREEN)
            } catch (e: Exception) {
                _message.value = UserMessage(Res.string.generic_error, MessageType.ERROR)
            } finally {
                uiState.value = uiState.value.copy(isLoading = false)
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
