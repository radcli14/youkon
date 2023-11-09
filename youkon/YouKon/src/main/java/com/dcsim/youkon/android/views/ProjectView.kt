package com.dcsim.youkon.android.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.YkMeasurement
import com.dcsim.youkon.YkProject
import com.dcsim.youkon.ProjectExpansionLevel
import com.dcsim.youkon.YkSystem
import com.dcsim.youkon.android.viewmodels.ProjectViewModel

//@Composable
class ProjectView(
    val vm: ProjectViewModel
) {
    @Composable
    fun Body() {
        var expansion by remember { mutableStateOf(ProjectExpansionLevel.COMPACT) }
        var measurements by remember { mutableStateOf(vm.project.measurements) }

        Surface(
            color = MaterialTheme.colors.surface.copy(alpha = 0.4f),
            modifier = Modifier
                .clickable {
                    expansion = when (expansion) {
                        ProjectExpansionLevel.COMPACT -> ProjectExpansionLevel.STATIC
                        ProjectExpansionLevel.STATIC -> ProjectExpansionLevel.COMPACT
                        ProjectExpansionLevel.EDITABLE -> ProjectExpansionLevel.STATIC
                    }
                }
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProjectTopRow(vm.project, expansion)
                ProjectContent(measurements, expansion,
                    editClick = {
                        expansion = when (expansion) {
                            ProjectExpansionLevel.STATIC -> ProjectExpansionLevel.EDITABLE
                            else -> ProjectExpansionLevel.STATIC
                        }
                    },
                    addClick = {
                        println("newNewNew")
                        vm.project.measurements.add(
                            YkMeasurement.new()
                        )
                        measurements = vm.project.measurements
                        println(measurements)
                    }
                )
            }
        }
    }

    /// The list of editable measurements when the project is opened in a sheet for editing
    @Composable
    fun MainStackWhenEditing() {
        /*
        VStack(alignment: .leading) {
            labelStack
            expansionStack
        }
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
        /*
        DisclosureGroup(isExpanded: $vc.isExpanded) {
            expansionStack
        } label: {
            labelStack
        }
                .padding()
            .background(Color.gray.opacity(0.2))
            .cornerRadius(8)
         */
    }

    /// The name and description shown at the top of the project
    @Composable
    private fun LabelStack() {
        /*
        HStack {
            projectImage
            VStack(alignment: .leading) {
            nameField
            descriptionField
        }
            .foregroundStyle(.foreground)
        }
         */
    }

    /// The content that is displayed when the `DisclosureGroup` is expanded
    @Composable
    private fun ExpansionStack() {
        /*
        VStack {
            Divider()
            systemPicker
            HStack(alignment: .top) {
            expansionView
            Spacer()
            expansionMenu
        }
        }
         */
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
        /*
        switch (vc.expansion) {
            case .editable:
            TextField("Name", text: $vc.editedName)
            .font(.title3)
            .fontWeight(.bold)
            .onChange(of: vc.editedName) { name in
                vc.project.name = name
        }
            default:
            Text(vc.editedName)
                .font(.headline)
            .fontWeight(.bold)
        }
         */
    }

    /// The subtitle of the project, which is the `.about` field in the `YkProject`
    @Composable
    private fun DescriptionField() {
        /*
        switch (vc.expansion) {
            case .editable:
            TextField("Description", text: $vc.editedDescription)
            .onChange(of: vc.editedDescription) { description in
                vc.project.about = description
        }
            .font(.body)
            default:
            Text(vc.editedDescription)
                .font(.caption)
        }
         */
    }

    /// When the `DisclosureGroup` is expanded, this will be inside, and will either contain the editable content when opened in a `.sheet`, or static text
    @Composable
    private fun ExpansionView() {
        //expansionMeasurements
    }

    @Composable
    private fun ExpansionMeasurements() {
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
        /*
        ForEach(vc.measurements, id: \.id) { measurement in
                switch (vc.expansion) {
                    case .editable: editableMeasurement(measurement)
                    default: staticMeasurement(measurement)
                }
        }
        if vc.measurements.isEmpty {
            Text("Add New Measurements")
        }

         */
    }

    /// In the `expansionMeasurementList`, this is a single measurement that is editable
    @Composable
    private fun EditableMeasurement(measurement: YkMeasurement) {
        /*
        HStack {
            subtractMeasurementButton(measurement)
            MeasurementView(measurement: measurement)
        }
            .animation(.easeInOut, value: vc.canSubtract)

         */
    }

    /// In the `expansionMeasurementList`, this is a single measurement that is not editable
    @Composable
    private fun staticMeasurement(measurement: YkMeasurement) {
        /*
        VStack(alignment: .leading) {
            Divider()
            Text(measurement.name)
                .font(.caption)
            .fontWeight(.bold)
            Text(measurement.about)
                .font(.caption2)
            .foregroundStyle(.secondary)
            Text(measurement.convertToSystem(targetSystem: vc.convertToSystem).valueString)
        }
            .multilineTextAlignment(.leading)

         */
    }

    /// If editable, this will display the `expansionPlusMinusStack`
    @Composable
    private fun ExpansionMenu() {
        /*
        if vc.expansion == .editable {
            expansionPlusMinusStack
        }
         */
    }

    /// The plus and minus buttons on the right hand side when editing, to create or delete measurements
    @Composable
    private fun ExpansionPlusMinusStack() {
        /*
        VStack {
            Button(action: vc.addMeasurement) {
            Image(systemName: "plus")
            .frame(height: 24)
        }
            Button(action: vc.subtractMeasurement) {
            Image(systemName: "minus")
            .frame(height: 24)
        }
        }
            .buttonStyle(.bordered)
        .foregroundColor(.indigo)
         */
    }

    /// The red `X` that shows up to the left of a measurement when the user has enabled subtracting measurements
    @Composable
    private fun subtractMeasurementButton(measurement: YkMeasurement) {
        /*
        if vc.canSubtract {
            Button(
                action: {
                vc.subtract(measurement: measurement)
            }
            ) {
            Image(systemName: "x.circle.fill")
            .foregroundColor(.pink)
            .font(.title2)
        }
        }

         */
    }
}


/// Top row with the name and description of the project, and button to expand/close
@Composable
fun ProjectTopRow(project: YkProject, expansion: ProjectExpansionLevel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            NameText(project.name)
            DescriptionText(project.about)
        }
        CloseIcon(expansion)
    }
}


/// Content with either static or editable measurements
@Composable
fun ProjectContent(
    measurements: List<YkMeasurement>,
    expansion: ProjectExpansionLevel,
    editClick: () -> Unit,
    addClick: () -> Unit
) {
    Row {
        Column {
            if (expansion == ProjectExpansionLevel.EDITABLE) {
                // Editable fields for each measurement and unit selection
                measurements.forEach { measurement ->
                    MeasurementView(measurement = measurement)
                }
            } else if (expansion == ProjectExpansionLevel.STATIC) {
                // Displays of the measurement after conversion to a consistent set of units
                measurements.forEach { measurement ->
                    Text(measurement.nameAndValueInSystem(YkSystem.SI))
                }
            }
        }

        // The edit button is visible when the project has been expanded beyond its compact level.
        // When it is tapped, the value, units, name, and description fields can be edited.
        // Tap again to collapse to the static level, in which you see but don't modify measurements.
        if (expansion != ProjectExpansionLevel.COMPACT) {
            Spacer(Modifier.weight(1f))
            Card {
                Column {
                    EditButton { editClick() }
                    if (expansion == ProjectExpansionLevel.EDITABLE) {
                        PlusButton { addClick() }
                        MinusButton { }
                    }
                }
            }
        }
    }
}
