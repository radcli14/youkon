package view

//import androidx.compose.material.icons.filled.Remove
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import model.YkProject
import viewmodel.MainViewModel
import viewmodel.ProjectsCardViews

class ProjectsCard(
    private val mainViewModel: MainViewModel = MainViewModel()
) {
    private val vm = mainViewModel.projectsCardViewModel
    @Composable
    fun Body() {
        Surface(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .onboardingModifier(ProjectsCardViews.SURFACE)
                .fillMaxHeight()
                .width(760.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LabelStack()
                ProjectContent()
            }
        }

        if (vm.showSubtractAlert.value) {
            SubtractAlert(
                title = vm.projectToDelete.value?.name ?: "",
                confirmAction = {
                    vm.confirmDelete()
                    //mainViewModel.saveUserToJson()
                    mainViewModel.saveUserToAll()
                },
                cancelAction = { vm.cancelDelete() }
            )
        }
    }

    /// Provides a view modifier for a colored shadow if the selected view is highlighted in the onboarding screen
    private fun Modifier.onboardingModifier(view: ProjectsCardViews): Modifier = composed {
        this.onboardingModifier(vm.highlightedView.value == view)
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
            PlusButton()
            Spacer(Modifier.width(8.dp))
            MinusButton()
        }
    }

    /// A button that, when tapped, adds a new, empty project
    @Composable
    fun PlusButton() {
        IconButton(
            modifier = Modifier.editButtonModifier().onboardingModifier(ProjectsCardViews.PLUS),
            onClick = {
                vm.addProject()
                mainViewModel.saveUserToAll()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add a new project",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

    /// A button that, when tapped, toggles the projects to show red "X" to delete them
    @Composable
    fun MinusButton() {
        IconButton(
            modifier = Modifier.editButtonModifier().onboardingModifier(ProjectsCardViews.MINUS),
            onClick = { vm.onSubtractButtonTap() }
        ) {
            Icon(
                imageVector = Icons.Default.Delete, // .Remove,
                contentDescription = "Allow deleting projects",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun ProjectContent() {
        val vertScrollState = rememberScrollState()

        FlowRow(
            modifier = Modifier
                .verticalScroll(vertScrollState)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            vm.projects.value.forEach { project ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.width(360.dp)
                ) {
                    SubtractProjectButton(project)
                    val pvm = vm.projectViewModel(project)
                    ProjectView(pvm, mainViewModel).Body()
                }
            }
        }
    }

    /// The red `X` that shows up to the left of a project when the user has enabled subtracting projects
    @Composable
    fun SubtractProjectButton(project: YkProject) {
        AnimatedVisibility(vm.canSubtract.value) {
            IconButton(
                onClick = { vm.subtract(project) }
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Delete ${project.name} project",
                    modifier = Modifier.editButtonModifier(
                        color = MaterialTheme.colorScheme.error,
                        alpha = 1f,
                        width = 24.dp,
                        height = 24.dp,
                        padding = 4.dp,
                        shape = CircleShape
                    ),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
