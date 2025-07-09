package utilities

actual suspend fun compressImage(
    bytes: ByteArray,
    quality: Int,
    maxWidth: Int,
    maxHeight: Int,
    imageFormat: String
): ByteArray {
    // No-op for desktop, just return the original bytes
    return bytes
} 