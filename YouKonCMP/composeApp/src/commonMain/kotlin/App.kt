import androidx.compose.runtime.Composable
import view.MainView
import firebase.login.LoginViewModel
import viewmodel.MainViewModel
import viewmodel.OnboardingScreenViewModel
import viewmodel.QuickConvertCardViewModel
import firebase.settings.SettingsViewModel
import firebase.sign_up.SignUpViewModel
import purchases.PurchasesViewModel

@Composable
fun App(
    mainViewModel: MainViewModel = MainViewModel(),
    quickConvertCardViewModel: QuickConvertCardViewModel = QuickConvertCardViewModel(),
    onboardingScreenViewModel: OnboardingScreenViewModel = OnboardingScreenViewModel(),
    loginViewModel: LoginViewModel? = null,
    settingsViewModel: SettingsViewModel? = null,
    signUpViewModel: SignUpViewModel? = null,
    purchasesViewModel: PurchasesViewModel = PurchasesViewModel(),
    context: Any? = null,
) {
    MainView(
        mainViewModel,
        quickConvertCardViewModel,
        onboardingScreenViewModel,
        loginViewModel,
        settingsViewModel,
        signUpViewModel,
        purchasesViewModel,
        context
    ).Body()
}
