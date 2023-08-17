package com.dcsim.youkon

actual fun formatToString(delimiters: String, number: Double): String {
    return number.toString()  // FormatSwiftString(delimiters, number)
}