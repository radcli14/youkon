package viewmodel

import Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.viewmodel.ViewModel

class OnboardingScreenViewModel : ViewModel() {
    private val tag = "OnboardingScreenViewModel"
    val showOnboarding = mutableStateOf(false)

    val mainViewModel = MainViewModel(loadDefault = true)
    private val projectsCardViewModel = mainViewModel.projectsCardViewModel
    val quickConvertCardViewModel = QuickConvertCardViewModel()

    init {
        Log.d(tag, "initialized an OnboardingScreenViewModel")
    }

    /// Open the dialog containing the onboarding screen
    fun openOnboarding() {
        Log.d(tag, "open onboarding screen")
        showOnboarding.value = true
    }

    /// Close the dialog containing the onboarding screen
    fun closeOnboarding() {
        Log.d(tag, "closed onboarding screen")
        showOnboarding.value = false
    }

    private val helps = arrayOf(
        "Welcome" to arrayOf("Thank you for trying the unit converter app designed for engineers"),
        "Quick Convert Card" to arrayOf(
            "Here you may instantly convert a single measurement to an equivalent unit",
            "1. Tap the `From` button in the upper left to select from a list of all available units in the app",
            "2. Tap the `To` button in the upper right to select from a list of units that may be converted given the `From` unit",
            "3. Enter a number in the lower left for a value in the `From` unit",
            "4. The converted value and unit are displayed in the lower right"
        ),
        "Projects Card" to arrayOf(
            "Here you may store all of your projects, each with multiple measurements, all converted to a consistent system of units",
            "1. Tap the `Plus` (+) button to add a new project",
            "2. Tap the `Minus` (-) button to enable subtracting a project, then tap the `X` button alongside to delete that project"
        ),
        "Project View" to arrayOf(
            "Here you will view the name, description, and measurements for an individual project",
            "1. Tap once on a project to expand it to show its list of measurements, and select a system of units",
            "2. Use the picker to toggle between multiple systems of units",
            "3. Tap on the list of measurements to open the editing screen for that project"
        ),
        "Editable Project" to arrayOf(
            "The bottom sheet expands to show a menu where you may modify data in this project",
            "1. Name and description fields for this project are at the top",
            "2. `Plus` (+) and `Minus` (-) are used to add and subtract measurements",
            "3. Each measurement has its own name and description field",
            "4. Number value and a unit may be selected for each measurement"
        )
    )
    val helpHeader: String get() = helps[currentPage.intValue].first

    private val helpContent: Array<String> get() = helps[currentPage.intValue].second
    val helpText: String get() = helpContent[currentText.intValue]

    val lastHelpIndex = helps.count() - 1
    private val lastTextIndex: Int get() = helpContent.count() - 1

    var currentPage = mutableIntStateOf(0)
    var currentText = mutableIntStateOf(0)
    fun incrementPage() {
        Log.d(tag, "Incrementing onboarding page\n  From: ${currentPage.intValue}-${currentText.intValue}")
        if (onLastBeforeExit) {
            currentPage.intValue = 0
            currentText.intValue = 0
            closeOnboarding()
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
        projectsCardViewModel.highlight(if (currentPage.intValue == 2) currentText.intValue else null)
        val projectViewModel = projectsCardViewModel.projectViewModel()
        when(currentPage.intValue) {
            3 -> projectViewModel.highlightInProjectView(currentText.intValue)
            4 -> {
                Log.d(tag, "got to the editing page, mainViewModel.isEditingProject = ${mainViewModel.isEditingProject.value}")
                if (mainViewModel.isEditingProject.value != true) {
                    mainViewModel.toggleEdit(projectViewModel.project.value)
                }
                Log.d(tag, "  ${mainViewModel.isEditingProject.value}")
                projectViewModel.highlightInEditableView(currentText.intValue)
            }
            else -> projectViewModel.highlight(null)
        }

    }

    private val onLastPage: Boolean get() = currentPage.intValue >= lastHelpIndex

    private val onLastText: Boolean get() = currentText.intValue >= lastTextIndex

    val onLastBeforeExit: Boolean get() = onLastPage && onLastText

    val navButtonDescription: String
        get() {
            return when (onLastPage && onLastText) {
                false -> "Move to the next item"
                true -> "Exit the onboarding screen"
            }
        }

    val navTransitionTime = 250

    var isWide = true
    val scale: Float get() = if (isWide) 0.6f else 0.69f
    val width: Dp get() = if (isWide) 880.dp else 400.dp
    val height = 720.dp
    val dialogFillRatio: Float get() = if (isWide) 0.75f else 0.9f
    val constraintPadding: Dp get() = if (isWide) 16.dp else 0.dp

    val onboardTextHeight = 128.dp

    val onboardTextAlign: Alignment
        get() = if (currentPage.value < 2) Alignment.TopStart else Alignment.BottomStart

    val onboardTextOffset: Dp
        get() = if (currentPage.value < 2) 0.dp else (-64).dp

    val mainViewVerticalOffset: Dp
        get() = if (currentPage.value < 2) 64.dp else (-96).dp
}
