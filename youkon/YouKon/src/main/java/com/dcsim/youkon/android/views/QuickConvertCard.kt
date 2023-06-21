package com.dcsim.youkon.android.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuickConvertCard() {
    Card(
        modifier = Modifier
            .requiredWidth(360.dp)
            .requiredHeight(128.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Quick Convert", style = MaterialTheme.typography.body2)
        }
    }
}