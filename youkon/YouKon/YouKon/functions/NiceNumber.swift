//
//  NiceNumber.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 8/29/23.
//  Copyright © 2023 DC Simulation Studio. All rights reserved.
//

import Foundation

/// Returns a nicely formatted string for a double, assuring not too many decimal places are printed
func niceNumber(_ num: Double) -> String {
    if num == 0 {
        return "0"
    }
    
    // If the number is neither too small nor too large, will display as float,
    // otherwise will use exponential notation
    let useFloat = ((num >= 0.001 && num <= 1000.0) || num == 0.0)
    
    // Create the string formatter, and use to format the number
    let fmt = useFloat ? "%.3f" : "%.2e"
    var numStr = String(format: fmt, num)
    
    // If using a float format, truncate zeros after decimel
    while ["0", "."].contains(numStr.last) && useFloat {
        numStr.removeLast()
    }
    return numStr
}
