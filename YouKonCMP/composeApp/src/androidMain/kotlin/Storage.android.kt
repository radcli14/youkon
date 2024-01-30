import android.os.Environment
import model.YkStrings
import model.YkUser
import java.io.File

actual class Storage {
    actual companion object {
        private const val tag = "Android Storage"

        /// The URL where the user data file will be stored
        private val documentsUrl: File
            get() = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS
                ),
                "YouKon"
            )

        /// The working file, which will be imported on startup, and saved any time the user modifies the data
        private val workingFile: File
            get() = File("${documentsUrl.absolutePath}/userdata.json")

        actual val defaultUser: YkUser
            get() {
                // TODO: load from JSON file in resources directory
                return try {
                    YkUser.fromJsonString(YkStrings.defaultUserJsonString)
                } catch (exception: Exception) {
                    Log.d(tag, "Failed to load defaultUser from Json, using testUser")
                    YkUser.testUser
                }
            }

        actual val savedUser: YkUser
            get() {
                try {
                    val contents = workingFile.readText()
                    return YkUser.fromJsonString(contents)
                } catch (err: Exception) {
                    Log.d(tag, "Failed to load the saved YkUser, falling back to a default user")
                }
                return defaultUser
            }

        actual fun saveUserToJson(user: YkUser) {
            val jsonString = user.asJsonString()
            try {
                if (!workingFile.exists()) {
                    // If the file doesn't exist, try creating it along with the necessary directories
                    workingFile.parentFile?.mkdirs()
                    workingFile.createNewFile()
                }
                workingFile.writeText(jsonString)
                Log.d(tag, "Save succeeded to $workingFile")
            } catch(exception: Exception) {
                Log.d(tag,"Save failed because $exception")
                exception.printStackTrace()
            }
        }
    }
}