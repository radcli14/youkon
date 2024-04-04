import androidx.compose.ui.window.ComposeUIViewController
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import firebase.service.LogServiceImpl
import firebase.service.StorageServiceImpl
import firebase.login.LoginViewModel
import viewmodel.MainViewModel
import viewmodel.OnboardingScreenViewModel
import viewmodel.QuickConvertCardViewModel
import firebase.settings.SettingsViewModel

private val storage = Storage()
private val mainViewModel = MainViewModel(storage)
private val quickConvertCardViewModel = QuickConvertCardViewModel(storage)
private val onboardingScreenViewModel = OnboardingScreenViewModel()
private val logService = LogServiceImpl()
private val accountService = AccountServiceImpl(Firebase.auth)
private val storageService = StorageServiceImpl(Firebase.firestore, accountService)
private val settingsViewModel = SettingsViewModel(logService, accountService, storageService)
private val loginViewModel = LoginViewModel(accountService, logService)

fun MainViewController() = ComposeUIViewController {
    // TODO: set the isWide variable based on detecting screen geometry
    onboardingScreenViewModel.isWide = false

    App(
        mainViewModel,
        quickConvertCardViewModel,
        onboardingScreenViewModel,
        loginViewModel,
        settingsViewModel
    )
}
