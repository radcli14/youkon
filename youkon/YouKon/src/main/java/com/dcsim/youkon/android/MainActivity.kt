package com.dcsim.youkon.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.dcsim.youkon.android.viewmodels.MainViewModel
import com.dcsim.youkon.android.viewmodels.ProjectsCardViewModel
import com.dcsim.youkon.android.viewmodels.QuickConvertCardViewModel
import com.dcsim.youkon.android.views.MainView

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val quickConvertCardViewModel: QuickConvertCardViewModel by viewModels()
    private lateinit var projectsCardViewModel: ProjectsCardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // The Projects card are dependent on user data that is contained in the `mainViewModel`
        projectsCardViewModel = ProjectsCardViewModel(mainViewModel.user)

        // Any time the user closes an editing dialog, save the user data to a json file
        mainViewModel.isEditingProject.observe(this) { isEditing ->
            if (!isEditing) {
                mainViewModel.saveUserToJson()
            }
        }

        setContent {
            MainView(
                mainViewModel,
                quickConvertCardViewModel,
                projectsCardViewModel
            ).Body()
        }
    }
}
