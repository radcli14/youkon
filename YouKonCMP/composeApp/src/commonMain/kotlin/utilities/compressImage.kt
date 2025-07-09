package utilities

/**
 * Multiplatform image compression function.
 *
 * @param bytes The original image bytes.
 * @param quality Compression quality (0-100).
 * @param maxWidth Maximum width of the output image.
 * @param maxHeight Maximum height of the output image.
 * @param imageFormat Image format (e.g., "JPEG").
 * @return Compressed image bytes.
 */
expect suspend fun compressImage(
    bytes: ByteArray,
    quality: Int = 80,
    maxWidth: Int = 256,
    maxHeight: Int = 256,
    imageFormat: String = "JPEG"
): ByteArray 