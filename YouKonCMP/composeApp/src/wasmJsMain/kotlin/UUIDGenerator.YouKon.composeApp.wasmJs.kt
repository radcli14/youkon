import kotlin.random.Random

actual class UUIDGenerator actual constructor() {
    actual fun generateUUID(): String {
        // Generate a random UUID (version 4)
        fun randomHex(length: Int) = (1..length).map { Random.nextInt(16).toString(16) }.joinToString("")
        return "${randomHex(8)}-${randomHex(4)}-4${randomHex(3)}-${(8 + Random.nextInt(4)).toString(16)}${randomHex(3)}-${randomHex(12)}"
    }
}