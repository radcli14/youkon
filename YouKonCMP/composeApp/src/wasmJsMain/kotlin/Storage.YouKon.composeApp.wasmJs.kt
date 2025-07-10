import model.YkQuickData
import model.YkUser

actual class Storage actual constructor() {
    actual val defaultUser: YkUser
        get() = TODO("Not yet implemented")
    actual val savedUser: YkUser
        get() = TODO("Not yet implemented")
    actual val savedQuickData: YkQuickData
        get() = TODO("Not yet implemented")

    actual fun saveUserToJson(user: YkUser) {
    }

    actual fun saveQuickDataToJson(data: YkQuickData) {
    }

}