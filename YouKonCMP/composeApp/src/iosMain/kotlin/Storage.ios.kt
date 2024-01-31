import kotlinx.cinterop.ExperimentalForeignApi
import model.YkQuickData
import model.YkStrings
import model.YkUnit
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

        /// The quick data file, containing a value, unit, and target unit for the quick convert card
        private val quickDataFile: String get() = "$documentsPath/quickdata.json"

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
        @OptIn(ExperimentalForeignApi::class)
        actual val savedUser: YkUser
            get() {
                try {
                    NSString.stringWithContentsOfFile(workingFile, NSUTF8StringEncoding, null)?.let {
                        return YkUser.fromJsonString(it)
                    }
                    Log.d(tag, "Loaded savedUser from Json")
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
                Log.d(tag, "Failed to load savedUser from Json, falling back to defaultUser")
                return defaultUser
            }

        @OptIn(ExperimentalForeignApi::class)
        actual val savedQuickData: YkQuickData
            get() {
                try {
                    NSString.stringWithContentsOfFile(quickDataFile, NSUTF8StringEncoding, null)?.let {
                        return YkQuickData.fromJsonString(it)
                    }
                    Log.d(tag, "Loaded savedUser from Json")
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
                Log.d(tag, "Failed to load savedUser from Json, falling back to defaultUser")
                return YkQuickData(2.26, YkUnit.METERS, YkUnit.FEET)
            }

        actual fun saveUserToJson(user: YkUser) {
            saveToJson(user.asJsonString(), workingFile)
        }

        actual fun saveQuickDataToJson(data: YkQuickData) {
            saveToJson(data.asJsonString, quickDataFile)
        }

        @OptIn(ExperimentalForeignApi::class)
        @Suppress("CAST_NEVER_SUCCEEDS")
        private fun saveToJson(str: String, file: String) {
            try {
                (str as NSString).writeToFile(file, true, NSUTF8StringEncoding, null)
                Log.d(tag, "saveToJson succeeded in writing to $file")
            } catch(exception: Exception) {
                Log.d(tag, "saveToJson failed with exception $exception")
            }
        }
    }
}