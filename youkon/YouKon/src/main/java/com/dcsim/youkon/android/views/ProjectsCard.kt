package com.dcsim.youkon.android.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.YkProject
import com.dcsim.youkon.android.viewmodels.ProjectsCardViewModel

class ProjectsCard(
    val vm: ProjectsCardViewModel = ProjectsCardViewModel()
) {
    @Composable
    fun Body() {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Projects",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.SemiBold,
                    //modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn {
                    items(vm.projects.value) { project ->
                        ProjectView(project = project)
                    }
                }
            }
        }
    }
}
/*
@Composable
fun ProjectsCard(projects: List<YkProject>) {
    Card(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Projects",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.SemiBold,
                //modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn {
                items(projects) { project ->
                    ProjectView(project = project)
                }
            }
        }
    }
}
*/