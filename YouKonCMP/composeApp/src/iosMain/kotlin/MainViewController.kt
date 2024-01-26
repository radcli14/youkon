import androidx.compose.ui.window.ComposeUIViewController
import viewmodel.MainViewModel
import viewmodel.OnboardingScreenViewModel
import viewmodel.ProjectsCardViewModel
import viewmodel.QuickConvertCardViewModel

fun MainViewController() = ComposeUIViewController {
    val mainViewModel = MainViewModel()
    val projectsCardViewModel = ProjectsCardViewModel(user = mainViewModel.user)
    App(
        mainViewModel,
        QuickConvertCardViewModel(),
        projectsCardViewModel,
        OnboardingScreenViewModel()
    )
}
