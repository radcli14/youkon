actual class Log {
    actual companion object {
        actual fun d(tag: String, message: String) {
            println("DEBUG: [${tag}] ${message}")
        }
        actual fun e(tag: String, message: String) {
            println("ERROR: [${tag}] ${message}")
        }
    }
} 