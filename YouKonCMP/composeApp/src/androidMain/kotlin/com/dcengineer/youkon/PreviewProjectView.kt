package com.dcengineer.youkon

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import model.wembyProject
import view.ProjectView
import viewmodel.ProjectViewModel


@Preview
@Composable
fun ProjectViewPreview() {
    val viewModel = ProjectViewModel(wembyProject())
    viewModel.toggleExpansion()
    ProjectView(viewModel).Body()
}
