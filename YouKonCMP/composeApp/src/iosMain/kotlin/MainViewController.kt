import androidx.compose.ui.window.ComposeUIViewController
import viewmodel.MainViewModel
import viewmodel.OnboardingScreenViewModel
import viewmodel.QuickConvertCardViewModel

private val mainViewModel = MainViewModel()
private val quickConvertCardViewModel = QuickConvertCardViewModel()
private val onboardingScreenViewModel = OnboardingScreenViewModel()

fun MainViewController() = ComposeUIViewController {
    // TODO: set the isWide variable based on detecting screen geometry
    onboardingScreenViewModel.isWide = false

    App(
        mainViewModel,
        quickConvertCardViewModel,
        onboardingScreenViewModel
    )
}
