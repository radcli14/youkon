package com.dcsim.youkon

import kotlin.math.abs
import kotlin.math.round

/// Round the double to a certain number of points past the decimel
fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

/* Generate a nicely formatted number from a `Double` input number, for example:
 * - Numbers between 1 and 1000 will have <= 4 total digits, i.e. 1.0, 12.34, 123.4
 * - Numbers greater than 1000 will be shown in engineering notation to a order of
 *   magnitude multiplier in intervals of 1000, i.e. 1.234e3, 123.4e6
 * - Numbers between 0.01 and 1.0 will have <= 4 total nonzero digits, i.e. 0.1234, 0.01234
 * - Numbers less than 1000 will be shown in engineering notation to a order of
 *   magnitude multiplier in intervals of 1000, i.e. 1.234e-3, 123.4e-6
 */
fun niceNumber(number: Double): String {
    val sign = if (number < 0) "-" else ""
    val mag = abs(number)
    var exp = 0

    // Return the negative sign up front, then the nicely formatted number
    return sign + when {
        mag == 0.0 -> "0.0"
        mag >= 1000.0 -> {
            // Determine how many exponents should be added, in sets of 3 or "thousands"
            // For example, if we have a number 1,234, this will determine that the
            // exponent value of 3 should be used, or 1.234e3.
            var lowered = mag
            while (lowered >= 1000.0) {
                lowered /= 1000.0
                exp += 3
            }
            // First 5 digits of the value after dividing by
            // 10^exp, plus the "e"  exponent in powers of 10
            lowered.round(3).toString().take(5) + "e$exp"
        }
        mag in 1.0..1000.0 -> mag.round(3).toString().take(5)
        mag in 0.1..1.0 -> mag.round(4).toString().take(6)
        mag in 0.01..0.1 -> mag.round(5).toString().take(7)
        mag <= 0.01 -> {
            // Similar to the <1000.0 case, but working in opposite direction
            var raised = mag
            while (raised < 1.0) {
                raised *= 1000.0
                exp -= 3
            }
            raised.round(3).toString().take(5) + "e$exp"
        }
        else -> mag.toString().take(5)
    }
}

fun niceNumber(number: Float) : String {
    return niceNumber(number.toDouble())
}