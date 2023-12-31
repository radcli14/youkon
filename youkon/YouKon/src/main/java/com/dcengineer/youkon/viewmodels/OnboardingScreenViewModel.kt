package com.dcengineer.youkon.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.lifecycle.ViewModel

class OnboardingScreenViewModel: ViewModel() {
    private val tag = "OnboardingScreenViewModel"
    val showOnboarding = mutableStateOf(false)

    val mainViewModel = MainViewModel(loadDefault = true)
    val projectsCardViewModel = ProjectsCardViewModel(mainViewModel.user)
    val quickConvertCardViewModel = QuickConvertCardViewModel()

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
            "1. Tap once on a project to expand it to show its list of measurements, and select a system of units",
            "2. Tap on the list of measurements to open the editing screen for that project"
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
        Log.d(tag, "  To: ${currentPage.intValue}-${currentText.intValue}")
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

    private val constraintChangeIndex = 1
    fun constraints(): ConstraintSet {
        return if (currentPage.intValue <= constraintChangeIndex) {
            ConstraintSet {
                val text = createRefFor("text")
                val main = createRefFor("main")
                val nav = createRefFor("nav")

                constrain(text) {
                    top.linkTo(parent.top)
                }

                constrain(main) {
                    top.linkTo(text.bottom, (-96).dp)
                }

                constrain(nav) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            }
        } else {
             ConstraintSet {
                 val text = createRefFor("text")
                 val main = createRefFor("main")
                 val nav = createRefFor("nav")

                 constrain(text) {
                     bottom.linkTo(nav.top)
                 }

                 constrain(main) {
                     bottom.linkTo(text.top, (-56).dp)
                 }

                 constrain(nav) {
                     end.linkTo(parent.end)
                     bottom.linkTo(parent.bottom)
                 }
             }
        }
    }
}