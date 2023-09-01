//
//  Measurement+Extensions.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 8/29/23.
//  Copyright © 2023 DC Simulation Studio. All rights reserved.
//

import Foundation
import shared

extension shared.Measurement {
    func toSwiftString() -> String {
        return "\(niceNumber(self.value)) \(self.unit.shortUnit)"
    }
    
    func toSwiftString(in targetUnit: MeasurementUnit) -> String {
        return self.toSwiftString() + " = " + self.convertTo(targetUnit: targetUnit).toSwiftString()
    }
}
