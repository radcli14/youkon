package utilities

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.ImageFormat

actual suspend fun compressImage(
    bytes: ByteArray,
    quality: Int,
    maxWidth: Int,
    maxHeight: Int,
    imageFormat: String
): ByteArray {
    val format = when (imageFormat.uppercase()) {
        "JPEG" -> ImageFormat.JPEG
        "PNG" -> ImageFormat.PNG
        else -> ImageFormat.JPEG
    }
    return FileKit.compressImage(
        bytes = bytes,
        quality = quality,
        maxWidth = maxWidth,
        maxHeight = maxHeight,
        imageFormat = format
    )
} 