package model

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}

class LogServiceImpl : LogService {
    override fun logNonFatalCrash(throwable: Throwable) =
        Firebase.crashlytics.recordException(throwable)
}
