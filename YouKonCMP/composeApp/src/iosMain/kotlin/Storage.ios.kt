import model.YkUser

actual class Storage {
    actual companion object {
        actual val defaultUser: YkUser
            get() {
                // TODO: load from JSON file in resources directory
                /*
                if let path = Bundle.main.path(forResource: "defaultuser", ofType: "json"),
                    let contents = try? String(contentsOfFile: path),
                    let defaultUser = user.fromJsonString(jsonString: contents) {
                    return defaultUser
                } else {
                    print("Failed to load the default YkUser, falling back to an empty YkUser()")
                    return YkUser()
                }
                */
                val defaultUser = YkUser()
                defaultUser.setAsTestUser()
                return defaultUser
            }
        actual val savedUser: YkUser
            get() = TODO("Not yet implemented")

        actual fun saveUserToJson(user: YkUser) {
        }
    }
}