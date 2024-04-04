import androidx.compose.runtime.Composable
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import firebase.service.LogServiceImpl
import firebase.service.StorageServiceImpl
import view.MainView
import firebase.login.LoginViewModel
import viewmodel.MainViewModel
import viewmodel.OnboardingScreenViewModel
import viewmodel.QuickConvertCardViewModel
import firebase.settings.SettingsViewModel

@Composable
fun App(
    mainViewModel: MainViewModel = MainViewModel(),
    quickConvertCardViewModel: QuickConvertCardViewModel = QuickConvertCardViewModel(),
    onboardingScreenViewModel: OnboardingScreenViewModel = OnboardingScreenViewModel(),
    loginViewModel: LoginViewModel = LoginViewModel(
        AccountServiceImpl(Firebase.auth),
        LogServiceImpl()
    ),
    settingsViewModel: SettingsViewModel = SettingsViewModel(
        LogServiceImpl(),
        AccountServiceImpl(Firebase.auth),
        StorageServiceImpl(Firebase.firestore, AccountServiceImpl(Firebase.auth))
    )
) {
    MainView(
        mainViewModel,
        quickConvertCardViewModel,
        onboardingScreenViewModel,
        loginViewModel,
        settingsViewModel
    ).Body()
}