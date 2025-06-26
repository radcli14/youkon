package viewmodel

import Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import org.jetbrains.compose.resources.StringArrayResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.allStringArrayResources
import youkon.composeapp.generated.resources.onboarding_editable_project_content
import youkon.composeapp.generated.resources.onboarding_editable_project_header
import youkon.composeapp.generated.resources.onboarding_project_view_content
import youkon.composeapp.generated.resources.onboarding_project_view_header
import youkon.composeapp.generated.resources.onboarding_projects_content
import youkon.composeapp.generated.resources.onboarding_projects_header
import youkon.composeapp.generated.resources.onboarding_quick_convert_content
import youkon.composeapp.generated.resources.onboarding_quick_convert_header
import youkon.composeapp.generated.resources.onboarding_thank_you_content
import youkon.composeapp.generated.resources.onboarding_thank_you_header
import youkon.composeapp.generated.resources.onboarding_welcome_content
import youkon.composeapp.generated.resources.onboarding_welcome_header

class OnboardingScreenViewModel : ViewModel() {
    private val tag = "OnboardingScreenViewModel"

    val mainViewModel = MainViewModel()
    private val projectsCardViewModel = mainViewModel.projectsCardViewModel.value
    val quickConvertCardViewModel = QuickConvertCardViewModel()

    init {
        Log.d(tag, "initialized an OnboardingScreenViewModel")
    }

    /// Reset the onboarding state to its initial state
    fun resetOnboarding() {
        Log.d(tag, "resetting onboarding state")
        mainViewModel.stopEditing()
        currentPage.intValue = 0
        currentText.intValue = 0
    }

    private val helps = arrayOf(
        Triple(Res.string.onboarding_welcome_header, Res.array.onboarding_welcome_content, 1),
        Triple(Res.string.onboarding_quick_convert_header, Res.array.onboarding_quick_convert_content, 5),
        Triple(Res.string.onboarding_projects_header, Res.array.onboarding_projects_content, 3),
        Triple(Res.string.onboarding_project_view_header, Res.array.onboarding_project_view_content, 4),
        Triple(Res.string.onboarding_editable_project_header, Res.array.onboarding_editable_project_content, 5),
        Triple(Res.string.onboarding_thank_you_header, Res.array.onboarding_thank_you_content, 2),
    )

    private val helpHeaderResource: StringResource get() = helps[currentPage.intValue].first
    val helpHeader: String
        @Composable
        get() = stringResource(helpHeaderResource)

    private val helpContentResource: StringArrayResource get() = helps[currentPage.intValue].second
    private val helpContent: List<String>
        @Composable
        get() = stringArrayResource(helpContentResource).toList()

    val helpText: String
        @Composable
        get() = helpContent[currentText.intValue]

    val lastHelpIndex = helps.count() - 1

    private val lastTextIndex: Int
        get() = helps[currentPage.intValue].third - 1

    var currentPage = mutableIntStateOf(0)
    var currentText = mutableIntStateOf(0)

    fun incrementPage() {
        Log.d(tag, "Incrementing onboarding page\n  From: ${currentPage.intValue}-${currentText.intValue}")
        if (onLastBeforeExit) {
            resetOnboarding()
        } else if (onLastText){
            currentPage.intValue += 1
            currentText.intValue = 0
        } else {
            currentText.intValue += 1
        }
        updateHighlight()
        Log.d(tag, "  To: ${currentPage.intValue}-${currentText.intValue}")
    }

    fun updateHighlight() {
        quickConvertCardViewModel.highlight(if (currentPage.intValue == 1) currentText.intValue else null)
        projectsCardViewModel.highlightInProjectsCard(if (currentPage.intValue == 2) currentText.intValue else null)
        val projectViewModel = projectsCardViewModel.projectViewModel()
        when(currentPage.intValue) {
            3 -> projectViewModel.highlightInProjectView(currentText.intValue)
            4 -> {
                if (!mainViewModel.isEditingProject.value) {
                    mainViewModel.startEditing(projectViewModel.project.value)
                }
                projectViewModel.highlightInEditableView(currentText.intValue)
            }
            else -> projectViewModel.highlight(null)
        }
        if (currentPage.intValue != 4) {
            mainViewModel.stopEditing()
        }
    }

    private val onLastPage: Boolean get() = currentPage.intValue >= lastHelpIndex

    val onLastText: Boolean
        get() = currentText.intValue >= lastTextIndex

    val onLastBeforeExit: Boolean
        get() = onLastPage && onLastText

    val navButtonDescription: String
        get() {
            return when (onLastPage && onLastText) {
                false -> "Move to the next item"
                true -> "Exit the onboarding screen"
            }
        }

    var isWide = mutableStateOf(true)
    val scale: Float get() = if (isWide.value) 0.69f else 0.6f
    val width: Dp get() = if (isWide.value) 880.dp else 200.dp
    val height = 720.dp
    val dialogFillRatio: Float get() = if (isWide.value) 0.75f else 0.9f

    val onboardTextHeight = 160.dp

    val textIsAboveScaledMainView: Boolean
        get() = currentPage.value < 2

    fun updateIsWide(windowWidth: Dp, windowHeight: Dp) {
        isWide.value = windowWidth > (windowHeight * 0.6f)
        Log.d(tag, "isWide = $isWide $windowWidth $windowHeight")
    }
}
