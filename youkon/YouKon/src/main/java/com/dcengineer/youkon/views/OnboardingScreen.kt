package com.dcengineer.youkon.views

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

class OnboardingScreen(
    val onDismissRequest: () -> Unit = {}
) {
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

    private var helpTextOffsets = arrayOf(0.dp, 0.dp, 360.dp, 360.dp, 360.dp)
    private var mainScreenOffsets = arrayOf(0.dp, 128.dp, (-256).dp, (-256).dp, (-256).dp)

    @Composable
    fun Body() {
        var currentPage by remember { mutableIntStateOf(0) }
        val helpTextOffset by animateDpAsState(
            targetValue = helpTextOffsets[currentPage]
        )
        val mainScreenOffset by animateDpAsState(
            targetValue = mainScreenOffsets[currentPage]
        )
        Column(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.large
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .padding(16.dp)
                    .clipToBounds()
            ) {
                OnboardText(currentPage,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(y = helpTextOffset)
                )
                ScaledMainView(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(y = mainScreenOffset)
                )
                NavButton(
                    Modifier.align(Alignment.BottomEnd),
                    currentPage
                ) {
                    if (currentPage < helps.count() - 1) {
                        currentPage += 1
                    } else {
                        currentPage = 0
                        onDismissRequest()
                    }
                }
            }

            Tabs(currentPage) {
                currentPage = it
            }
        }
    }

    @Composable
    fun AsDialog() {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Body()
        }
    }

    /// Shows two lines of text at the top, with the title of the current screen, and some helpful text
    @Composable
    fun OnboardText(currentPage: Int, modifier: Modifier = Modifier) {
        Column(modifier = modifier.padding(16.dp)) {
            Text(helps[currentPage].first,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(helps[currentPage].second,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }

    /// A row of tabs at the bottom of the screen
    @Composable
    fun Tabs(currentPage: Int, onTabSelected: (Int) -> Unit) {
        TabRow(
            modifier = Modifier.fillMaxWidth(0.7f),
            selectedTabIndex = currentPage,
        ) {
            helps.forEachIndexed { index, _ ->
                Tab(selected = index == currentPage, onClick = {
                    onTabSelected(index)
                }, modifier = Modifier.padding(16.dp)) {
                    TabIcon(index == currentPage)
                }
            }
        }
    }

    /// The icon of a tab, with appearance dependent on whether the tab is selected
    @Composable
    fun TabIcon(isCurrent: Boolean) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    color = tabColor(isCurrent),
                    shape = RoundedCornerShape(4.dp)
                )
        )
    }

    /// The color of a tab, dependent on wheter it is selected
    @Composable
    fun tabColor(isCurrent: Boolean): Color {
        return if (isCurrent) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.primaryContainer
        }
    }

    /// The entire app screen, scaled down to fit in a onboarding dialog
    @Composable
    fun ScaledMainView(modifier: Modifier = Modifier) {
        Surface(
            modifier = modifier.scale(0.69f),
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 8.dp,
            border = BorderStroke(4.dp, MaterialTheme.colorScheme.primaryContainer)
        ) {
            MainView().MainContentStack()
        }
    }

    /// The button to navigate to the next onboarding screen
    @Composable
    fun NavButton(
        modifier: Modifier = Modifier,
        currentPage: Int = 0,
        onClick: () -> Unit
    ) {
        FloatingActionButton(
            modifier = modifier.padding(16.dp),
            onClick = { onClick() }
        ) {
            Icon(navButtonIcon(currentPage), "Move to the next item.")
        }
    }

    @Composable
    fun navButtonIcon(currentPage: Int): ImageVector {
        return if (currentPage < helps.count() - 1) Icons.Default.NavigateNext else Icons.Default.Check
    }
}

@Preview
@Composable
fun OnboardingPreview() {
    OnboardingScreen().Body()
}
