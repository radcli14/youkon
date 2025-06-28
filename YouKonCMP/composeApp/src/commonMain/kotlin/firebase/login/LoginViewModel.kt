package firebase.login

import AccountService
import navigation.LOGIN_SCREEN
import LaunchCatchingViewModel
import navigation.SETTINGS_SCREEN
import androidx.compose.runtime.mutableStateOf
import isValidEmail
import firebase.service.LogService
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.email_error
import youkon.composeapp.generated.resources.empty_password_error
import youkon.composeapp.generated.resources.generic_error
import youkon.composeapp.generated.resources.logged_in_successfully
import youkon.composeapp.generated.resources.recovery_email_sent

enum class MessageType {
    SUCCESS, ERROR
}

data class UserMessage(
    val text: StringResource,
    val type: MessageType
)

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false
)

class LoginViewModel(
    private val accountService: AccountService,
    logService: LogService
) : LaunchCatchingViewModel(logService) {
    var uiState = mutableStateOf(LoginUiState())
        private set

    private val _message = MutableStateFlow<UserMessage?>(null)
    val message: StateFlow<UserMessage?> = _message.asStateFlow()

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
        clearMessage()
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
        clearMessage()
    }

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            _message.value = UserMessage(Res.string.email_error, MessageType.ERROR)
            return
        }

        if (password.isBlank()) {
            _message.value = UserMessage(Res.string.empty_password_error, MessageType.ERROR)
            return
        }

        uiState.value = uiState.value.copy(isLoading = true)
        launchCatching {
            try {
                accountService.authenticate(email, password)
                _message.value = UserMessage(Res.string.logged_in_successfully, MessageType.SUCCESS)
                openAndPopUp(SETTINGS_SCREEN, LOGIN_SCREEN)
            } catch (e: Exception) {
                _message.value = UserMessage(Res.string.generic_error, MessageType.ERROR)
            } finally {
                uiState.value = uiState.value.copy(isLoading = false)
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    fun onForgotPasswordClick() {
        if (!email.isValidEmail()) {
            _message.value = UserMessage(Res.string.email_error, MessageType.ERROR)
            return
        }

        launchCatching {
            accountService.sendRecoveryEmail(email)
            _message.value = UserMessage(Res.string.recovery_email_sent, MessageType.SUCCESS)
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
