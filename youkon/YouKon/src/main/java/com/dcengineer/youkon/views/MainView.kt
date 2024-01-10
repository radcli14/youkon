package com.dcengineer.youkon.views

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcengineer.youkon.ProjectExpansionLevel
import com.dcengineer.youkon.YoukonTheme
import com.dcengineer.youkon.viewmodels.MainViewModel
import com.dcengineer.youkon.viewmodels.OnboardingScreenViewModel
import com.dcengineer.youkon.viewmodels.ProjectsCardViewModel
import com.dcengineer.youkon.viewmodels.QuickConvertCardViewModel
import kotlinx.coroutines.launch


class MainView(
    private var mainViewModel: MainViewModel = MainViewModel(),
    private var quickConvertCardViewModel: QuickConvertCardViewModel = QuickConvertCardViewModel(),
    private var projectsCardViewModel: ProjectsCardViewModel = ProjectsCardViewModel(),
    private var onboardingScreenViewModel: OnboardingScreenViewModel = OnboardingScreenViewModel()
) {

    /// Initialize using the fake view models inside the onboarding screen
    constructor(onboardingScreenViewModel: OnboardingScreenViewModel): this(
        onboardingScreenViewModel.mainViewModel,
        onboardingScreenViewModel.quickConvertCardViewModel,
        onboardingScreenViewModel.projectsCardViewModel,
        onboardingScreenViewModel
    )

    @Composable
    fun Body() {
        YoukonTheme {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                BottomSheetLayout()
                ActionButton(Modifier.align(Alignment.BottomEnd))
                Onboarding()
            }
        }
    }

    /// The onboarding screen will be shown on first app startup, or when user taps the help button
    @Composable
    fun Onboarding() {
        OnboardingScreen(onboardingScreenViewModel).AsDialog()
    }

    /// Holds state and the bottom sheet scaffold to allow the editing screen to appear
    /// as a sheet from the bottom of the screen.
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BottomSheetLayout() {
        // Set up the scaffold state such that it clears the project being edited from the
        // view model if the user drags the sheet downward. Will do the same if the user taps
        // the floating action button in the lower right.
        val scaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberModalBottomSheetState(
                confirmValueChange = {
                    if (it == SheetValue.Hidden) {
                        mainViewModel.stopEditing()
                    }
                    false
                }
            )
        )

        // React to changes in mainViewModel.isEditingProject by expanding or collapsing
        val scope = rememberCoroutineScope()
        val isBottomSheetExpanded by mainViewModel.isEditingProject.observeAsState()
        LaunchedEffect(isBottomSheetExpanded) {
            if (isBottomSheetExpanded == true) {
                scope.launch { scaffoldState.bottomSheetState.expand() }
            } else {
                scope.launch { scaffoldState.bottomSheetState.hide() }
            }
        }

        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = 360.dp,
            sheetContent = {
                ProjectEditingSheet()
            },
            modifier = Modifier.tapOutside()
        ) {
            MainContentStack()
        }
    }

    /// Modifier used to close the bottom sheet when tapping outside of it
    @SuppressLint("UnnecessaryComposedModifier")
    private fun Modifier.tapOutside() = composed {
        Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = { mainViewModel.stopEditing() }
            )
        }
    }

    /// The stack of a `Header`, `QuickConvertCard`, and `ProjectsCard`
    @Composable
    fun MainContentStack() {
        BackgroundBox {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Header()
                QuickConvertCard(quickConvertCardViewModel).Body()
                ProjectsCard(projectsCardViewModel, mainViewModel).Body()
            }
        }
    }

    /// When the user taps on the values in a `ProjectView` this opens the sheet for editing it
    @Composable
    private fun ProjectEditingSheet() {
        Column {
            mainViewModel.project?.let { project ->
                val pvm = mainViewModel.projectsCardViewModel.projectViewModel(project)
                pvm.expansion.value = ProjectExpansionLevel.EDITABLE
                ProjectView(pvm).Body()
            }
            Spacer(Modifier.weight(1f))
        }
    }

    /// A floating action button that will open the onboarding screen,
    /// or close the sheet to conclude editing a project
    @Composable
    private fun ActionButton(modifier: Modifier = Modifier) {
        val isBottomSheetExpanded by mainViewModel.isEditingProject.observeAsState()
        val showOnboarding by remember { onboardingScreenViewModel.showOnboarding }
        AnimatedVisibility(!showOnboarding,
            modifier = modifier.padding(16.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    if (isBottomSheetExpanded == true) {
                        mainViewModel.stopEditing()
                    } else {
                        onboardingScreenViewModel.openOnboarding()
                    }
              },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    closeButtonIcon(isBottomSheetExpanded),
                    contentDescription = "Open a help dialog, or confirm and close the edit dialog."
                )
            }
        }
    }

    @Composable
    private fun closeButtonIcon(isBottomSheetExpanded: Boolean?): ImageVector {
        return if (isBottomSheetExpanded == true) Icons.Filled.Check else Icons.Rounded.QuestionMark
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MainView().Body()
}
