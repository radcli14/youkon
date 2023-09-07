//
//  MeasurementUnit+Extensions.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 8/25/23.
//  Copyright © 2023 DC Simulation Studio. All rights reserved.
//

import Foundation
import shared

extension YkUnit {
    var toString: String {
        String(describing: self).replacingOccurrences(of: "_", with: " ")
    }
    
    var allAvailableUnits: [YkUnit] {
        kotlinToSwiftArray(self.allUnits)
    }
}
