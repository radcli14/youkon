package com.dcengineer.youkon.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcengineer.youkon.ProjectExpansionLevel
import com.dcengineer.youkon.YoukonTheme
import com.dcengineer.youkon.viewmodels.MainViewModel
import com.dcengineer.youkon.viewmodels.ProjectsCardViewModel
import com.dcengineer.youkon.viewmodels.QuickConvertCardViewModel


class MainView(
    private val mainViewModel: MainViewModel,
    private val quickConvertCardViewModel: QuickConvertCardViewModel,
    private val projectsCardViewModel: ProjectsCardViewModel
) {
    @Composable
    fun Body() {
        YoukonTheme {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                BottomSheetLayout()
                CloseButton(Modifier.align(Alignment.BottomEnd))
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun BottomSheetLayout() {
        val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        val isBottomSheetExpanded by mainViewModel.isEditingProject.observeAsState()
        LaunchedEffect(isBottomSheetExpanded) {
            if (isBottomSheetExpanded == true) {
                sheetState.show()
            } else {
                sheetState.hide()
            }
        }

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
            sheetShape = MaterialTheme.shapes.large.copy(
                bottomStart = CornerSize(0.dp),
                bottomEnd = CornerSize(0.dp)
            )
        ) {
            MainContentStack()
        }
    }

    /// The stack of a `Header`, `QuickConvertCard`, and `ProjectsCard`
    @Composable
    private fun MainContentStack() {
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

    /// A floating action button that will close the sheet to conclude editing a project
    @Composable
    private fun CloseButton(modifier: Modifier = Modifier) {
        val isBottomSheetExpanded by mainViewModel.isEditingProject.observeAsState()
        AnimatedVisibility(isBottomSheetExpanded == true,
            modifier = modifier.padding(16.dp)
        ) {
            FloatingActionButton(
                onClick = { mainViewModel.stopEditing() },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Filled.Check, "Confirm and close the edit dialog.")
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MainView(MainViewModel(), QuickConvertCardViewModel(), ProjectsCardViewModel()).Body()
}
