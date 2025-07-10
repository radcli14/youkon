package firebase.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}
