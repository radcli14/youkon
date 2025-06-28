package view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowForward
import androidx.compose.material.icons.twotone.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import theming.fullWidthSemitransparentPadded
import viewmodel.OnboardingScreenViewModel

class OnboardingScreen(
    private val viewModel: OnboardingScreenViewModel = OnboardingScreenViewModel(),
) {
    @Composable
    fun Body(navController: NavHostController? = null) {

        // Update if tablet (isWide == true)
        val windowInfo = LocalWindowInfo.current
        LaunchedEffect(windowInfo.containerSize) {
            viewModel.updateIsWide(
                windowInfo.containerSize.width.dp,
                windowInfo.containerSize.height.dp
            )
        }

        // Wrap everything in a box to manage placement of the tabs at the bottom
        Box(modifier = Modifier.fullWidthSemitransparentPadded().fillMaxHeight()) {

            // Wrap the text and the scaled view in a column so they are always stacked
            Column(
                modifier = Modifier.padding(16.dp).padding(bottom = 56.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AnimatedVisibility(viewModel.textIsAboveScaledMainView) {
                    OnboardText()
                }

                ScaledMainView(modifier = Modifier.weight(1f))

                AnimatedVisibility(!viewModel.textIsAboveScaledMainView) {
                    OnboardText()
                }
            }

            // Indicators of position in the onboarding sequence
            Tabs(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.69f)
                    .padding(16.dp)
                    .clip(MaterialTheme.shapes.large)
            )

            // Action button to navigate forward one screen, or exit
            NavButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                navController = navController
            )
        }
    }

    /// Shows two lines of text at the top or bottom, with the title of the current screen, and some helpful text
    @Composable
    fun OnboardText(modifier: Modifier = Modifier) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .requiredHeightIn(min = viewModel.onboardTextHeight),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = modifier.padding(16.dp)) {
                AnimatedContent(viewModel.helpHeader) { helpHeader ->
                    Text(helpHeader,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                AnimatedContent(viewModel.helpText) { helpText ->
                    Text(helpText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }

    /// A row of tabs at the bottom of the screen
    @Composable
    fun Tabs(modifier: Modifier = Modifier, onChangeTab: () -> Unit = {}) {
        TabRow(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            selectedTabIndex = viewModel.currentPage.intValue,
        ) {
            for (index in 0 .. viewModel.lastHelpIndex) {
                Tab(selected = index == viewModel.currentPage.intValue, onClick = {
                    viewModel.currentPage.intValue = index
                    viewModel.currentText.intValue = 0
                    viewModel.updateHighlight()
                }, modifier = Modifier.padding(16.dp)) {
                    TabIcon(index == viewModel.currentPage.intValue)
                }
            }
        }
        onChangeTab()
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

    /// The color of a tab, dependent on whether it is selected
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
            modifier = modifier,
            shape = MaterialTheme.shapes.large,
            shadowElevation = 8.dp,
            border = BorderStroke(4.dp, MaterialTheme.colorScheme.primaryContainer)
        ) {
            CompositionLocalProvider(
                LocalDensity provides Density(
                    density = LocalDensity.current.density * viewModel.scale
                )
            ) {
                MainView(onboardingScreenViewModel = viewModel).Body()

                // This box sits on top of the view, and is here to disable user input
                Box(Modifier.fillMaxSize().pointerInput(Unit) {})
            }
        }
    }

    /// The button to navigate to the next onboarding screen
    @Composable
    fun NavButton(
        modifier: Modifier = Modifier,
        navController: NavHostController?
    ) {
        FloatingActionButton(
            modifier = modifier.padding(8.dp),
            onClick = {
                viewModel.incrementPage()
                if (viewModel.onLastBeforeExit) {
                    navController?.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                    viewModel.resetOnboarding()
                }
            }
        ) {
            Icon(navButtonIcon(),
                contentDescription = viewModel.navButtonDescription,
            )
        }
    }

    @Composable
    fun navButtonIcon(): ImageVector {
        return if (viewModel.onLastBeforeExit) Icons.TwoTone.Check else Icons.AutoMirrored.TwoTone.ArrowForward
    }
}

