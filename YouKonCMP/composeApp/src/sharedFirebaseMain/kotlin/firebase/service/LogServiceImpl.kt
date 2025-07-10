package firebase.service

class LogServiceImpl : LogService {
    override fun logNonFatalCrash(throwable: Throwable) {
        // TODO: Implement real logging, e.g., Firebase Crashlytics
        println("Non-fatal crash: ${throwable.message}")
    }
} 