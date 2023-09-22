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
import com.dcsim.youkon.YkProject
import com.dcsim.youkon.YkUser
import com.dcsim.youkon.android.views.BackgroundBox
import com.dcsim.youkon.android.views.Header
import com.dcsim.youkon.android.views.ProjectsCard
import com.dcsim.youkon.android.views.QuickConvertCard

class MainActivity : ComponentActivity() {
    val user = YkUser()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user.setAsTestUser()
        println("Set Test User")
        println(user.asJsonString())
        user.projects.forEach { project ->
            println(project)
            println(project.asJsonString())
            project.measurements.forEach { measurement ->
                println(measurement)
                println(measurement.asJsonString())
            }
        }

        setContent {
            MainView(user.projects)
        }
    }
}

@Composable
fun MainView(projects: List<YkProject>) {
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
                    QuickConvertCard()
                    ProjectsCard(projects)
                }
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    val user = YkUser()
    user.setAsTestUser()
    MainView(user.projects)
}
