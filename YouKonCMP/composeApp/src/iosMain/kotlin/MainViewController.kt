import androidx.compose.ui.window.ComposeUIViewController
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import firebase.service.LogServiceImpl
import firebase.service.StorageServiceImpl
import firebase.login.LoginViewModel
import firebase.service.AccountServiceImpl
import viewmodel.MainViewModel
import viewmodel.OnboardingScreenViewModel
import viewmodel.QuickConvertCardViewModel
import firebase.settings.SettingsViewModel
import firebase.sign_up.SignUpViewModel

private val logService = LogServiceImpl()
private val accountService = AccountServiceImpl(Firebase.auth)
private val localStorage = Storage()
private val cloudStorage = StorageServiceImpl(Firebase.firestore, accountService)

private val mainViewModel = MainViewModel(accountService, localStorage, cloudStorage)
private val quickConvertCardViewModel = QuickConvertCardViewModel(localStorage)
private val onboardingScreenViewModel = OnboardingScreenViewModel()
private val settingsViewModel = SettingsViewModel(logService, accountService, cloudStorage)
private val loginViewModel = LoginViewModel(accountService, logService)
private val signUpViewModel = SignUpViewModel(accountService, logService)

fun MainViewController() = ComposeUIViewController {
    App(
        mainViewModel,
        quickConvertCardViewModel,
        onboardingScreenViewModel,
        loginViewModel,
        settingsViewModel,
        signUpViewModel
    )
}
