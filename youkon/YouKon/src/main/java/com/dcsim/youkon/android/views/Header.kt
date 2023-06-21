package com.dcsim.youkon.android.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.android.R


@Composable
fun Header() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_clearbackground),
            contentDescription = "App icon",
            modifier = Modifier.width(90.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.surface)
        )
        Text("YouKon",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h2
        )
    }
}