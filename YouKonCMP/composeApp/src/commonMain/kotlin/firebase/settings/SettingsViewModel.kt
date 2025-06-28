package firebase.settings

import AccountService
import navigation.LOGIN_SCREEN
import LaunchCatchingViewModel
import navigation.SIGN_UP_SCREEN
import navigation.SPLASH_SCREEN
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

    private val _message = MutableStateFlow<StringResource?>(null)
    val message: StateFlow<StringResource?> = _message.asStateFlow()

    fun onLoginClick(openScreen: (String) -> Unit) = openScreen(LOGIN_SCREEN)

    fun onSignUpClick(openScreen: (String) -> Unit) = openScreen(SIGN_UP_SCREEN)

    @OptIn(ExperimentalResourceApi::class)
    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            _message.value = Res.string.signed_out_successfully
            restartApp(SPLASH_SCREEN)
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.deleteAccount()
            _message.value = Res.string.account_deleted_successfully
            restartApp(SPLASH_SCREEN)
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
