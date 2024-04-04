package firebase.service

//import dev.gitlive.firebase.crashlytics.crashlytics

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}

class LogServiceImpl : LogService {
    /*override fun logNonFatalCrash(throwable: Throwable) =
        Firebase.crashlytics.recordException(throwable)
     */
    override fun logNonFatalCrash(throwable: Throwable) {
        // TODO: Not yet implemented
    }
}
