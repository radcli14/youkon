import androidx.compose.ui.window.ComposeUIViewController
import viewmodel.MainViewModel
import viewmodel.OnboardingScreenViewModel
import viewmodel.ProjectsCardViewModel
import viewmodel.QuickConvertCardViewModel

fun MainViewController() = ComposeUIViewController {
    val mainViewModel = MainViewModel()
    val quickConvertCardViewModel = QuickConvertCardViewModel()
    val onboardingScreenViewModel = OnboardingScreenViewModel()
    App(
        mainViewModel,
        quickConvertCardViewModel,
        onboardingScreenViewModel
    )
}
