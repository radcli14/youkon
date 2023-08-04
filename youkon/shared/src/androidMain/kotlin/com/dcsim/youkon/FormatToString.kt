package com.dcsim.youkon

actual fun formatToString(delimiters: String, number: Double): String {
    return delimiters.format(number)
}