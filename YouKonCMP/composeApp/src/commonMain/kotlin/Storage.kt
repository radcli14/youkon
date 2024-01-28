import model.YkUser

expect class Storage {
    companion object {
        /// The default user for someone opening the app for the first time is stored in `resources/defaultuser.json`
        val defaultUser: YkUser

        /// The `YkUser` that is saved from a previous session
        val savedUser: YkUser

        /// Save the current `YkUser` to a `.json` file
        fun saveUserToJson(user: YkUser)
    }
}