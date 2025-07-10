actual fun String.isValidEmail(): Boolean {
    // Simple email regex for WASM/JS
    return this.isNotBlank() && Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(this)
}

actual fun String.isValidPassword(): Boolean {
    val minPassLength = 6
    val passPattern = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$")
    return this.isNotBlank() && this.length >= minPassLength && passPattern.matches(this)
}