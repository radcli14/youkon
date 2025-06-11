package view

import YoukonTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.twotone.Check
import androidx.compose.material.icons.twotone.CheckCircle
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import firebase.login.LoginScreen
import firebase.login.LoginViewModel
import firebase.settings.SettingsViewModel
import firebase.sign_up.SignUpScreen
import firebase.sign_up.SignUpViewModel
import getPlatform
import kotlinx.coroutines.launch
import model.ProjectExpansionLevel
import org.jetbrains.compose.resources.painterResource
import viewmodel.MainViewModel
import viewmodel.OnboardingScreenViewModel
import viewmodel.QuickConvertCardViewModel
import viewmodel.SettingsScreenState
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.header
import youkon.composeapp.generated.resources.icon_clearbackground


class MainView(
    private var mainViewModel: MainViewModel = MainViewModel(),
    private var quickConvertCardViewModel: QuickConvertCardViewModel = QuickConvertCardViewModel(),
    private var onboardingScreenViewModel: OnboardingScreenViewModel? = null,
    private var loginViewModel: LoginViewModel? = null,
    private var settingsViewModel: SettingsViewModel? = null,
    private var signupViewModel: SignUpViewModel? = null
) {
    /// Initialize using the fake view models inside the onboarding screen
    constructor(onboardingScreenViewModel: OnboardingScreenViewModel): this(
        onboardingScreenViewModel.mainViewModel,
        onboardingScreenViewModel.quickConvertCardViewModel,
    )

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Body() {
        YoukonTheme {
            BackgroundBox {
                /// Holds state and the bottom sheet scaffold to allow the editing screen to appear
                /// as a sheet from the bottom of the screen.
                BottomSheetScaffold(
                    scaffoldState = scaffoldState,
                    sheetPeekHeight = Constants.sheetPeakHeight,
                    sheetContent = {
                        ProjectEditingSheet()
                    },
                    modifier = Modifier
                        .closeSheetOnTapOutside()
                        .closeKeyboardOnTapOutside(),
                    topBar = { TopBar() },
                    containerColor = Color.Transparent
                ) {
                    MainContentStack()
                    Onboarding()
                    SettingsDialog()
                }
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
                .height(Constants.settingsBoxHeight)
                .padding(Constants.mainContentPadding)
        ) {
            when (mainViewModel.settingsScreenState.value) {
                SettingsScreenState.SETTINGS -> SettingsScreen()
                SettingsScreenState.SIGN_IN -> SignInScreen()
                SettingsScreenState.CREATE_ACCOUNT -> CreateAccountScreen()
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

    @Composable
    private fun CreateAccountScreen() {
        signupViewModel?.let { viewModel ->
            SignUpScreen(
                mainViewModel::openAndPopupFromLoginScreen,
                viewModel
            )
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
            val isBottomSheetExpanded by mainViewModel.isEditingProject.collectAsState()
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
        Column(
            modifier = Modifier.padding(Constants.mainContentPadding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Constants.mainContentSpacing)
        ) {
            //Header()
            QuickConvertCard(quickConvertCardViewModel).Body()
            ProjectsCard(mainViewModel).Body()
        }
    }

    @Composable
    fun SettingsButton(modifier: Modifier = Modifier) {
        IconButton(
            onClick = mainViewModel::showSettings
        ) {
            Icon(
                imageVector = Icons.TwoTone.Settings,
                contentDescription = "Settings",
                //modifier = Modifier.size(Constants.settingsButtonSize)
            )
        }
    }

    /// When the user taps on the values in a `ProjectView` this opens the sheet for editing it
    @Composable
    private fun ProjectEditingSheet() {
        val activeProject by mainViewModel.project.collectAsState()
        Column(
            Modifier.closeKeyboardOnTapOutside()
        ) {
            activeProject?.let { project ->
                val pcvm = mainViewModel.projectsCardViewModel.collectAsState()
                val pvm = pcvm.value.projectViewModel(project)
                pvm.expansion.value = ProjectExpansionLevel.EDITABLE
                ProjectViewWhenEditing(pvm).Body()
            }
            Spacer(Modifier.weight(1f))
        }
    }

    /// The bottom bar, with either settings and info button, or text field controls
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar() {
        val quickIsFocused by quickConvertCardViewModel.isFocused.collectAsState()
        CenterAlignedTopAppBar(
            title = {
                Text("YouKon",
                    fontFamily = philosopherFont,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displayMedium
                )
            },
            navigationIcon = {
                Image(
                    painter = painterResource(Res.drawable.icon_clearbackground),
                    contentDescription = "App icon",
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                )
            },
            actions = {
                AnimatedVisibility(quickIsFocused) {
                    MeasurementEditingControls(
                        onPlusMinusClick = quickConvertCardViewModel::switchSign,
                        onTimesTenClick = quickConvertCardViewModel::multiplyByTen,
                        onDivideByTenClick = quickConvertCardViewModel::divideByTen,
                        onClearValueClick = quickConvertCardViewModel::clearValue
                    )
                }
                AnimatedVisibility(!quickIsFocused) {
                    SettingsButton()
                }
                ActionButton()
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )
    }

    @Composable
    fun MeasurementEditingControls(
        onPlusMinusClick: () -> Unit,
        onTimesTenClick: () -> Unit,
        onDivideByTenClick: () -> Unit,
        onClearValueClick: () -> Unit
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MeasurementEditingButton("±", onPlusMinusClick)
            MeasurementEditingButton("×10", onTimesTenClick)
            MeasurementEditingButton("÷10", onDivideByTenClick)
            MeasurementEditingButton("Clear", onClearValueClick)
        }
    }

    @Composable
    fun MeasurementEditingButton(text: String, onClick: () -> Unit) {
        Button(
            onClick = onClick,
            modifier = Modifier.width(56.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            Text(text)
        }
    }


    /// An action button that will open the onboarding screen,
    /// or close the sheet to conclude editing the quick convert card or a project
    @Composable
    private fun ActionButton(modifier: Modifier = Modifier) {
        val localFocusManager = LocalFocusManager.current
        val quickIsFocused by quickConvertCardViewModel.isFocused.collectAsState()
        val isBottomSheetExpanded by mainViewModel.isEditingProject.collectAsState()
        val showOnboarding by remember {
            onboardingScreenViewModel?.showOnboarding ?: mutableStateOf(false)
        }
        AnimatedVisibility(!showOnboarding,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            IconButton(
                onClick = {
                    if (quickIsFocused) {
                        localFocusManager.clearFocus()
                    } else if (isBottomSheetExpanded) {
                        mainViewModel.stopEditing(saveAfterStopping = true)
                    } else {
                        onboardingScreenViewModel?.openOnboarding()
                    }
                },
            ) {
                Icon(actionButtonIcon(isBottomSheetExpanded || quickIsFocused),
                    contentDescription = "Open a help dialog, or confirm and close the edit dialog.",
                )
            }
        }
    }

    @Composable
    private fun actionButtonIcon(isConfirmationAction: Boolean?): ImageVector {
        return if (isConfirmationAction == true) Icons.TwoTone.CheckCircle else Icons.TwoTone.Info
    }

    class Constants {
        companion object {
            private val isIphone = "iOS" in getPlatform().name
            val mainContentPadding = 16.dp
            val mainContentSpacing = 16.dp
            val settingsButtonTopPadding = if (isIphone) 48.dp else 40.dp
            val settingsButtonEndPadding = 12.dp
            val settingsButtonSize = 40.dp
            val settingsBoxHeight = 420.dp
            val actionButtonBottomPadding = if (isIphone) 36.dp else 24.dp
            val actionButtonEndPadding = 24.dp
            val actionButtonIconSize = 36.dp
            val sheetPeakHeight = 500.dp
        }
    }
}
