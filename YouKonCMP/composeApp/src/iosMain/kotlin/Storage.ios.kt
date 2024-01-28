import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.*
import model.YkUser

actual class Storage {
    actual companion object {
        actual val defaultUser: YkUser
            get() {
                // TODO: load from JSON file in resources directory
                //val path = Bundle.main.path(forResource: "defaultuser", ofType: "json")
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
            get() {
                /*
                // find all possible documents directories for this user
                val paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)

                // just send back the first one, which ought to be the only one
                return paths[0]

                 */
                return defaultUser
            }

        @OptIn(ExperimentalForeignApi::class)
        actual fun saveUserToJson(user: YkUser) {
            /*
            let str = user.asJsonString()
            do {
                try str.write(to: workingFile, atomically: true, encoding: .utf8)
            } catch {
                print(error.localizedDescription)
            }
             */
            val str = user.asJsonString()
            /*try {
                (str as NSString).writeToFile(path, true, NSUTF8StringEncoding, null)
            }*/
        }
    }
}