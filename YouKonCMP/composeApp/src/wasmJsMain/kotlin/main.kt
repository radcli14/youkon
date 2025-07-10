import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import firebase.login.LoginViewModel
import firebase.service.AccountServiceImpl
import firebase.service.LogServiceImpl
import firebase.service.StorageServiceImpl
import firebase.settings.SettingsViewModel
import firebase.sign_up.SignUpViewModel
import kotlinx.browser.document
import viewmodel.MainViewModel
import viewmodel.OnboardingScreenViewModel
import viewmodel.QuickConvertCardViewModel

private val logService = LogServiceImpl()
private val accountService = AccountServiceImpl()
private val localStorage = Storage()
private val cloudStorage = StorageServiceImpl()

private val mainViewModel = MainViewModel(accountService, localStorage, cloudStorage)
private val quickConvertCardViewModel = QuickConvertCardViewModel(localStorage)
private val onboardingScreenViewModel = OnboardingScreenViewModel()
private val settingsViewModel = SettingsViewModel(logService, accountService, cloudStorage)
private val loginViewModel = LoginViewModel(accountService, logService)
private val signUpViewModel = SignUpViewModel(accountService, logService)


@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        App(
            mainViewModel,
            quickConvertCardViewModel,
            onboardingScreenViewModel,
            loginViewModel,
            settingsViewModel,
            signUpViewModel
        )
    }
}
