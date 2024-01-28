actual class Log {
    actual companion object {
        actual fun d(tag: String, message: String) {
            println("debug $tag: $message")
        }
    }
}