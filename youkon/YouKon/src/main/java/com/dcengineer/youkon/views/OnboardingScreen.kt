package com.dcengineer.youkon.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class OnboardingScreen {
    private val screens = arrayOf(
        "Welcome",
        "Quick Convert Card",
        "Projects Card",
        "Project View",
        "Editable Project"
    )

    @Composable
    fun Body() {
        var currentPage by remember { mutableIntStateOf(0) }
        Column {
            when (currentPage) {
                0 -> Welcome()
                1 -> QuickConvertCard()
                2 -> ProjectsCard()
                3 -> ProjectView()
                4 -> EditingProject()
            }
            Tabs(currentPage) { currentPage = it }
        }
    }

    @Composable
    fun Tabs(currentPage: Int, onTabSelected: (Int) -> Unit) {
        TabRow(selectedTabIndex = currentPage) {
            screens.forEachIndexed { index, _ ->
                Tab(selected = index == currentPage, onClick = {
                    onTabSelected(index)
                }, modifier = Modifier.padding(16.dp)) {
                    TabIcon(index == currentPage)
                }
            }
        }
    }

    @Composable
    fun TabIcon(isCurrent: Boolean) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    color = tabColor(isCurrent),
                    shape = RoundedCornerShape(4.dp)
                )
        )
    }

    @Composable
    fun tabColor(isCurrent: Boolean): Color {
        return if (isCurrent) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.primaryContainer
        }
    }

    @Composable
    fun Welcome() {
        Text("Welcome")
    }

    @Composable
    fun QuickConvertCard() {
        Text("Quick Convert Card")
    }

    @Composable
    fun ProjectsCard() {
        Text("Projects Card")
    }

    @Composable
    fun ProjectView() {
        Text("Project View")
    }

    @Composable
    fun EditingProject() {
        Text("Editing Project")
    }
}

@Preview
@Composable
fun OnboardingPreview() {
    OnboardingScreen().Body()
}
