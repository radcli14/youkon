import androidx.compose.ui.window.ComposeUIViewController
import viewmodel.MainViewModel
import viewmodel.OnboardingScreenViewModel
import viewmodel.QuickConvertCardViewModel

private val mainViewModel = MainViewModel()
private val quickConvertCardViewModel = QuickConvertCardViewModel()
private val onboardingScreenViewModel = OnboardingScreenViewModel()

fun MainViewController() = ComposeUIViewController {
    App(
        mainViewModel,
        quickConvertCardViewModel,
        onboardingScreenViewModel
    )
}
