package com.dcengineer.youkon

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import model.wembyProject
import purchases.PurchasesViewModel
import view.ProjectViewWhenEditing
import viewmodel.ProjectViewModel


@Preview
@Composable
fun ProjectViewWhenEditingPreview() {
    val viewModel = ProjectViewModel(wembyProject())
    val purchases = PurchasesViewModel()
    viewModel.toggleExpansion()
    ProjectViewWhenEditing(viewModel, purchases).Body()
}


