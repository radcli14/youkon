import Foundation

@_cdecl("FormatSwiftString") // Add this attribute to expose the function to Kotlin
public func FormatSwiftString(_ delimiters: UnsafePointer<Int8>, _ number: Double) -> UnsafeMutablePointer<Int8>? {
    let formatString = String(cString: delimiters)
    let formattedString = String(format: formatString, number)
    return strdup(formattedString)
}