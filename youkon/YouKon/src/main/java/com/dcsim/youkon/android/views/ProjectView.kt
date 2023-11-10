package com.dcsim.youkon.android.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.ProjectExpansionLevel
import com.dcsim.youkon.YkMeasurement
import com.dcsim.youkon.android.viewmodels.ProjectViewModel

//@Composable
class ProjectView(
    private val vm: ProjectViewModel
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
        Column(horizontalAlignment = Alignment.Start) {
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

    private val imageSize: Dp
        get() = if (vm.expansion.value == ProjectExpansionLevel.EDITABLE) 48.dp else 32.dp

    /// The disclosure group with static content inside, with label with name and description
    @Composable
    private fun DisclosureGroupWhenNotEditing() {
        Surface(
            color = MaterialTheme.colors.surface.copy(alpha = 0.4f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .clickable {
                    vm.expansion.value = when (vm.expansion.value) {
                        ProjectExpansionLevel.COMPACT -> ProjectExpansionLevel.STATIC
                        ProjectExpansionLevel.STATIC -> ProjectExpansionLevel.COMPACT
                        ProjectExpansionLevel.EDITABLE -> ProjectExpansionLevel.STATIC
                    }
                }
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                NameField()
                DescriptionField()
            }
            CloseIcon(vm.expansion.value)
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
            Divider()
            Column {
                Divider()
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
        /*
        Image("noImageIcons\((vc.project.id.first?.wholeNumberValue ?? 0) % 7)")
            .resizable()
            .frame(width: imageSize, height: imageSize)
            .padding(imageSize / 8.0)
            .colorInvert()
            .colorMultiply(.primary)
        .background(.gray.opacity(0.3))
        .clipShape(RoundedRectangle(cornerRadius: imageSize / 4))
        .shadow(radius: 1)
         */
    }

    /// The title of the project, which is the `.name` field in the `YkProject`
    @Composable
    private fun NameField() {
        when (vm.expansion.value) {
            ProjectExpansionLevel.EDITABLE -> {
                /*
                TextField("Name", text: $vc.editedName)
                    .font(.title3)
                    .fontWeight(.bold)
                    .onChange(of: vc.editedName) { name in
                        vc.project.name = name
                }
                 */
            }
            else -> {
                Text(
                    text = vm.project.name,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    /// The subtitle of the project, which is the `.about` field in the `YkProject`
    @Composable
    private fun DescriptionField() {
        when (vm.expansion.value) {
            ProjectExpansionLevel.EDITABLE -> {
                /*
                TextField("Description", text: $vc.editedDescription)
                    .onChange(of: vc.editedDescription) { description in
                        vc.project.about = description
                }
                 */
            }
            else -> {
                Text(
                    text = vm.editedDescription.value,
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
        /*
        ScrollView {
            VStack(alignment: .leading) {
            expansionMeasurementsList
        }
            .onTapGesture {
                if vc.expansion != .editable {
                    contentViewController.toggleEdit(to: vc.project)
                }
            }
        }
         */
    }

    /// A `ForEach` corresponding to each of the measurements, in either editable or static form
    @Composable
    private fun ExpansionMeasurementsList() {
        Column {
            vm.measurements.value.forEach { measurement ->
                when (vm.expansion.value) {
                    ProjectExpansionLevel.EDITABLE -> EditableMeasurement(measurement)
                    else -> StaticMeasurement(measurement)
                }
            }
        }
        if (vm.measurements.value.isEmpty()) {
            Text("Add New Measuremenbts")
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
                //mainViewModel.saveUserToJson()
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
