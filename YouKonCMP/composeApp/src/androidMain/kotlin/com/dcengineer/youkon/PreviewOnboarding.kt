package com.dcengineer.youkon

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import view.OnboardingScreen
import viewmodel.OnboardingScreenViewModel


@Preview
@Composable
fun OnboardingPreview() {
    val viewModel = OnboardingScreenViewModel()
    viewModel.isWide = false
    OnboardingScreen(viewModel).Body()
}
