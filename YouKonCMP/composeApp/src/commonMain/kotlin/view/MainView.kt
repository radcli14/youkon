package view

import YoukonTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import firebase.login.LoginScreen
import getPlatform
import kotlinx.coroutines.launch
import model.ProjectExpansionLevel
import firebase.login.LoginViewModel
import viewmodel.MainViewModel
import viewmodel.OnboardingScreenViewModel
import viewmodel.QuickConvertCardViewModel
import viewmodel.SettingsScreenState
import firebase.settings.SettingsViewModel


class MainView(
    private var mainViewModel: MainViewModel = MainViewModel(),
    private var quickConvertCardViewModel: QuickConvertCardViewModel = QuickConvertCardViewModel(),
    private var onboardingScreenViewModel: OnboardingScreenViewModel? = null,
    private var loginViewModel: LoginViewModel? = null,
    private var settingsViewModel: SettingsViewModel? = null
) {
    /// Initialize using the fake view models inside the onboarding screen
    constructor(onboardingScreenViewModel: OnboardingScreenViewModel): this(
        onboardingScreenViewModel.mainViewModel,
        onboardingScreenViewModel.quickConvertCardViewModel,
    )

    @Composable
    fun Body() {
        YoukonTheme {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                BottomSheetLayout()
                SettingsButton(Modifier.align(Alignment.TopEnd))
                ActionButton(Modifier.align(Alignment.BottomEnd))
                Onboarding()
                SettingsDialog()
            }
        }
    }

    /// The onboarding screen will be shown on first app startup, or when user taps the help button
    @Composable
    fun Onboarding() {
        onboardingScreenViewModel?.let {
            OnboardingScreen(it).AsDialog()
        }
    }

    @Composable
    private fun SettingsDialog() {
        if (mainViewModel.settingsScreenState.value != SettingsScreenState.HIDDEN) {
            Dialog(
                onDismissRequest = mainViewModel::hideSettings
            ) {
                SettingsBox()
            }
        }
    }

    @Composable
    private fun SettingsBox() {
        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceBright,
                    MaterialTheme.shapes.large
                )
                .height(360.dp)
                .padding(16.dp)
        ) {
            when (mainViewModel.settingsScreenState.value) {
                SettingsScreenState.SETTINGS -> SettingsScreen()
                SettingsScreenState.SIGN_IN -> SignInScreen()
                else -> {}
            }
        }
    }

    @Composable
    private fun SettingsScreen() {
        settingsViewModel?.let { viewModel ->
            firebase.settings.SettingsScreen(
                restartApp = mainViewModel::restartAppFromSettingsScreen,
                openScreen = mainViewModel::openScreenFromSettingsScreen,
                viewModel
            )
        }
    }

    @Composable
    private fun SignInScreen() {
        loginViewModel?.let { viewModel ->
            LoginScreen(
                openAndPopUp = mainViewModel::openAndPopupFromLoginScreen,
                viewModel
            )
        }
    }

    /// Holds state and the bottom sheet scaffold to allow the editing screen to appear
    /// as a sheet from the bottom of the screen.
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BottomSheetLayout() {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = 500.dp,
            sheetContent = {
                ProjectEditingSheet()
            },
            modifier = Modifier
                .closeSheetOnTapOutside()
                .closeKeyboardOnTapOutside()
        ) {
            MainContentStack()
        }
    }

    /// Set up the scaffold state such that it clears the project being edited from the
    /// view model if the user drags the sheet downward. Will do the same if the user taps
    /// the floating action button in the lower right.
    @OptIn(ExperimentalMaterial3Api::class)
    private val scaffoldState: BottomSheetScaffoldState
        @Composable
        get() {
            val scope = rememberCoroutineScope()
            val scaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = rememberModalBottomSheetState()
            )

            // React to changes in mainViewModel.isEditingProject by expanding or collapsing
            val isBottomSheetExpanded by mainViewModel.isEditingProject.observeAsState()
            LaunchedEffect(isBottomSheetExpanded) {
                if (isBottomSheetExpanded) {
                    scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                } else {
                    scope.launch { scaffoldState.bottomSheetState.hide() }
                }
            }

            // Observe dragging of the bottom sheet state, modify expanded state if needed
            LaunchedEffect(scaffoldState.bottomSheetState) {
                snapshotFlow { scaffoldState.bottomSheetState.targetValue }
                    .collect { state ->
                        when (state) {
                            SheetValue.Expanded -> {
                                scope.launch { scaffoldState.bottomSheetState.expand() }
                            }
                            SheetValue.PartiallyExpanded -> {
                                scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                            }
                            SheetValue.Hidden -> {
                                scope.launch { scaffoldState.bottomSheetState.hide() }
                            }
                        }
                    }
            }

            // Make sure that once the sheet is put into hidden state, you stop editing the project
            LaunchedEffect(scaffoldState.bottomSheetState) {
                snapshotFlow { scaffoldState.bottomSheetState.currentValue }
                    .collect { state ->
                        if (state == SheetValue.Hidden && mainViewModel.isEditingProject.value) {
                            mainViewModel.stopEditing(saveAfterStopping = true)
                        }
                    }
            }

            return scaffoldState
        }

    /// Modifier used to close the bottom sheet when tapping outside of it
    private fun Modifier.closeSheetOnTapOutside() = composed {
        Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = { mainViewModel.stopEditing() }
            )
        }
    }

    /// Modifier used to close the keyboard when tapping outside of it
    private fun Modifier.closeKeyboardOnTapOutside() = composed {
        val localFocusManager = LocalFocusManager.current
        Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = { localFocusManager.clearFocus() }
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
                ProjectsCard(mainViewModel).Body()
            }
        }
    }

    @Composable
    fun SettingsButton(modifier: Modifier = Modifier) {
        val isIphone = "iOS" in getPlatform().name
        IconButton(
            modifier = modifier.padding(top = if (isIphone) 32.dp else 0.dp),
            onClick = mainViewModel::showSettings
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }

    /// When the user taps on the values in a `ProjectView` this opens the sheet for editing it
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ProjectEditingSheet() {
        Column(
            Modifier.closeKeyboardOnTapOutside()
        ) {
            mainViewModel.project?.let { project ->
                val pvm = mainViewModel.projectsCardViewModel.projectViewModel(project)
                pvm.expansion.value = ProjectExpansionLevel.EDITABLE
                ProjectViewWhenEditing(pvm).Body()
            }
            Spacer(Modifier.weight(1f))
        }
    }

    /// A floating action button that will open the onboarding screen,
    /// or close the sheet to conclude editing a project
    @Composable
    private fun ActionButton(modifier: Modifier = Modifier) {
        val isBottomSheetExpanded by mainViewModel.isEditingProject.observeAsState()
        val showOnboarding by remember {
            onboardingScreenViewModel?.showOnboarding ?: mutableStateOf(false)
        }
        AnimatedVisibility(!showOnboarding,
            modifier = modifier.padding(16.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    if (isBottomSheetExpanded) {
                        mainViewModel.stopEditing(saveAfterStopping = true)
                    } else {
                        onboardingScreenViewModel?.openOnboarding()
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
        return if (isBottomSheetExpanded == true) Icons.Filled.Check else Icons.Rounded.Info
    }
}

/*
@Preview
@Composable
fun DefaultPreview() {
    MainView().Body()
}
 */