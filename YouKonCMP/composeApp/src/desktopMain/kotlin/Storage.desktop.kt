import model.YkUser

actual class Storage {
    actual companion object {
        actual val defaultUser: YkUser
            get() = TODO("Not yet implemented")
        actual val savedUser: YkUser
            get() = TODO("Not yet implemented")

        actual fun saveUserToJson(user: YkUser) {
        }
    }
}