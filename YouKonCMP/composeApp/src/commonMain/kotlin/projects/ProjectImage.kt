package projects

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.util.toImageBitmap
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.launch
import model.YkProject
import okio.ByteString.Companion.decodeBase64
import okio.ByteString.Companion.toByteString
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import theming.grayBackground
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.icon_for_project
import youkon.composeapp.generated.resources.noimageicons0
import youkon.composeapp.generated.resources.noimageicons1
import youkon.composeapp.generated.resources.noimageicons2
import youkon.composeapp.generated.resources.noimageicons3
import youkon.composeapp.generated.resources.noimageicons4
import youkon.composeapp.generated.resources.noimageicons5
import youkon.composeapp.generated.resources.noimageicons6
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import androidx.compose.ui.graphics.ImageBitmap as ComposeImageBitmap

/// The image representing a project, either a generic icon, or a user-specified image
@OptIn(ExperimentalEncodingApi::class)
@Composable
fun ProjectImage(
    project: YkProject,
    imageSize: Dp,
    imageShape: CornerBasedShape,
    onSelectNewImage: ((PlatformFile) -> Unit)? = null
) {

    val coroutineScope = rememberCoroutineScope()
    val imageBitmap: MutableState<ComposeImageBitmap?> = remember { mutableStateOf(null) }

    Surface(
        onClick = {
            onSelectNewImage?.let { fcn ->
                coroutineScope.launch {
                    val imageFile = FileKit.openFilePicker(type = FileKitType.Image)
                    imageFile?.let { fcn(it) }
                }
            }
        },
        shape = imageShape,
        shadowElevation = 2.dp,
        color = grayBackground,
    ) {
        Log.d("ProjectImage", "started ${project.images.first()}")
        if (project.images.isNotEmpty()) {
            val imageString = project.images.first()
            val imageBytes = Base64.decode(imageString)
            imageBitmap.value = imageBytes.decodeToImageBitmap()
            Log.d("ProjectImage", "decoded imageBitmap")
        }

        imageBitmap.value?.let { bitmap ->
            Image(
                painter = BitmapPainter(bitmap),
                contentDescription = stringResource(Res.string.icon_for_project).replace("$1%s", project.name),
                modifier = Modifier
                    .size(imageSize)
                    .padding(imageSize / 8)
                    .background(Color.Transparent)
            )
        } ?: run {
            NoImageIcon(project, size = imageSize)
        }
    }
}


@Composable
fun NoImageIcon(project: YkProject, size: Dp = 64.dp) {
    Image(
        painter = painterResource(noImageIcon(project.id)),
        contentDescription = stringResource(Res.string.icon_for_project).replace("$1%s", project.name),  // TODO: this shouldn't require replace, try again after updating compose
        modifier = Modifier
            .size(size)
            .padding(size / 8)
            .background(Color.Transparent)
        ,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
    )
}

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
