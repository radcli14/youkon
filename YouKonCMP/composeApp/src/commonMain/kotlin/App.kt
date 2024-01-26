import androidx.compose.runtime.Composable
import view.MainView
import viewmodel.MainViewModel
import viewmodel.OnboardingScreenViewModel
import viewmodel.ProjectsCardViewModel
import viewmodel.QuickConvertCardViewModel

@Composable
fun App(
    mainViewModel: MainViewModel = MainViewModel(),
    quickConvertCardViewModel: QuickConvertCardViewModel = QuickConvertCardViewModel(),
    projectsCardViewModel: ProjectsCardViewModel = ProjectsCardViewModel(),
    onboardingScreenViewModel: OnboardingScreenViewModel = OnboardingScreenViewModel()
) {
    MainView(
        mainViewModel,
        quickConvertCardViewModel,
        projectsCardViewModel,
        onboardingScreenViewModel
    ).Body()
}