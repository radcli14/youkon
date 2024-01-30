import kotlinx.cinterop.ExperimentalForeignApi
import model.YkStrings
import platform.Foundation.*
import model.YkUser

actual class Storage {
    actual companion object {
        private const val tag = "Storage"

        /// The path string where the user data file will be stored
        private val documentsPath: String
            get() {
                // find all possible documents directories for this user
                val paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true)

                // just send back the first one, which ought to be the only one
                return paths[0]?.toString() ?: ""
            }

        /// The working file, which will be imported on startup, and saved any time the user modifies the data
        private val workingFile: String get() = "$documentsPath/userdata.json"

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
                Log.d(tag, "workingFile: $workingFile")
                return defaultUser
            }

        @OptIn(ExperimentalForeignApi::class)
        @Suppress("CAST_NEVER_SUCCEEDS")
        actual fun saveUserToJson(user: YkUser) {
            val jsonString = user.asJsonString() as NSString
            try {
                jsonString.writeToFile(workingFile, true, NSUTF8StringEncoding, null)
            } catch(exception: Exception) {
                Log.d(tag, "saveUserToJson failed with exception $exception")
            }
        }
    }
}