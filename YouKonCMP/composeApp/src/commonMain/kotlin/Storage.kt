import model.YkQuickData
import model.YkUser

expect class Storage {
    companion object {
        /// The default user for someone opening the app for the first time is stored in `resources/defaultuser.json`
        val defaultUser: YkUser

        /// The `YkUser` that is saved from a previous session
        val savedUser: YkUser

        /// The `YkQuickData` contains the value, unit, and target unit from a prior session
        val savedQuickData: YkQuickData

        /// Save the current `YkUser` to a `.json` file
        fun saveUserToJson(user: YkUser)

        /// Save the current `YkQuickData` to a `.json` file
        fun saveQuickDataToJson(data: YkQuickData)
    }
}