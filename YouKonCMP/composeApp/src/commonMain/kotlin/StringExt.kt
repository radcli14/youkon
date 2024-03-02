expect fun String.isValidEmail(): Boolean

expect fun String.isValidPassword(): Boolean

fun String.passwordMatches(repeated: String): Boolean {
  return this == repeated
}

fun String.idFromParameter(): String {
  return this.substring(1, this.length - 1)
}
