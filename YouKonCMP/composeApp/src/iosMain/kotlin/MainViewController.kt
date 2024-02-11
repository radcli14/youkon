import androidx.compose.ui.window.ComposeUIViewController
import viewmodel.MainViewModel
import viewmodel.OnboardingScreenViewModel
import viewmodel.QuickConvertCardViewModel

private val storage = Storage()
private val mainViewModel = MainViewModel(storage)
private val quickConvertCardViewModel = QuickConvertCardViewModel(storage)
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
