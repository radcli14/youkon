package view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.SwapVert
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import model.YkProject
import viewmodel.MainViewModel
import viewmodel.ProjectsCardViews

class ProjectsCard(
    private val mainViewModel: MainViewModel = MainViewModel(),
    private val isExtended: Boolean = false
) {
    @Composable
    fun Body() {
        val vm = mainViewModel.projectsCardViewModel.collectAsState()
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .onboardingModifier(ProjectsCardViews.SURFACE)
                .fillMaxHeight()
                .width(760.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = Constants.horizontalPadding, vertical = Constants.verticalPadding),
                verticalArrangement = Arrangement.spacedBy(Constants.verticalPadding)
            ) {
                LabelStack()
                ProjectContent()
            }
        }

        if (vm.value.showSubtractAlert.value) {
            SubtractAlert(
                title = vm.value.projectToDelete.value?.name ?: "",
                confirmAction = {
                    vm.value.projectToDelete.value?.let {
                        mainViewModel.deleteProjectFromCloud(it)
                    }
                    vm.value.confirmDelete()
                    mainViewModel.saveUserToAll()
                },
                cancelAction = { vm.value.cancelDelete() }
            )
        }
    }

    /// Provides a view modifier for a colored shadow if the selected view is highlighted in the onboarding screen
    private fun Modifier.onboardingModifier(view: ProjectsCardViews): Modifier = composed {
        val vm = mainViewModel.projectsCardViewModel.collectAsState()
        this.onboardingModifier(vm.value.highlightedView.value == view)
    }

    /// A row with the text defining the card as `Projects`, and buttons to toggle adding and subtracting
    @Composable
    fun LabelStack() {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Projects",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.weight(1f))
            ControlButtons()
        }
    }

    @Composable
    fun ControlButtons() {
        Row {
            PlusButton()
            MinusButton()
            ReorderButton()
        }
    }

    /// A button that, when tapped, adds a new, empty project
    @Composable
    fun PlusButton() {
        val vm = mainViewModel.projectsCardViewModel.collectAsState()
        FilledIconButton(
            modifier = Modifier.onboardingModifier(ProjectsCardViews.PLUS),
            enabled = !(vm.value.canSubtract.value || vm.value.canReorder.value),
            shape = MaterialTheme.shapes.medium,
            colors = editButtonColors,
            onClick = {
                vm.value.addProject()
                mainViewModel.saveUserToAll()
            }
        ) {
            Icon(
                imageVector = Icons.TwoTone.Add,
                contentDescription = "Add a new project"
            )
        }
    }

    /// A button that, when tapped, toggles the projects to show red "X" to delete them
    @Composable
    fun MinusButton() {
        val vm = mainViewModel.projectsCardViewModel.collectAsState()
        FilledIconButton(
            enabled = !vm.value.canReorder.value,
            modifier = Modifier.onboardingModifier(ProjectsCardViews.MINUS),
            shape = MaterialTheme.shapes.medium,
            colors = editButtonColors,
            onClick = vm.value::onSubtractButtonTap
        ) {
            Icon(
                imageVector = Icons.TwoTone.Delete,
                contentDescription = "Allow deleting projects"
            )
        }
    }

    @Composable
    fun ReorderButton() {
        val vm = mainViewModel.projectsCardViewModel.collectAsState()
        FilledIconButton(
            enabled = !vm.value.canSubtract.value,
            modifier = Modifier.onboardingModifier(ProjectsCardViews.REORDER),
            shape = MaterialTheme.shapes.medium,
            colors = editButtonColors,
            onClick = {
                vm.value.onReorderButtonTap()
                mainViewModel.saveUserToAll()
            }
        ) {
            Icon(
                imageVector = Icons.TwoTone.SwapVert,
                contentDescription = "Allow reordering projects"
            )
        }
    }

    private var nCols = mutableStateOf(1)

    @Composable
    fun ProjectContent() {
        val vm by mainViewModel.projectsCardViewModel.collectAsState()

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(nCols.value),
            verticalItemSpacing = Constants.verticalSpacing,
            horizontalArrangement = Arrangement.spacedBy(Constants.horizontalPadding),
            modifier = Modifier.onSizeChanged {
                nCols.value = if (it.width.dp >= Constants.widthForTwoColumns) 2 else 1
            }
        ) {
            items(vm.projects, key = { it.id }) { project ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.width(Constants.projectRowWidth).animateItem()
                ) {
                    SubtractProjectButton(project)
                    ReorderControls(project)
                    val pvm = vm.projectViewModel(project, onProjectUpdated = {
                        mainViewModel.projectsCardViewModel.value.updateProject(it)
                    })
                    ProjectView(pvm, mainViewModel, isExtended).Body()
                }
            }
        }
    }

    /// The red `X` that shows up to the left of a project when the user has enabled subtracting projects
    @Composable
    fun SubtractProjectButton(project: YkProject) {
        val vm = mainViewModel.projectsCardViewModel.collectAsState()
        AnimatedVisibilityForControls(vm.value.canSubtract.value) {
            SubtractButton(onClick = { vm.value.subtract(project) })
        }
    }

    /// Up and Down buttons for changing the position of a project in the card
    @Composable
    fun ReorderControls(project: YkProject) {
        val vm = mainViewModel.projectsCardViewModel.collectAsState()
        AnimatedVisibilityForControls(vm.value.canReorder.value) {
            UpDownButtons(
                contentDescriptionLeader = "Reorder ${project.name} project",
                onClick = { direction -> vm.value.onReorderControlButtonTap(project, direction) }
            )
        }
    }

    private class Constants {
        companion object {
            val horizontalPadding = 16.dp
            val verticalPadding = 8.dp
            val verticalSpacing = 16.dp
            val projectRowWidth = 360.dp
            val widthForTwoColumns = 1440.dp
        }
    }
}
