package utilities

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.ImageFormat
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.compressImage
import io.github.vinceglb.filekit.readBytes
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class ImageFileManipulator {
    companion object {
        @OptIn(ExperimentalEncodingApi::class)
        fun getThumbnailImageFromImagesList(images: List<String>, tag: String = "ImageFileManipulator"): ImageBitmap? {
            images.firstOrNull()?.let { imageString ->
                try {
                    val imageBytes = Base64.decode(imageString)
                    return imageBytes.decodeToImageBitmap()
                }  catch (e: Exception) {
                    Log.e(tag, "Failed to decode base64 string to image: ${e.message}")
                }
            }
            return null
        }
    }
}

/// Create a thumbnail by compressing and resizing
suspend fun PlatformFile.thumbnailBytes(quality: Int = 80, size: Int = 256): ByteArray {
    val imageBytes = readBytes()
    return FileKit.compressImage(
        bytes = imageBytes,
        quality = quality,
        maxWidth = size,
        maxHeight = size,
        imageFormat = ImageFormat.JPEG
    )
}
