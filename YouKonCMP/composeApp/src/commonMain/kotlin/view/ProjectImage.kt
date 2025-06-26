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
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.icon_for_project
import youkon.composeapp.generated.resources.noimageicons0
import youkon.composeapp.generated.resources.noimageicons1
import youkon.composeapp.generated.resources.noimageicons2
import youkon.composeapp.generated.resources.noimageicons3
import youkon.composeapp.generated.resources.noimageicons4
import youkon.composeapp.generated.resources.noimageicons5
import youkon.composeapp.generated.resources.noimageicons6

/// Selects which icon to use when no image was provided in the project based on the project id,
/// selecting from one of the 7 `noImageIcon` resources
private fun noImageIcon(id: String): DrawableResource {
    return when(id.first().code % 7) {
        1 -> Res.drawable.noimageicons1
        2 -> Res.drawable.noimageicons2
        3 -> Res.drawable.noimageicons3
        4 -> Res.drawable.noimageicons4
        5 -> Res.drawable.noimageicons5
        6 -> Res.drawable.noimageicons6
        else -> Res.drawable.noimageicons0
    }
}

/// The image representing a project, either a generic icon, or a user-specified image
@Composable
fun ProjectImage(project: YkProject, imageSize: Dp, imageShape: CornerBasedShape) {
    Surface(
        shape = imageShape,
        shadowElevation = 2.dp,
        color = grayBackground
    ) {
        Image(
            painter = painterResource(noImageIcon(project.id)),
            contentDescription = stringResource(Res.string.icon_for_project, project.name),
            modifier = Modifier
                .size(imageSize)
                .padding(imageSize / 8)
                .background(Color.Transparent)
            ,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
        )
    }
}
