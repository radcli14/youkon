package com.dcengineer.youkon.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

class OnboardingScreen {
    private val screens = arrayOf(
        "Welcome",
        "Quick Convert Card",
        "Projects Card",
        "Project View",
        "Editable Project"
    )

    @Composable
    fun Body() {
        var currentPage by remember { mutableIntStateOf(0) }
        Column {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.large
                    )
            ) {
                Text(screens[currentPage],
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                ScaledMainView()

                NavButton(Modifier.align(Alignment.BottomEnd)) {
                    currentPage = if (currentPage < 4) currentPage + 1 else 0
                }
            }

            Tabs(currentPage) { currentPage = it }
        }
    }

    @Composable
    fun AsDialog() {
        Dialog(onDismissRequest = {}) {
            Body()
        }
    }

    /// A row of tabs at the bottom of the screen
    @Composable
    fun Tabs(currentPage: Int, onTabSelected: (Int) -> Unit) {
        TabRow(
            selectedTabIndex = currentPage,
        ) {
            screens.forEachIndexed { index, _ ->
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
    fun ScaledMainView() {
        Surface(
            modifier = Modifier.scale(0.69f),
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 8.dp,
            border = BorderStroke(4.dp, MaterialTheme.colorScheme.primaryContainer)
        ) {
            MainView().MainContentStack()
        }
    }

    /// The button to navigate to the next onboarding screen
    @Composable
    fun NavButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
        FloatingActionButton(
            modifier = modifier
                .padding(16.dp),
            onClick = { onClick() }
        ) {
            Icon(Icons.Default.NavigateNext, "Move to the next item.")
        }
    }
}

@Preview
@Composable
fun OnboardingPreview() {
    OnboardingScreen().Body()
}
