package firebase.settings

import AccountService
import LOGIN_SCREEN
import LaunchCatchingViewModel
import SIGN_UP_SCREEN
import SPLASH_SCREEN
import kotlinx.coroutines.flow.map
import firebase.service.LogService
import firebase.service.StorageService
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.account_deleted_successfully
import youkon.composeapp.generated.resources.signed_out_successfully
import firebase.login.MessageType
import firebase.login.UserMessage

data class SettingsUiState(
    val name: String,
    val isAnonymousAccount: Boolean = true
)

class SettingsViewModel(
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService
) : LaunchCatchingViewModel(logService) {
    val uiState = accountService.currentUser.map {
        SettingsUiState(it.name, it.isAnonymous)
    }

    private val _message = MutableStateFlow<UserMessage?>(null)
    val message: StateFlow<UserMessage?> = _message.asStateFlow()

    fun onLoginClick(openScreen: (String) -> Unit) = openScreen(LOGIN_SCREEN)

    fun onSignUpClick(openScreen: (String) -> Unit) = openScreen(SIGN_UP_SCREEN)

    @OptIn(ExperimentalResourceApi::class)
    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            _message.value = UserMessage(Res.string.signed_out_successfully, MessageType.SUCCESS)
            restartApp(SPLASH_SCREEN)
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.deleteAccount()
            _message.value = UserMessage(Res.string.account_deleted_successfully, MessageType.SUCCESS)
            restartApp(SPLASH_SCREEN)
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
