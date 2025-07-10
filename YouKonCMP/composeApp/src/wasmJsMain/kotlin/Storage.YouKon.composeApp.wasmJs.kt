import model.YkQuickData
import model.YkStrings
import model.YkUnit
import model.YkUser

actual class Storage actual constructor() {
    actual val defaultUser: YkUser
        get() = try {
            YkUser.fromJsonString(YkStrings.defaultUserJsonString)
        } catch (exception: Exception) {
            YkUser.testUser
        }
    actual val savedUser: YkUser
        get() = defaultUser
    actual val savedQuickData: YkQuickData
        get() = YkQuickData(2.26, YkUnit.METERS, YkUnit.FEET)

    actual fun saveUserToJson(user: YkUser) {
        // no-op for WASM
    }

    actual fun saveQuickDataToJson(data: YkQuickData) {
        // no-op for WASM
    }
}