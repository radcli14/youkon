package com.dcsim.youkon.android.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.YkProject
import com.dcsim.youkon.android.viewmodels.MainViewModel
import com.dcsim.youkon.android.viewmodels.ProjectsCardViewModel

class ProjectsCard(
    private val vm: ProjectsCardViewModel = ProjectsCardViewModel(),
    private val mainViewModel: MainViewModel = MainViewModel()
) {
    @Composable
    fun Body() {
        Surface(
            color = MaterialTheme.colors.surface.copy(alpha = 0.4f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LabelStack()
                ProjectContent()
            }
        }
    }

    @Composable
    fun LabelStack() {
        Row(verticalAlignment = Alignment.CenterVertically) {  //(spacing: 8) {
            Text(
                text = "Projects",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.SemiBold
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
                vm.addProject()
                //mainViewModel.saveUserToJson()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add a new project",
                modifier = Modifier.editButtonModifier(),
                tint = MaterialTheme.colors.primary
            )
        }
    }

    @Composable
    fun MinusButton() {
        IconButton(
            onClick = { vm.onSubtractButtonTap() }
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Allow deleting projects",
                modifier = Modifier.editButtonModifier(),
                tint = MaterialTheme.colors.primary
            )
        }
    }

    @Composable
    fun ProjectContent() {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(vm.projects.value) { project ->
                Row(verticalAlignment = Alignment.CenterVertically) {
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
