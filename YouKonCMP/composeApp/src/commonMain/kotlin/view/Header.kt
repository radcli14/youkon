package view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.header

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Header() {
    Image(
        painter = painterResource(Res.drawable.header),
        contentDescription = "App header",
        modifier = Modifier
            .requiredHeightIn(min = 64.dp, max = 96.dp)
            .fillMaxWidth(),
        contentScale = ContentScale.Fit,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
    )
}