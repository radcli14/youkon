package com.dcsim.youkon.android.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
                LabelStack()
                LazyColumn {
                    items(vm.projects.value) { project ->
                        ProjectView(project = project)
                    }
                }
            }
        }
    }

    @Composable
    fun LabelStack() {
        Row {  //(spacing: 8) {
            Text(
                text = "Projects",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.SemiBold,
                //modifier = Modifier.padding(bottom = 16.dp)
            )
            //Spacer()
            PlusButton()
            MinusButton()
        }
    }

    @Composable
    fun PlusButton() {
        /*
                Button(action: {
            vc.addProject()
            contentViewController.saveUserToJson()
        }) {
            Image(systemName: "plus")
                .frame(height: 24)
        }
        .buttonStyle(.bordered)
         */
    }

    @Composable
    fun MinusButton() {
        /*
        Button(action: vc.onSubtractButtonTap) {
            Image(systemName: "minus")
            .frame(height: 24)
        }
            .buttonStyle(.bordered)
         */
    }

    /*
    private var projectColumns: [GridItem] {
        Array(
            repeating: GridItem(.flexible()),
            count: UIDevice.current.userInterfaceIdiom == .phone ? 1 : 2
        )
    }
     */

    @Composable
    fun ProjectContent() {
        /*
        ScrollView {
            LazyVGrid(columns: projectColumns, spacing: 16) {
                ForEach(vc.projects, id: \.id) { project in
                    HStack {
                        subtractProjectButton(project)
                        let pvc = vc.projectViewController(for: project)
                        ProjectView(pvc)
                    }
                    .animation(.easeInOut, value: vc.canSubtract)
                }
            }
        }
         */

        /// The red `X` that shows up to the left of a project when the user has enabled subtracting projects
        /*
        private fun subtractProjectButton(_ project: YkProject) -> some View {
            if vc.canSubtract {
                Button(
                    action: {
                    vc.subtract(project: project)
                }
                ) {
                Image(systemName: "x.circle.fill")
                .foregroundColor(.pink)
                .font(.title2)
            }
            }
        }
         */
    }
}
