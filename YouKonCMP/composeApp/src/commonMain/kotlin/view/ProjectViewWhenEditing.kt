package view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Badge
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Description
import androidx.compose.material.icons.twotone.Straighten
import androidx.compose.material.icons.twotone.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import model.YkMeasurement
import org.jetbrains.compose.resources.stringResource
import projects.ProjectImage
import purchases.PurchasesViewModel
import theming.AnimatedVisibilityForControls
import theming.UpDownButtons
import theming.defaultPadding
import theming.editButtonColors
import theming.onboardingModifier
import viewmodel.ProjectViewModel
import viewmodel.ProjectViewViews
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.add_new_measurement
import youkon.composeapp.generated.resources.add_new_measurements
import youkon.composeapp.generated.resources.allow_reorder_measurement
import youkon.composeapp.generated.resources.close_project_editor
import youkon.composeapp.generated.resources.edit_measurements
import youkon.composeapp.generated.resources.project_description_label
import youkon.composeapp.generated.resources.project_description_placeholder
import youkon.composeapp.generated.resources.project_name_label
import youkon.composeapp.generated.resources.project_name_placeholder
import youkon.composeapp.generated.resources.reorder_measurement

/// Upon tapping on the measurements, a bottom sheet will open into "Editable" mode.
class ProjectViewWhenEditing(
    private val vm: ProjectViewModel,
    private val purchases: PurchasesViewModel? = null,
    private val onCloseButtonClick: () -> Unit
) {
    /// The list of editable measurements when the project is opened in a sheet for editing
    @Composable
    fun Body() {
        Box {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Constants.spacing),
                modifier = Modifier.defaultPadding()
            ) {
                LabelStack()
                ExpansionStack()
            }

            FilledIconButton(
                onClick = onCloseButtonClick,
                modifier = Modifier.align(Alignment.TopEnd).padding(end = Constants.padding),
                shape = MaterialTheme.shapes.large,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(Res.string.close_project_editor)
                )
            }
        }

        if (vm.showSubtractAlert.value) {
            SubtractAlert(
                title = vm.measurementToDelete.value?.name ?: "",
                confirmAction = vm::confirmDelete,
                cancelAction = vm::cancelDelete
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Constants.spacing),
            modifier = Modifier.fillMaxWidth()
        ) {
            val project by vm.project.collectAsState()
            ProjectImage(
                project = project,
                imageSize = Constants.imageSize,
                imageShape = MaterialTheme.shapes.large,
                onSelectNewImage = vm::updateImage
            )
            NameField()
            DescriptionField()
        }
    }

    /// The content that is displayed when the `DisclosureGroup` is expanded
    @Composable
    private fun ExpansionStack() {
        Column(
            verticalArrangement = Arrangement.spacedBy(Constants.spacing)
        ) {
            HorizontalDivider()
            ExpansionPlusMinusStack()
            ExpansionMeasurementsList()
        }
    }

    /// The title of the project, which is the `.name` field in the `YkProject`
    @Composable
    private fun NameField() {
        OutlinedTextField(
            value = vm.editedName.value,
            onValueChange = vm::updateName,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(Res.string.project_name_label)) },
            placeholder = { Text(stringResource(Res.string.project_name_placeholder)) },
            trailingIcon = { Icon(Icons.TwoTone.Badge, contentDescription = null) },
            textStyle = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            ),
        )
    }

    /// The subtitle of the project, which is the `.about` field in the `YkProject`
    @Composable
    private fun DescriptionField() {
        OutlinedTextField(
            value = vm.editedDescription.value,
            onValueChange = vm::updateDescription,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(Res.string.project_description_label)) },
            placeholder = { Text(stringResource(Res.string.project_description_placeholder)) },
            trailingIcon = { Icon(Icons.TwoTone.Description, contentDescription = null) },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
        )
    }

    /// The plus and minus buttons on the right hand side when editing, to create or delete measurements
    @Composable
    private fun ExpansionPlusMinusStack() {
        Row(
            modifier = Modifier.onboardingModifier(ProjectViewViews.PLUS_MINUS),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.edit_measurements),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.weight(1f))
            PlusButton()
            SubtractButton()
            ReorderButton()
        }
    }

    @Composable
    fun PlusButton() {
        FilledIconButton(
            enabled = vm.addButtonIsEnabled,
            shape = MaterialTheme.shapes.medium,
            colors = editButtonColors,
            onClick = vm::addMeasurement
        ) {
            Icon(
                imageVector = Icons.TwoTone.Add,
                contentDescription = stringResource(Res.string.add_new_measurement),
            )
        }
    }

    @Composable
    fun SubtractButton() {
        FilledIconButton(
            enabled = vm.subtractButtonIsEnabled,
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
            enabled = vm.reorderButtonIsEnabled,
            shape = MaterialTheme.shapes.medium,
            colors = editButtonColors,
            onClick = vm::onReorderButtonTap
        ) {
            Icon(
                imageVector = Icons.TwoTone.SwapVert,
                contentDescription = stringResource(Res.string.allow_reorder_measurement),
            )
        }
    }

    /// A `ForEach` corresponding to each of the measurements, in either editable or static form
    @Composable
    private fun ExpansionMeasurementsList() {
        val measurements = vm.measurements.collectAsState()
        val project = vm.project.collectAsState()

        Column {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Constants.spacing)
            ) {
                items(measurements.value.count(),
                    key = { measurements.value[it].id }
                ) {
                    EditableMeasurement(
                        measurement = measurements.value[it],
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
                    AddMeasurementSuggestion()
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
                purchases = purchases,
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
            theming.SubtractButton(onClick = { vm.subtract(measurement) })
        }
    }

    /// Up and Down buttons for changing the position of a measurement in the card
    @Composable
    fun ReorderControls(measurement: YkMeasurement) {
        AnimatedVisibilityForControls(vm.canReorder.value) {
            UpDownButtons(
                contentDescriptionLeader = stringResource(Res.string.reorder_measurement).replace("$1%s", measurement.name),  // TODO: this shouldn't require replace, try again after updating compose in gradle
                onClick = { direction -> vm.onReorderControlButtonTap(measurement, direction) }
            )
        }
    }

    @Composable
    fun AddMeasurementSuggestion() {
        Button(
            onClick = vm::addMeasurement,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = MaterialTheme.shapes.medium,
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.TwoTone.Straighten,
                    contentDescription = null
                )
                Text(
                    stringResource(Res.string.add_new_measurements),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }

    private class Constants {
        companion object {
            val imageSize = 128.dp
            val padding = 16.dp
            val spacing = 8.dp
        }
    }
}
