package com.dcsim.youkon
//import java.lang.Math.min

expect fun formatToString(delimiters: String, number: Double): String

fun niceNumber(number: Double) : String {
    return if (number <= 0.001 || number >= 1000) {
        formatToString("%.3E", number)
        //"%.3E".format(number)
    } else {
        // Initially format with extra digits after the decimal
        var longString = formatToString("%7.5f", number)
        //var longString = "%7.5f".format(number)

        // Find the last non-zero and non-decimal digit
        var numToDrop = 0
        while (numToDrop < 6) {
            when (longString[longString.length - numToDrop - 1]) {
                in arrayOf('0', '.') -> numToDrop += 1
                else -> break
            }
        }

        // Return the reduced string, dropping zeros or decimals at the end
        longString.dropLast(numToDrop)
    }
}

fun niceNumber(number: Float) : String {
    return niceNumber(number.toDouble())
}