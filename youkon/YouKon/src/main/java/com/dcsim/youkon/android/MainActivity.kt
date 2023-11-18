package com.dcsim.youkon.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.Surface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.ProjectExpansionLevel
import com.dcsim.youkon.android.viewmodels.MainViewModel
import com.dcsim.youkon.android.viewmodels.ProjectsCardViewModel
import com.dcsim.youkon.android.viewmodels.QuickConvertCardViewModel
import com.dcsim.youkon.android.views.BackgroundBox
import com.dcsim.youkon.android.views.Header
import com.dcsim.youkon.android.views.ProjectView
import com.dcsim.youkon.android.views.ProjectsCard
import com.dcsim.youkon.android.views.QuickConvertCard
import com.dcsim.youkon.android.views.roundedRadius

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val quickConvertCardViewModel: QuickConvertCardViewModel by viewModels()
    private lateinit var projectsCardViewModel: ProjectsCardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // The Projects card are dependent on user data that is contained in the `mainViewModel`
        projectsCardViewModel = ProjectsCardViewModel(mainViewModel.user)

        // Any time the user closes an editing dialog, save the user data to a json file
        mainViewModel.isEditingProject.observe(this) { isEditing ->
            if (!isEditing) {
                mainViewModel.saveUserToJson()
            }
        }

        setContent {
            MainView(
                mainViewModel,
                quickConvertCardViewModel,
                projectsCardViewModel
            ).Body()
        }
    }
}

class MainView(
    private val mainViewModel: MainViewModel,
    private val quickConvertCardViewModel: QuickConvertCardViewModel,
    private val projectsCardViewModel: ProjectsCardViewModel
) {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun Body() {
        val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        val isBottomSheetExpanded by mainViewModel.isEditingProject.observeAsState()
        LaunchedEffect(isBottomSheetExpanded) {
            if (isBottomSheetExpanded == true) {
                sheetState.show()
            } else {
                sheetState.hide()
            }
        }

        YoukonTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                ModalBottomSheetLayout(
                    sheetContent = {
                        ProjectEditingSheet()
                        LaunchedEffect(sheetState.currentValue) {
                            if (sheetState.currentValue == ModalBottomSheetValue.Hidden) {
                                mainViewModel.stopEditing()
                            }
                        }
                    },
                    sheetBackgroundColor = MaterialTheme.colorScheme.surface,
                    sheetState = sheetState,
                    sheetShape = RoundedCornerShape(topEnd = roundedRadius, topStart = roundedRadius)
                ) {
                    MainContentStack()
                }

            }
        }
    }

    /// The stack of a `Header`, `QuickConvertCard`, and `ProjectsCard`
    @Composable
    private fun MainContentStack() {
        BackgroundBox {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
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
}

@Preview
@Composable
fun DefaultPreview() {
    MainView(MainViewModel(), QuickConvertCardViewModel(), ProjectsCardViewModel()).Body()
}
