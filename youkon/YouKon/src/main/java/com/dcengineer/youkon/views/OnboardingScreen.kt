package com.dcengineer.youkon.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.layoutId
import com.dcengineer.youkon.viewmodels.OnboardingScreenViewModel

class OnboardingScreen(
    private val viewModel: OnboardingScreenViewModel = OnboardingScreenViewModel(),
) {
    @Composable
    fun Body() {
        Column(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.large
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConstraintLayout(
                viewModel.constraints(),
                modifier = Modifier
                    .fillMaxHeight(viewModel.dialogFillRatio)
                    .padding(viewModel.constraintPadding)
                    .clipToBounds(),
                animateChanges = true
            ) {
                OnboardText(Modifier.layoutId("text"))
                ScaledMainView(Modifier.layoutId("main"))
                NavButton(Modifier.layoutId("nav"))
            }

            Tabs()
        }
    }

    @Composable
    fun AsDialog() {
        if (viewModel.showOnboarding.value) {
            Dialog(onDismissRequest = { viewModel.closeOnboarding() }) {
                Body()
            }
        }
    }

    /// Shows two lines of text at the top, with the title of the current screen, and some helpful text
    @Composable
    fun OnboardText(modifier: Modifier = Modifier) {
        Column(
            modifier = modifier.padding(16.dp).height(96.dp)
        ) {
            Text(viewModel.helpHeader,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(viewModel.helpText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }

    /// A row of tabs at the bottom of the screen
    @Composable
    fun Tabs() {
        TabRow(
            modifier = Modifier.fillMaxWidth(0.7f),
            selectedTabIndex = viewModel.currentPage.intValue,
        ) {
            for (index in 0 .. viewModel.lastHelpIndex) {
                Tab(selected = index == viewModel.currentPage.intValue, onClick = {
                    viewModel.currentPage.intValue = index
                    viewModel.currentText.intValue = 0
                }, modifier = Modifier.padding(16.dp)) {
                    TabIcon(index == viewModel.currentPage.intValue)
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
            modifier = modifier
                .scale(viewModel.scale)
                .requiredSize(viewModel.width, viewModel.height),
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 8.dp,
            border = BorderStroke(4.dp, MaterialTheme.colorScheme.primaryContainer)
        ) {
            MainView().BottomSheetLayout()
        }
    }

    /// The button to navigate to the next onboarding screen
    @Composable
    fun NavButton(
        modifier: Modifier = Modifier
    ) {
        FloatingActionButton(
            modifier = modifier.padding(16.dp),
            onClick = { viewModel.incrementPage() }
        ) {
            Icon(navButtonIcon(), viewModel.navButtonDescription)
        }
    }

    @Composable
    fun navButtonIcon(): ImageVector {
        return if (viewModel.onLastBeforeExit) Icons.Default.Check else Icons.Default.NavigateNext
    }

}

@Preview
@Composable
fun OnboardingPreview() {
    val viewModel = OnboardingScreenViewModel()
    viewModel.isWide = false
    OnboardingScreen(viewModel).Body()
}
