package view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import model.ProjectExpansionLevel
import model.YkMeasurement
import model.YkSystem
import org.jetbrains.compose.resources.stringResource
import viewmodel.MainViewModel
import viewmodel.ProjectViewModel
import viewmodel.ProjectViewViews
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.add_new_measurements
import youkon.composeapp.generated.resources.choose_a_system
import youkon.composeapp.generated.resources.measurement_description_blank
import youkon.composeapp.generated.resources.measurement_name_blank
import youkon.composeapp.generated.resources.swipe_for_options
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.times

/// The `ProjectView` displays the data from a `YkProject`.
/// Initially shown with an icon, name, and description, in "Compact" mode.
/// When tapped, the view will expand similar to show the measurements in "Static" mode.
class ProjectView(
    private val vm: ProjectViewModel,
    private val mainViewModel: MainViewModel? = null,
    private val unitsAreExtended: Boolean = false
) {
    /// Provides a view modifier for a colored shadow if the selected view is highlighted in the onboarding screen
    private fun Modifier.onboardingModifier(view: ProjectViewViews): Modifier = composed {
        this.onboardingModifier(vm.highlightedView.value == view)
    }

    /// The disclosure group with static content inside, with label with name and description
    @Composable
    fun Body() {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.onboardingModifier(ProjectViewViews.COMPACT)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = Constants.bodyStartPadding, end = Constants.bodyEndPadding)
                    .padding(vertical = Constants.bodyStartPadding)
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
                .onboardingModifier(ProjectViewViews.STATIC)
                .clickable { vm.toggleExpansion() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProjectImage(
                project = vm.project.value,
                imageSize = Constants.imageSize,
                imageShape = MaterialTheme.shapes.medium
            )
            Spacer(Modifier.width(Constants.labelSpacerWidth))
            Column(modifier = Modifier.weight(1f)) {
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
            visible = vm.expansion.value != ProjectExpansionLevel.COMPACT,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                HorizontalDivider(Modifier.padding(top = Constants.divTopPadding))
                SystemPicker()
                ExpansionMeasurementsList()
            }
        }
    }

    /// Selection control between `YkSystem` variations, such as SI or IMPERIAL
    @Composable
    private fun SystemPicker() {
        Column(Modifier.onboardingModifier(ProjectViewViews.SYSTEM_PICKER)) {
            Text(
                text = stringResource(Res.string.choose_a_system),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleSmall
            )

            // Check if content is scrollable, if it is, show the "swipe_for_options" text
            val lazyListState = rememberLazyListState()
            if (lazyListState.canScrollForward || lazyListState.canScrollBackward) {
                Text(
                    text = stringResource(Res.string.swipe_for_options),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            LazyRow(
                state = lazyListState,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(YkSystem.entries.count()) { idx ->
                    val system = YkSystem.entries[idx]
                    val isSelected = vm.convertToSystem.value == system
                    if (unitsAreExtended || system.basicSystems.contains(system)) {
                        SystemPickerButton(system, isSelected)
                    }
                }
            }
        }
    }

    /// The individual buttons representing a system of units
    @Composable
    private fun SystemPickerButton(system: YkSystem, isSelected: Boolean) {
        Button(
            onClick = { vm.toggleSystem(system) },
            modifier = Modifier.width(Constants.systemPickerButtonWidth),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = pickerColor(isSelected),
                contentColor = pickerTextColor(isSelected)
            ),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = system.toString(),
                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal
                )
                Text(system.text, style = MaterialTheme.typography.labelSmall)
            }
        }
    }

    /// The title of the project, which is the `.name` field in the `YkProject`
    @Composable
    private fun NameField() {
        val project = vm.project.collectAsState()
        Text(
            text = project.value.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )
    }

    /// The subtitle of the project, which is the `.about` field in the `YkProject`
    @Composable
    private fun DescriptionField() {
        val project = vm.project.collectAsState()
        Text(
            text = project.value.about,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = Constants.descriptionBottomPadding),
        )
    }

    /// A `ForEach` corresponding to each of the measurements, in either editable or static form
    @Composable
    private fun ExpansionMeasurementsList() {
        val project = vm.project.collectAsState()

        Column(
            modifier = Modifier
                .onboardingModifier(ProjectViewViews.STATIC_MEASUREMENTS)
                .clickable {
                    mainViewModel?.startEditing(vm.project.value)
                }
        ) {
            project.value.measurements.forEach { measurement ->
                StaticMeasurement(measurement)
            }

            // I had a confusing issue where the two conditional state updates would fire
            // at different times, and would end up with either a situation where the message below
            // would go away from going from empty to adding one measurement, or when adding one
            // new measurement the editable form would not update. Adding multiple conditionals
            // seems to make sure they both fire on state updates, though its real ugly code given
            // that both states are supposed to mean the same thing. Someone please fix.
            if (vm.measurements.value.isEmpty()) {
                if (project.value.measurements.isEmpty()) {
                    Text(stringResource(Res.string.add_new_measurements))
                }
            }
        }
    }

    /// In the `expansionMeasurementList`, this is a single measurement that is not editable
    @Composable
    private fun StaticMeasurement(measurement: YkMeasurement) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(Constants.staticMeasurementPadding)
        ) {
            HorizontalDivider()
            Text(
                measurement.name.ifBlank { stringResource(Res.string.measurement_name_blank) },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                measurement.about.ifBlank { stringResource(Res.string.measurement_description_blank) },
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            TextWithSubscripts(measurement.convertToSystem(vm.convertToSystem.value).valueString,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }

    private class Constants {
        companion object {
            val bodyBackgroundAlpha = 0.4f
            val bodyStartPadding = 16.dp
            val bodyEndPadding = 8.dp
            val labelSpacerWidth = 8.dp
            val descriptionBottomPadding = 4.dp
            val imageSize = 36.dp
            val divTopPadding = 4.dp
            val systemPickerButtonWidth = 96.dp
            val staticMeasurementPadding = 2.dp
        }
    }
}
