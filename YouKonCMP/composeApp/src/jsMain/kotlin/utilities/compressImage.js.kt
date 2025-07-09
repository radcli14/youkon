package utilities

actual suspend fun compressImage(
    bytes: ByteArray,
    quality: Int,
    maxWidth: Int,
    maxHeight: Int,
    imageFormat: String
): ByteArray {
    // TODO: Implement real JS image compression using Canvas/ImageBitmap if needed
    // For now, just return the original bytes
    return bytes
} 