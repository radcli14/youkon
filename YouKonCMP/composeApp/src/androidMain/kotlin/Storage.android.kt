import android.content.Context
import android.os.Environment
import model.YkQuickData
import model.YkStrings
import model.YkUnit
import model.YkUser
import java.io.File

actual class Storage actual constructor() {

    /// Context is provided on Android to provide the `context.filesDir`, where we have read/write access
    private var context: Context? = null
    constructor(context: Context) : this() {
        this.context = context
    }

    private val tag = "Android Storage"

    /// The URL where the user data file will be stored
    private val documentsUrl: File
        get() = context?.filesDir ?: File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS
            ),
            "YouKon"
        )

    /// The working file, which will be imported on startup, and saved any time the user modifies the data
    private val workingFile: File
        get() = File(documentsUrl, "userdata.json")

    /// The quick data file, containing a value, unit, and target unit for the quick convert card
    private val quickDataFile: File
        get() = File(documentsUrl, "quickdata.json")

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

    actual val savedQuickData: YkQuickData
        get() {
            try {
                val contents = quickDataFile.readText()
                return YkQuickData.fromJsonString(contents)
            } catch (err: Exception) {
                Log.d(tag, "Failed to load the saved YkQuick, falling back to default")
            }
            return YkQuickData(2.26, YkUnit.METERS, YkUnit.FEET)
        }

    actual fun saveUserToJson(user: YkUser) {
        val jsonString = user.asJsonString()
        try {
            Log.d(tag, "attempting to save to $workingFile")
            if (!workingFile.exists()) {
                // If the file doesn't exist, try creating it along with the necessary directories
                Log.d(tag, "before mkdirs")
                workingFile.parentFile?.mkdirs()
                Log.d(tag, "after mkdirs")
                workingFile.createNewFile()
                Log.d(tag, "after createNewFile")
            }
            Log.d(tag, "after exist checks")
            workingFile.writeText(jsonString)
            Log.d(tag, "Save succeeded to $workingFile")
        } catch(exception: Exception) {
            Log.d(tag,"Save failed because $exception")
            exception.printStackTrace()
        }
    }

    actual fun saveQuickDataToJson(data: YkQuickData) {
        val jsonString = data.asJsonString
        try {
            if (!quickDataFile.exists()) {
                // If the file doesn't exist, try creating it along with the necessary directories
                quickDataFile.parentFile?.mkdirs()
                quickDataFile.createNewFile()
            }
            quickDataFile.writeText(jsonString)
            Log.d(tag, "Save succeeded to $quickDataFile")
        } catch(exception: Exception) {
            Log.d(tag,"Save failed because $exception")
            exception.printStackTrace()
        }
    }
}