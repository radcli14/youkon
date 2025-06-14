package view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import getPlatform
import kotlinx.coroutines.delay
import viewmodel.OnboardingScreenViewModel
import androidx.navigation.NavHostController

class OnboardingScreen(
    private val viewModel: OnboardingScreenViewModel = OnboardingScreenViewModel(),
) {
    @Composable
    fun Body(navController: NavHostController? = null) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
                .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.69f),
                shape = MaterialTheme.shapes.large
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var verticalOffsetForMainView by remember {
                mutableStateOf(viewModel.mainViewVerticalOffset)
            }
            val offsetForMainView by animateDpAsState(verticalOffsetForMainView)

            Box(modifier = Modifier
                //.fillMaxSize()
                .weight(1f)
            ) {
                ScaledMainView(
                    Modifier
                        .align(Alignment.TopCenter)
                        .offset(
                            x = if ("iOS" in getPlatform().name) (-12).dp else 0.dp,
                            y = offsetForMainView
                        )
                )
                OnboardText(
                    Modifier
                        .align(viewModel.onboardTextAlign)
                        .offset(y = viewModel.onboardTextOffset)
                )
                NavButton(
                    Modifier.align(Alignment.BottomEnd)
                ) {
                    verticalOffsetForMainView = viewModel.mainViewVerticalOffset
                    viewModel.incrementPage()
                    if (viewModel.onLastBeforeExit) {
                        navController?.navigate("main") {
                            popUpTo("main") { inclusive = true }
                        }
                        viewModel.resetOnboarding()
                    }
                }
                Tabs(Modifier.align(Alignment.BottomCenter)) {
                    verticalOffsetForMainView = viewModel.mainViewVerticalOffset
                }
            }
        }
    }

    /// Shows two lines of text at the top, with the title of the current screen, and some helpful text
    //@SuppressLint("UnusedContentLambdaTargetStateParameter")
    @Composable
    fun OnboardText(modifier: Modifier = Modifier) {
        // The header text shows up at the top, and updates when the currentPage changes
        var helpHeader by remember { mutableStateOf(viewModel.helpHeader) }
        var helpHeaderVisible by remember { mutableStateOf(true) }
        val helpHeaderAlpha by animateFloatAsState(
            targetValue = if (helpHeaderVisible) 1f else 0f,
            animationSpec = tween(durationMillis = viewModel.navTransitionTime),
            label = ""
        )
        LaunchedEffect(viewModel.helpHeader) {
            helpHeaderVisible = false
            delay(viewModel.navTransitionTime.toLong())
            helpHeader = viewModel.helpHeader
            helpHeaderVisible = true
        }

        // The help text is the secondary text, and updates when the currentPage or currentText changes
        var helpText by remember { mutableStateOf(viewModel.helpText) }
        var helpTextVisible by remember { mutableStateOf(true) }
        val helpTextAlpha by animateFloatAsState(
            targetValue = if (helpTextVisible) 1f else 0f,
            animationSpec = tween(durationMillis = 100 + viewModel.navTransitionTime),
            label = ""
        )
        LaunchedEffect(viewModel.helpText) {
            helpTextVisible = false
            delay(100 + viewModel.navTransitionTime.toLong())
            helpText = viewModel.helpText
            helpTextVisible = true
        }

        Column(
            modifier = modifier
                .padding(16.dp)
                .height(viewModel.onboardTextHeight)
        ) {
            Text(helpHeader,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.alpha(helpHeaderAlpha)
            )
            Text(helpText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.alpha(helpTextAlpha)
            )
        }
    }

    /// A row of tabs at the bottom of the screen
    @Composable
    fun Tabs(modifier: Modifier = Modifier, onChangeTab: () -> Unit = {}) {
        TabRow(
            modifier = modifier
                .fillMaxWidth(0.69f)
                .padding(16.dp)
                .clip(MaterialTheme.shapes.large),
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
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
            modifier = modifier
                .scale(viewModel.scale)
                .requiredSize(viewModel.width, viewModel.height),
            shape = MaterialTheme.shapes.large,
            shadowElevation = 8.dp,
            border = BorderStroke(4.dp, MaterialTheme.colorScheme.primaryContainer)
        ) {
            MainView(onboardingScreenViewModel = viewModel).Body()

            // This box sits on top of the view, and is here to disable user input
            Box(Modifier.fillMaxSize().pointerInput(Unit) {})
        }
    }

    /// The button to navigate to the next onboarding screen
    @Composable
    fun NavButton(
        modifier: Modifier = Modifier,
        onNavigate: () -> Unit = {}
    ) {
        FloatingActionButton(
            modifier = modifier.padding(8.dp),
            onClick = onNavigate
        ) {
            Icon(navButtonIcon(),
                contentDescription = viewModel.navButtonDescription,
                modifier = Modifier.size(40.dp)
            )
        }
    }

    @Composable
    fun navButtonIcon(): ImageVector {
        return if (viewModel.onLastBeforeExit) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward
    }
}

