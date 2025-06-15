expect fun String.isValidEmail(): Boolean

expect fun String.isValidPassword(): Boolean

fun String.passwordMatches(repeated: String): Boolean {
  return this == repeated
}

fun String.idFromParameter(): String {
  return this.substring(1, this.length - 1)
}

fun String.toDoubleOrZeroOrNull(): Double? {
    // First check if this can be converted to a double
    toDoubleOrNull()?.let { return it }
    // Otherwise see if this is blank, or a negative sign, which we assume is equivalent to zero
    if (isBlank() || this == "-") { return 0.0 }
    // Fall through, return null
    return null
}

fun String.numericValueEquals(other: String): Boolean {
    return this.toDoubleOrZeroOrNull() == other.toDoubleOrZeroOrNull()
}

val String.canBeInt: Boolean get() {
    return toDoubleOrZeroOrNull()?.rem(1.0) == 0.0
}

fun String.countSignificantDigits(): Int {
    // Remove any negative sign
    val withoutSign = replace("-", "")
    // Split by decimal point
    val parts = withoutSign.split(".")
    if (parts.size == 1) {
        // No decimal point, count all digits
        return parts[0].length
    }
    // Has decimal point, count all digits
    return parts[0].length + parts[1].length
}
