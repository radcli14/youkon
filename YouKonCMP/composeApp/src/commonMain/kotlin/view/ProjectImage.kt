package view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import model.YkProject
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


/// Selects which icon to use when no image was provided in the project based on the project id,
/// selecting from one of the 7 `noImageIcon` resources
private fun noImageIconString(id: String): String {
    return "noimageicons${id.first().code % 7}.png"
}

/// The image representing a project, either a generic icon, or a user-specified image
@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProjectImage(project: YkProject, imageSize: Dp, imageShape: CornerBasedShape) {
    Surface(
        shape = imageShape,
        shadowElevation = 2.dp,
        color = grayBackground
    ) {
        Image(
            painter = painterResource(noImageIconString(project.id)),
            contentDescription = "Icon for ${project.name}",
            modifier = Modifier
                .size(imageSize)
                .padding(imageSize / 8)
                .background(Color.Transparent)
            ,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
        )
    }
}
