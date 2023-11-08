package com.dcsim.youkon.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.android.viewmodels.MainViewModel
import com.dcsim.youkon.android.viewmodels.ProjectsCardViewModel
import com.dcsim.youkon.android.viewmodels.QuickConvertCardViewModel
import com.dcsim.youkon.android.views.BackgroundBox
import com.dcsim.youkon.android.views.Header
import com.dcsim.youkon.android.views.ProjectsCard
import com.dcsim.youkon.android.views.QuickConvertCard

class MainActivity : ComponentActivity() {
    private lateinit var mainViewModel: MainViewModel
    private val quickConvertCardViewModel = QuickConvertCardViewModel()
    private lateinit var projectsCardViewModel: ProjectsCardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = MainViewModel()
        projectsCardViewModel = ProjectsCardViewModel(mainViewModel.user)

        setContent {
            MainView(mainViewModel, quickConvertCardViewModel, projectsCardViewModel)
        }
    }
}

@Composable
fun MainView(
    mainViewModel: MainViewModel,
    quickConvertCardViewModel: QuickConvertCardViewModel,
    projectsCardViewModel: ProjectsCardViewModel
) {
    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            BackgroundBox {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Header()
                    QuickConvertCard(quickConvertCardViewModel).Body()
                    ProjectsCard(projectsCardViewModel).Body()
                }
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MainView(MainViewModel(), QuickConvertCardViewModel(), ProjectsCardViewModel())
}
