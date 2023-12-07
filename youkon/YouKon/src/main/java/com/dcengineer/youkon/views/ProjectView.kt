package com.dcengineer.youkon.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dcengineer.youkon.R
import com.dcengineer.youkon.YkSystem
import com.dcsim.youkon.ProjectExpansionLevel
import com.dcsim.youkon.YkMeasurement
import com.dcengineer.youkon.viewmodels.MainViewModel
import com.dcengineer.youkon.viewmodels.ProjectViewModel

/// The `ProjectView` displays the data from a `YkProject`.
/// Initially shown with an icon, name, and description, in "Compact" mode.
/// When tapped, the view will expand similar to show the measurements in "Static" mode.
/// Upon tapping on the measurements, a bottom sheet will open into "Editable" mode.
class ProjectView(
    private val vm: ProjectViewModel,
    private val mainViewModel: MainViewModel? = null
) {
    @Composable
    fun Body() {
        if (vm.expansion.value == ProjectExpansionLevel.EDITABLE) {
            MainStackWhenEditing()
        } else {
            DisclosureGroupWhenNotEditing()
        }
    }

    /// The list of editable measurements when the project is opened in a sheet for editing
    @Composable
    fun MainStackWhenEditing() {
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

    /// The disclosure group with static content inside, with label with name and description
    @Composable
    private fun DisclosureGroupWhenNotEditing() {
        Surface(
            color = grayBackground.copy(alpha = 0.4f),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                LabelStack()
                ExpansionStack()
            }
        }
    }

    /// The name and description shown at the top of the project
    @Composable
    private fun LabelStack() {
        Row(
            modifier = if (vm.expansion.value != ProjectExpansionLevel.EDITABLE) {
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (vm.expansion.value != ProjectExpansionLevel.EDITABLE) vm.toggleExpansion()
                    }
            } else {
               Modifier.fillMaxWidth()
            },
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProjectImage()
            Column(modifier = Modifier.weight(1f)) {
                NameField()
                DescriptionField()
            }
            if (vm.expansion.value != ProjectExpansionLevel.EDITABLE) {
                CloseIcon(vm.expansion.value)
            }
        }
    }

    /// The content that is displayed when the `DisclosureGroup` is expanded
    @Composable
    private fun ExpansionStack() {
        AnimatedVisibility(
            visible =vm.expansion.value != ProjectExpansionLevel.COMPACT,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                Divider(Modifier.padding(top = vm.divTopPadding))
                SystemPicker()
                ExpansionMenu()
                ExpansionView()
            }
        }
    }

    /// Selection control between `YkSystem` variations, such as SI or IMPERIAL
    @Composable
    private fun SystemPicker() {
        if (vm.expansion.value != ProjectExpansionLevel.EDITABLE) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                YkSystem.entries.forEach { system ->
                    val isSelected = vm.convertToSystem.value == system
                    Button(
                        onClick = { vm.toggleSystem(system) },
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                        ,
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = pickerColor(isSelected),
                            contentColor = pickerTextColor
                        )
                    ) {
                        Text(text = system.toString())
                    }
                }
            }
        }
    }

    /// Selects which icon to use when no image was provided in the project based on the project id,
    /// selecting from one of the 7 `noImageIcon` resources
    private val noImageIconResource: Int
        get() {
            return when(vm.project.value.id.first().code % 7) {
                1 -> R.drawable.noimageicons1
                2 -> R.drawable.noimageicons2
                3 -> R.drawable.noimageicons3
                4 -> R.drawable.noimageicons4
                5 -> R.drawable.noimageicons5
                6 -> R.drawable.noimageicons6
                else -> R.drawable.noimageicons0
            }
        }

    /// The image representing the project, either a generic icon, or a user-specified image
    @Composable
    private fun ProjectImage() {
        Surface(
            shape = imageShape,
            shadowElevation = 2.dp,
            color = grayBackground
        ) {
            Image(
                painter = painterResource(id = noImageIconResource),
                contentDescription = "Icon for ${vm.editedName.value}",
                modifier = Modifier
                    .size(vm.imageSize)
                    .padding(vm.imageSize / 8)
                    .background(Color.Transparent)
                ,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
            )
        }
    }

    private val imageShape: CornerBasedShape
        @Composable
        get() {
            return if (vm.expansion.value == ProjectExpansionLevel.EDITABLE)
                MaterialTheme.shapes.large
            else
                MaterialTheme.shapes.medium
        }

    /// The title of the project, which is the `.name` field in the `YkProject`
    @Composable
    private fun NameField() {
        val project = vm.project.collectAsState()

        when (vm.expansion.value) {
            ProjectExpansionLevel.EDITABLE -> {
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
            else -> {
                Text(
                    text = project.value.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    /// The subtitle of the project, which is the `.about` field in the `YkProject`
    @Composable
    private fun DescriptionField() {
        val project = vm.project.collectAsState()

        when (vm.expansion.value) {
            ProjectExpansionLevel.EDITABLE -> {
                BasicTextFieldWithHint(
                    value = vm.editedDescription.value,
                    hint = "description",
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    onValueChange = { vm.updateDescription(it) },
                )
            }
            else -> {
                Text(
                    text = project.value.about,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }

    /// When the `DisclosureGroup` is expanded, this will be inside, and will either contain the editable content when opened in a `.sheet`, or static text
    @Composable
    private fun ExpansionView() {
        ExpansionMeasurements()
    }

    @Composable
    private fun ExpansionMeasurements() {
        ExpansionMeasurementsList()
    }

    /// A `ForEach` corresponding to each of the measurements, in either editable or static form
    @Composable
    private fun ExpansionMeasurementsList() {
        val viewModel = remember { vm }
        Column(
            modifier = if (vm.expansion.value == ProjectExpansionLevel.STATIC) {
                Modifier.clickable {
                    mainViewModel?.toggleEdit(vm.project.value)
                    }
            } else {
                Modifier
            }
        ) {
            viewModel.measurements.value.forEach { measurement ->
                when (vm.expansion.value) {
                    ProjectExpansionLevel.EDITABLE -> EditableMeasurement(measurement)
                    else -> StaticMeasurement(measurement)
                }
            }
            if (viewModel.measurements.value.isEmpty()) {
                Text("Add New Measurements")
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

    /// In the `expansionMeasurementList`, this is a single measurement that is not editable
    @Composable
    private fun StaticMeasurement(measurement: YkMeasurement) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(2.dp)
        ) {
            Divider()
            Text(measurement.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(measurement.about,
                style = MaterialTheme.typography.labelMedium
            )
            Text(measurement.convertToSystem(vm.convertToSystem.value).valueString)
        }
    }

    /// If editable, this will display the `expansionPlusMinusStack`
    @Composable
    private fun ExpansionMenu() {
        if (vm.expansion.value == ProjectExpansionLevel.EDITABLE) {
            ExpansionPlusMinusStack()
        }
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
                imageVector = Icons.Default.Remove,
                contentDescription = "Allow deleting measurements",
                modifier = Modifier.editButtonModifier(),
                tint = MaterialTheme.colorScheme.primary
            )
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
