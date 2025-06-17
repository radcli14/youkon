package view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.SwapVert
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import model.YkMeasurement
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import viewmodel.MainViewModel
import viewmodel.ProjectViewModel
import viewmodel.ProjectViewViews
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.swap_vert_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import kotlinx.coroutines.flow.MutableStateFlow

/// Upon tapping on the measurements, a bottom sheet will open into "Editable" mode.
class ProjectViewWhenEditing(
    private val vm: ProjectViewModel,
) {
    /// The list of editable measurements when the project is opened in a sheet for editing
    @Composable
    fun Body() {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(16.dp)
        ) {
            LabelStack()
            ExpansionStack()
        }

        if (vm.showSubtractAlert.value) {
            SubtractAlert(
                title = vm.measurementToDelete.value?.name ?: "",
                confirmAction = {
                    vm.confirmDelete()
                },
                cancelAction = { vm.cancelDelete() }
            )
        }
    }

    /// Provides a view modifier for a colored shadow if the selected view is highlighted in the onboarding screen
    private fun Modifier.onboardingModifier(view: ProjectViewViews): Modifier = composed {
        this.onboardingModifier(vm.highlightedView.value == view)
    }

    /// The name and description shown at the top of the project
    @Composable
    private fun LabelStack() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onboardingModifier(ProjectViewViews.LABEL_STACK),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProjectImage(
                project = vm.project.value,
                imageSize = Constants.imageSize,
                imageShape = MaterialTheme.shapes.large
            )
            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                NameField()
                DescriptionField()
            }
        }
    }

    /// The content that is displayed when the `DisclosureGroup` is expanded
    @Composable
    private fun ExpansionStack() {
        Column {
            HorizontalDivider(Modifier.padding(top = Constants.divTopPadding))
            ExpansionPlusMinusStack()
            ExpansionMeasurementsList()
        }
    }

    /// The title of the project, which is the `.name` field in the `YkProject`
    @Composable
    private fun NameField() {
        BasicTextFieldWithHint(
            value = vm.editedName.value,
            hint = "name",
            textStyle = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            ),
            onValueChange = { vm.updateName(it) }
        )
    }

    /// The subtitle of the project, which is the `.about` field in the `YkProject`
    @Composable
    private fun DescriptionField() {
        BasicTextFieldWithHint(
            value = vm.editedDescription.value,
            hint = "description",
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            onValueChange = { vm.updateDescription(it) },
        )
    }

    /// The plus and minus buttons on the right hand side when editing, to create or delete measurements
    @Composable
    private fun ExpansionPlusMinusStack() {
        Row(
            modifier = Modifier.onboardingModifier(ProjectViewViews.PLUS_MINUS),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Edit Measurements",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.weight(1f))
            PlusButton()
            MinusButton()
            ReorderButton()
        }
    }

    @Composable
    fun PlusButton() {
        val isEnabled = !(vm.canSubtract.value || vm.canReorder.value)
        FilledIconButton(
            enabled = isEnabled,
            shape = MaterialTheme.shapes.medium,
            colors = editButtonColors,
            onClick = vm::addMeasurement
        ) {
            Icon(
                imageVector = Icons.TwoTone.Add,
                contentDescription = "Add a new measurement",
            )
        }
    }

    @Composable
    fun MinusButton() {
        FilledIconButton(
            enabled = !vm.canReorder.value,
            shape = MaterialTheme.shapes.medium,
            colors = editButtonColors,
            onClick = vm::subtractMeasurement,
        ) {
            Icon(
                imageVector = Icons.TwoTone.Delete,
                contentDescription = "Allow deleting measurements",
            )
        }
    }

    @Composable
    fun ReorderButton() {
        FilledIconButton(
            enabled = !vm.canSubtract.value,
            shape = MaterialTheme.shapes.medium,
            colors = editButtonColors,
            onClick = vm::onReorderButtonTap
        ) {
            Icon(
                imageVector = Icons.TwoTone.SwapVert,
                contentDescription = "Allow reordering measurements",
            )
        }
    }

    /// A `ForEach` corresponding to each of the measurements, in either editable or static form
    @Composable
    private fun ExpansionMeasurementsList() {
        val project = vm.project.collectAsState()

        Column {
            LazyColumn {
                items(project.value.measurements.count(),
                    key = { project.value.measurements[it].id }
                ) {
                    EditableMeasurement(
                        measurement = project.value.measurements[it],
                        modifier = Modifier.animateItem()
                    )
                }
            }

            // I had a confusing issue where the two conditional state updates would fire
            // at different times, and would end up with either a situation where the message below
            // would go away from going from empty to adding one measurement, or when adding one
            // new measurement the editable form would not update. Adding multiple conditionals
            // seems to make sure they both fire on state updates, though its real ugly code given
            // that both states are supposed to mean the same thing. Someone please fix.
            if (vm.measurements.value.isEmpty()) {
                if (project.value.measurements.isEmpty()) {
                    Text("Add New Measurements")
                }
            }
        }
    }

    /// In the `expansionMeasurementList`, this is a single measurement that is editable
    @Composable
    private fun EditableMeasurement(measurement: YkMeasurement, modifier: Modifier = Modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            SubtractMeasurementButton(measurement)
            ReorderControls(measurement)
            MeasurementView(
                measurement = measurement,
                highlightNameAndDescription = vm.highlightedView.value == ProjectViewViews.MEASUREMENT_LABEL,
                highlightValueAndUnit = vm.highlightedView.value == ProjectViewViews.MEASUREMENT_FIELDS,
                onMeasurementUpdated = { updatedMeasurement ->
                    vm.updateMeasurement(updatedMeasurement)
                }
            ).Body()
        }
    }

    /// The red `X` that shows up to the left of a measurement when the user has enabled subtracting measurements
    @Composable
    private fun SubtractMeasurementButton(measurement: YkMeasurement) {
        AnimatedVisibilityForControls(vm.canSubtract.value) {
            SubtractButton(onClick = { vm.subtract(measurement) })
        }
    }

    /// Up and Down buttons for changing the position of a measurement in the card
    @Composable
    fun ReorderControls(measurement: YkMeasurement) {
        //val vm = mainViewModel.projectsCardViewModel.collectAsState()
        AnimatedVisibilityForControls(vm.canReorder.value) {
            UpDownButtons(
                contentDescriptionLeader = "Reorder ${measurement.name} measurement",
                onClick = { direction -> vm.onReorderControlButtonTap(measurement, direction) }
            )
        }
    }

    private class Constants {
        companion object {
            val imageSize = 48.dp
            val divTopPadding = 16.dp
        }
    }
}

/*
@Preview
@Composable
fun ProjectViewWhenEditingPreview() {
    val viewModel = ProjectViewModel(wembyProject())
    viewModel.toggleExpansion()
    ProjectViewWhenEditing(viewModel).Body()
}
 */
