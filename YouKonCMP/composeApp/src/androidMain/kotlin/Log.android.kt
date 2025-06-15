import android.util.Log

actual class Log {
    actual companion object {
        actual fun d(tag: String, message: String) {
            Log.d(tag, message)
        }
        actual fun e(tag: String, message: String) {
            Log.e(tag, message)
        }
    }
}
