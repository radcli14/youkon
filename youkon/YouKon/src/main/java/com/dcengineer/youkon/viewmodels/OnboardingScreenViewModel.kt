package com.dcengineer.youkon.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.lifecycle.ViewModel

class OnboardingScreenViewModel: ViewModel() {
    private val tag = "OnboardingScreenViewModel"

    private val helps = arrayOf(
    "Welcome" to
            "Thank you for trying the unit converter app designed for engineers",
    "Quick Convert Card" to
            "1. Tap the `From` button in the upper left to select from a list of all available units in the app\n" +
            "2. Tap the `To` button in the upper right to select from a list of units that may be converted given the `From` unit\n" +
            "3. Enter a number in the lower left for a value in the `From` unit\n" +
            "4. The converted value and unit are displayed in the lower right",
    "Projects Card" to
            "Here you may store all of your projects, each with multiple measurements, all converted to a consistent system of units\n" +
            "1. Tap the `Plus` (+) button to add a new project\n" +
            "2. Tap the `Minus` (-) button to enable subtracting a project, then tap the `X` button alongside to delete that project",
    "Project View" to
            "1. Tap once on a project to expand it to show its list of measurements, and select a system of units\n" +
            "2. Tap on the list of measurements to open the editing screen for that project",
    "Editable Project" to
            "The bottom sheet expands to show a menu where you may modify data in this project\n" +
            "1. Name and description fields at the top are used to edit the header and labels for this project\n" +
            "2. `Plus` (+) and `Minus` (-) are used to add and subtract measurements\n" +
            "3. Each measurement has its own name and description field\n" +
            "4. Number value and a unit may be selected for each measurement"
    )
    fun helpHeader(): String {
        return helps[currentPage.intValue].first
    }
    fun helpContent(): String {
        return helps[currentPage.intValue].second
    }
    val lastHelpIndex = helps.count() - 1

    var currentPage = mutableIntStateOf(0)
    fun incrementPage() {
        Log.d(tag, "Incrementing onboarding page\n  From: ${currentPage.intValue}")
        if (onLastPage()) {
            currentPage.intValue = 0
        } else {
            currentPage.intValue += 1
        }
        Log.d(tag, "  To: ${currentPage.intValue}")
    }
    fun onLastPage(): Boolean {
        return currentPage.intValue >= lastHelpIndex
    }

    fun navButtonDescription(): String {
        return when (onLastPage()) {
            false -> "Move to the next item"
            true -> "Exit the onboarding screen"
        }
    }

    var isWide = true
    val scale = if (isWide) 0.6f else 0.69f
    val width = if (isWide) 880.dp else 400.dp
    val height = 720.dp
    val dialogFillRatio = if (isWide) 0.75f else 0.9f

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
                    top.linkTo(text.bottom, (-64).dp)
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
                     bottom.linkTo(parent.bottom)
                 }

                 constrain(main) {
                     bottom.linkTo(text.top, (-64).dp)
                 }

                 constrain(nav) {
                     end.linkTo(parent.end)
                     bottom.linkTo(parent.bottom)
                 }
             }
        }
    }
}