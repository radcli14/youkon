package view

//import androidx.compose.material.icons.filled.Remove
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import model.YkProject
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import viewmodel.MainViewModel
import viewmodel.ProjectsCardViews
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.swap_vert_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24

class ProjectsCard(
    private val mainViewModel: MainViewModel = MainViewModel()
) {
    @Composable
    fun Body() {
        val vm = mainViewModel.projectsCardViewModel.collectAsState()
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
            PlusButton()
            Spacer(Modifier.width(8.dp))
            MinusButton()
            Spacer(Modifier.width(8.dp))
            ReorderButton()
        }
    }

    /// A button that, when tapped, adds a new, empty project
    @Composable
    fun PlusButton() {
        val vm = mainViewModel.projectsCardViewModel.collectAsState()
        IconButton(
            modifier = Modifier.editButtonModifier().onboardingModifier(ProjectsCardViews.PLUS),
            onClick = {
                vm.value.addProject()
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
        val vm = mainViewModel.projectsCardViewModel.collectAsState()
        IconButton(
            enabled = !vm.value.canReorder.value,
            modifier = Modifier.editButtonModifier(
                color = pickerColor(vm.value.canSubtract.value)
            ).onboardingModifier(ProjectsCardViews.MINUS),
            onClick = vm.value::onSubtractButtonTap
        ) {
            Icon(
                imageVector = Icons.Default.Delete, // .Remove,
                contentDescription = "Allow deleting projects",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun ReorderButton() {
        val vm = mainViewModel.projectsCardViewModel.collectAsState()
        IconButton(
            enabled = !vm.value.canSubtract.value,
            modifier = Modifier.editButtonModifier(
                color = pickerColor(vm.value.canReorder.value)
            ), //.onboardingModifier(ProjectsCardViews.REORDER),
            onClick = vm.value::onReorderButtonTap
        ) {
            Icon(
                painter = painterResource(Res.drawable.swap_vert_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                contentDescription = "Allow reordering projects",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

    @OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
    @Composable
    fun ProjectContent() {
        val vm = mainViewModel.projectsCardViewModel.collectAsState()
        val vertScrollState = rememberScrollState()

        FlowRow(
            modifier = Modifier
                .verticalScroll(vertScrollState)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            vm.value.projects.value.forEach { project ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.width(360.dp)
                ) {
                    SubtractProjectButton(project)
                    ReorderControls(project)
                    val pvm = vm.value.projectViewModel(project)
                    ProjectView(pvm, mainViewModel).Body()
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
}
