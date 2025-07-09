actual class Log {
    actual companion object {
        actual fun d(tag: String, message: String) {
            println("debug $tag: $message")
        }
        actual fun e(tag: String, message: String) {
            println("error $tag: $message")
        }
    }
}