package view

import navigation.LOGIN_SCREEN
import navigation.SETTINGS_SCREEN
import navigation.SIGN_UP_SCREEN
import theming.YoukonTheme
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.twotone.CheckCircle
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import theming.closeKeyboardOnTapOutside
import theming.closeSheetOnTapOutside
import theming.defaultPadding
import firebase.login.LoginScreen
import firebase.login.LoginViewModel
import firebase.settings.SettingsViewModel
import firebase.sign_up.SignUpScreen
import firebase.sign_up.SignUpViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.ProjectExpansionLevel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import purchases.PurchasesViewModel
import purchases.YouKonExtendedPaywall
import theming.philosopherFont
import viewmodel.MainViewModel
import viewmodel.OnboardingScreenViewModel
import viewmodel.QuickConvertCardViewModel
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.app_icon_decoration
import youkon.composeapp.generated.resources.help
import youkon.composeapp.generated.resources.icon_clearbackground
import youkon.composeapp.generated.resources.navigate_to
import youkon.composeapp.generated.resources.onboarding
import youkon.composeapp.generated.resources.save_changes
import youkon.composeapp.generated.resources.settings
import utilities.requestReview


class MainView(
    private var mainViewModel: MainViewModel = MainViewModel(),
    private var quickConvertCardViewModel: QuickConvertCardViewModel = QuickConvertCardViewModel(),
    private var onboardingScreenViewModel: OnboardingScreenViewModel? = null,
    private var loginViewModel: LoginViewModel? = null,
    private var settingsViewModel: SettingsViewModel? = null,
    private var signupViewModel: SignUpViewModel? = null,
    private var purchasesViewModel: PurchasesViewModel = PurchasesViewModel(),
    private var context: Any? = null
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
                val navController = rememberNavController()

                val showPaywall by purchasesViewModel.shouldShowPaywall.collectAsState()

                // A review should be requested if the user has taken enough actions, or enough time elapsed with the app open
                LaunchedEffect(mainViewModel.userSaveActions.value) {
                    if (mainViewModel.shouldRequestReview) {
                        requestReview(context)
                    }
                }
                LaunchedEffect(Unit) {
                    delay(69696)
                    requestReview(context)
                }

                /// Holds state and the bottom sheet scaffold to allow the editing screen to appear
                /// as a sheet from the bottom of the screen.
                BottomSheetScaffold(
                    scaffoldState = scaffoldState,
                    sheetPeekHeight = Constants.sheetPeakHeight,
                    sheetContent = {
                        ProjectEditingSheet()
                    },
                    modifier = Modifier
                        //.windowInsetsPadding(WindowInsets.safeContent)
                        .closeSheetOnTapOutside(mainViewModel::stopEditing)
                        .closeKeyboardOnTapOutside(),
                    topBar = { TopBar(navController) },
                    containerColor = Color.Transparent,
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "main"
                    ) {
                        composable("main") {
                            MainContentStack()
                        }
                        composable("onboarding") {
                            OnboardingScreen(onboardingScreenViewModel ?: OnboardingScreenViewModel()).Body(navController)
                        }
                        composable(SETTINGS_SCREEN) {
                            SettingsScreen(navController)
                        }
                        composable(LOGIN_SCREEN) {
                            SignInScreen(navController)
                        }
                        composable(SIGN_UP_SCREEN) {
                            CreateAccountScreen(navController)
                        }
                    }
                }

                AnimatedVisibility(showPaywall) {
                    YouKonExtendedPaywall(dismissRequest = purchasesViewModel::hidePaywall)
                }
            }
        }
    }

    /// The top bar, with app branding, and either settings and info or close button
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun TopBar(navController: NavHostController) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        val currentRouteTitle = currentRoute?.capitalize(locale = Locale.current) ?: "Unknown"

        CenterAlignedTopAppBar(
            title = {
                AnimatedContent(
                    when (currentRoute) {
                        "main" -> "YouKon"
                        SETTINGS_SCREEN -> stringResource(Res.string.settings)
                        "onboarding" -> stringResource(Res.string.onboarding)
                        else -> currentRouteTitle
                    }
                ) { titleText ->
                    Text(
                        text = titleText,
                        fontFamily = philosopherFont,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            },
            navigationIcon = {
                AnimatedVisibility(currentRoute != "main") {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
                AnimatedVisibility(currentRoute == "main") {
                    Image(
                        painter = painterResource(Res.drawable.icon_clearbackground),
                        contentDescription = stringResource(Res.string.app_icon_decoration),
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )
                }
            },
            actions = {
                AnimatedVisibility(currentRoute == "main") {
                    Row {
                        SettingsButton(navController)
                        ActionButton(navController)
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )
    }

    @Composable
    private fun SettingsButton(navController: NavHostController) {
        IconButton(
            onClick = { navController.navigate(SETTINGS_SCREEN) }
        ) {
            Icon(
                imageVector = Icons.TwoTone.Settings,
                contentDescription = stringResource(Res.string.navigate_to, Res.string.settings),
            )
        }
    }

    /// An action button that will open the onboarding screen,
    /// or close the sheet to conclude editing the quick convert card or a project
    @Composable
    private fun ActionButton(navController: NavHostController) {
        IconButton(
            onClick = {
                if (mainViewModel.isEditingProject.value) {
                    mainViewModel.stopEditing(saveAfterStopping = true)
                } else {
                    navController.navigate("onboarding")
                }
            }
        ) {
            Icon(
                imageVector = if (mainViewModel.isEditingProject.value) {
                    Icons.TwoTone.CheckCircle
                } else {
                    Icons.TwoTone.Info
                },
                contentDescription = if (mainViewModel.isEditingProject.value) {
                    stringResource(Res.string.save_changes)
                } else {
                    stringResource(Res.string.navigate_to, Res.string.help)
                }
            )
        }
    }

    @Composable
    private fun SettingsScreen(navController: NavHostController) {
        val extendedPurchaseState by purchasesViewModel.extendedPurchaseState.collectAsState()
        settingsViewModel?.let { viewModel ->
            firebase.settings.SettingsScreen(
                restartApp = mainViewModel::restartAppFromSettingsScreen,
                openScreen = { route -> navController.navigate(route) },
                extendedPurchaseState = extendedPurchaseState,
                showPaywall = purchasesViewModel::showPaywall,
                viewModel
            )
        }
    }

    @Composable
    private fun SignInScreen(navController: NavHostController) {
        loginViewModel?.let { viewModel ->
            LoginScreen(
                openAndPopUp = { open, popup ->
                    navController.popBackStack()
                },
                viewModel
            )
        }
    }

    @Composable
    private fun CreateAccountScreen(navController: NavHostController) {
        signupViewModel?.let { viewModel ->
            SignUpScreen(
                openAndPopUp = { open, popup ->
                    navController.navigate(open) {
                        popUpTo(popup) { inclusive = true }
                    }
                },
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

    /// The stack of a `Header`, `QuickConvertCard`, and `ProjectsCard`
    @Composable
    fun MainContentStack() {
        Column(
            modifier = Modifier
                .defaultPadding()
                .padding(bottom = Constants.mainContentPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Constants.mainContentSpacing)
        ) {
            QuickConvertCard(quickConvertCardViewModel, purchasesViewModel).Body()
            val isExtended by purchasesViewModel.isExtended.collectAsState()
            ProjectsCard(mainViewModel, isExtended).Body()
        }
    }

    /// When the user taps on the values in a `ProjectView` this opens the sheet for editing it
    @Composable
    private fun ProjectEditingSheet() {
        val activeProject by mainViewModel.project.collectAsState()
        Column(
            Modifier
                .closeKeyboardOnTapOutside()
        ) {
            activeProject?.let { project ->
                val pcvm = mainViewModel.projectsCardViewModel.collectAsState()
                val pvm = pcvm.value.projectViewModel(project)
                pvm.expansion.value = ProjectExpansionLevel.EDITABLE
                ProjectViewWhenEditing(pvm, purchasesViewModel, onCloseButtonClick = {
                    mainViewModel.stopEditing(saveAfterStopping = true)
                }).Body()
            }
            Spacer(Modifier.weight(1f))
        }
    }

    class Constants {
        companion object {
            val mainContentPadding = 16.dp
            val mainContentSpacing = 16.dp
            val sheetPeakHeight = 500.dp
        }
    }
}
