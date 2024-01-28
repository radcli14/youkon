actual class Log {
    actual companion object {
        actual fun d(tag: String, message: String) {
            print("debug $tag: $message")
        }
    }
}