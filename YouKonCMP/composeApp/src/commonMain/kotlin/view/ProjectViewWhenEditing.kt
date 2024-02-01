package view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import model.YkMeasurement
import viewmodel.MainViewModel
import viewmodel.ProjectViewModel

/// Upon tapping on the measurements, a bottom sheet will open into "Editable" mode.
class ProjectViewWhenEditing(
    private val vm: ProjectViewModel,
    private val mainViewModel: MainViewModel? = null
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
                    mainViewModel?.saveUserToJson()
                },
                cancelAction = { vm.cancelDelete() }
            )
        }
    }

    /// The name and description shown at the top of the project
    @Composable
    private fun LabelStack() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProjectImage(
                project = vm.project.value,
                imageSize = vm.imageSize,
                imageShape = MaterialTheme.shapes.large
            )
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                NameField()
                DescriptionField()
            }
        }
    }

    /// The content that is displayed when the `DisclosureGroup` is expanded
    @Composable
    private fun ExpansionStack() {
        Column {
            Divider(Modifier.padding(top = vm.divTopPadding))
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Edit Measurements",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.weight(1f))
            PlusButton()
            MinusButton()
        }
    }

    @Composable
    fun PlusButton() {
        IconButton(
            onClick = {
                vm.addMeasurement()
                mainViewModel?.saveUserToJson()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add a new measurement",
                modifier = Modifier.editButtonModifier(),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

    @Composable
    fun MinusButton() {
        IconButton(
            onClick = { vm.subtractMeasurement() }
        ) {
            Icon(
                imageVector = Icons.Default.Delete, // .Remove,
                contentDescription = "Allow deleting measurements",
                modifier = Modifier.editButtonModifier(),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

    /// A `ForEach` corresponding to each of the measurements, in either editable or static form
    @Composable
    private fun ExpansionMeasurementsList() {
        val project = vm.project.collectAsState()

        Column {
            project.value.measurements.forEach { measurement ->
                EditableMeasurement(measurement)
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
    private fun EditableMeasurement(measurement: YkMeasurement) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SubtractMeasurementButton(measurement)
            MeasurementView(measurement).Body()
        }
    }

    /// The red `X` that shows up to the left of a measurement when the user has enabled subtracting measurements
    @Composable
    private fun SubtractMeasurementButton(measurement: YkMeasurement) {
        AnimatedVisibility(vm.canSubtract.value) {
            IconButton(
                onClick = { vm.subtract(measurement) }
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Delete ${measurement.name} measurement",
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

/*
@Preview
@Composable
fun ProjectViewWhenEditingPreview() {
    val viewModel = ProjectViewModel(wembyProject())
    viewModel.toggleExpansion()
    ProjectViewWhenEditing(viewModel).Body()
}

 */