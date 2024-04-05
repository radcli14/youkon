package firebase.settings

import AccountService
import LOGIN_SCREEN
import LaunchCatchingViewModel
import SIGN_UP_SCREEN
import SPLASH_SCREEN
import kotlinx.coroutines.flow.map
import firebase.service.LogService
import firebase.service.StorageService

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

    fun onLoginClick(openScreen: (String) -> Unit) = openScreen(LOGIN_SCREEN)

    fun onSignUpClick(openScreen: (String) -> Unit) = openScreen(SIGN_UP_SCREEN)

    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            restartApp(SPLASH_SCREEN)
        }
    }

    fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.deleteAccount()
            restartApp(SPLASH_SCREEN)
        }
    }
}