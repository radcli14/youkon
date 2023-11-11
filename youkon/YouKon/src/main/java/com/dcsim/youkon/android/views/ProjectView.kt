package com.dcsim.youkon.android.views

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.ProjectExpansionLevel
import com.dcsim.youkon.YkMeasurement
import com.dcsim.youkon.android.R
import com.dcsim.youkon.android.viewmodels.MainViewModel
import com.dcsim.youkon.android.viewmodels.ProjectViewModel

//@Composable
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
        /*
        .alert(isPresented: $vc.showSubtractAlert) {
            SubtractAlert(
                title: vc.measurementToDelete?.name ?? "",
                confirmAction: {
                    vc.confirmDelete()
                    contentViewController.saveUserToJson()
                },
                cancelAction: vc.cancelDelete
            )
        }
         */
    }

    /// The disclosure group with static content inside, with label with name and description
    @Composable
    private fun DisclosureGroupWhenNotEditing() {
        Surface(
            color = MaterialTheme.colors.surface.copy(alpha = 0.4f),
            shape = RoundedCornerShape(8.dp),
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
            modifier = Modifier
                .fillMaxWidth()
                .clickable { vm.toggleExpansion() },
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProjectImage()
            Column {
                NameField()
                DescriptionField()
            }
            if (vm.expansion.value != ProjectExpansionLevel.EDITABLE) {
                Spacer(Modifier.weight(1f))
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
            //Divider()
            Column {
                Divider(Modifier.padding(top = vm.divTopPadding))
                SystemPicker()
                Row(verticalAlignment = Alignment.Top) {
                    ExpansionView()
                    Spacer(Modifier.weight(1f))
                    ExpansionMenu()
                }
            }
        }
    }

    @Composable
    private fun SystemPicker() {
        /*
        if vc.expansion != .editable {
            Picker("System", selection: $vc.convertToSystem) {
            ForEach(YkSystem.entries, id: \.self) { option in
                Text(String(describing: option))
        }
        }
            .pickerStyle(.segmented)
        }
         */
    }

    /// The image representing the project, either a generic icon, or a user-specified image
    @Composable
    private fun ProjectImage() {
        Surface(
            shape = RoundedCornerShape(vm.imageSize / 4),
            elevation = 2.dp
        ) {
            Image(
                painter = painterResource(id = R.drawable.noimageicons0),
                contentDescription = "Icon for ${vm.editedName.value}",
                modifier = Modifier
                    .size(vm.imageSize)
                    //.padding(vm.imageSize / 8)
                    .background(Color.Gray.copy(alpha = 0.3f))
                ,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
            )
        }
    }

    /// The title of the project, which is the `.name` field in the `YkProject`
    @Composable
    private fun NameField() {
        val project = vm.project.collectAsState()

        when (vm.expansion.value) {
            ProjectExpansionLevel.EDITABLE -> {
                BasicTextField(
                    value = vm.editedName.value,
                    onValueChange = { vm.updateName(it) },
                    textStyle = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold),
                )
            }
            else -> {
                Text(
                    text = project.value.name,
                    style = MaterialTheme.typography.subtitle1,
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
                BasicTextField(
                    value = vm.editedDescription.value,
                    onValueChange = { vm.updateDescription(it) },
                    textStyle = MaterialTheme.typography.body1
                )
            }
            else -> {
                Text(
                    text = project.value.about,
                    style = MaterialTheme.typography.body1,
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
        Column(
            modifier = Modifier.clickable {
                //vm.toggleEdit()
                mainViewModel?.toggleEdit(vm.project.value)
            }
        ) {
            vm.measurements.value.forEach { measurement ->
                when (vm.expansion.value) {
                    ProjectExpansionLevel.EDITABLE -> EditableMeasurement(measurement)
                    else -> StaticMeasurement(measurement)
                }
            }
        }
        if (vm.measurements.value.isEmpty()) {
            Text("Add New Measurements")
        }
    }

    /// In the `expansionMeasurementList`, this is a single measurement that is editable
    @Composable
    private fun EditableMeasurement(measurement: YkMeasurement) {
        Row {
            SubtractMeasurementButton(measurement)
            MeasurementView(measurement).Body()
        }
    }

    /// In the `expansionMeasurementList`, this is a single measurement that is not editable
    @Composable
    private fun StaticMeasurement(measurement: YkMeasurement) {
        Column(horizontalAlignment = Alignment.Start) {
            Divider()
            Text(measurement.name,
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Bold
            )
            Text(measurement.about,
                style = MaterialTheme.typography.caption
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
        Column {
            PlusButton()
            MinusButton()
        }
    }

    @Composable
    fun PlusButton() {
        IconButton(
            onClick = {
                vm.addMeasurement()
                //mainViewModel?.saveUserToJson()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add a new measurement",
                modifier = Modifier.editButtonModifier(),
                tint = MaterialTheme.colors.primary
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
                contentDescription = "Allow deleting projects",
                modifier = Modifier.editButtonModifier(),
                tint = MaterialTheme.colors.primary
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
                        color = MaterialTheme.colors.error,
                        alpha = 1f,
                        width = 24.dp,
                        height = 24.dp,
                        padding = 4.dp,
                        shape = CircleShape
                    ),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}
