private const val MIN_PASS_LENGTH = 6
private const val EMAIL_PATTERN = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
private const val PASS_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$"

actual fun String.isValidEmail(): Boolean {
    return this.isNotBlank() && Regex(EMAIL_PATTERN).matches(this)
}

actual fun String.isValidPassword(): Boolean {
    return this.isNotBlank() &&
            this.length >= MIN_PASS_LENGTH &&
            Regex(PASS_PATTERN).matches(this)
}