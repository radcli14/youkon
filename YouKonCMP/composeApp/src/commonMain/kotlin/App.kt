import androidx.compose.runtime.Composable
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import model.LogServiceImpl
import view.MainView
import viewmodel.LoginViewModel
import viewmodel.MainViewModel
import viewmodel.OnboardingScreenViewModel
import viewmodel.QuickConvertCardViewModel

@Composable
fun App(
    mainViewModel: MainViewModel = MainViewModel(),
    quickConvertCardViewModel: QuickConvertCardViewModel = QuickConvertCardViewModel(),
    onboardingScreenViewModel: OnboardingScreenViewModel = OnboardingScreenViewModel(),
    loginViewModel: LoginViewModel = LoginViewModel(AccountServiceImpl(Firebase.auth), LogServiceImpl())
) {
    MainView(
        mainViewModel,
        quickConvertCardViewModel,
        onboardingScreenViewModel,
        loginViewModel
    ).Body()
}