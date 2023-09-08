//
//  Measurement+Extensions.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 8/29/23.
//  Copyright © 2023 DC Simulation Studio. All rights reserved.
//

import Foundation
import shared

extension YkMeasurement {
    func nameAndValueInSystem(system: String) -> String {
        name + ": " + self.convertToSystem(targetSystem: system).toSwiftString()
    }
    
    func toSwiftString() -> String {
        return "\(niceNumber(self.value)) \(self.unit.shortUnit)"
    }
    
    func toSwiftString(in targetUnit: YkUnit) -> String {
        return self.toSwiftString() + " = " + self.convertTo(targetUnit: targetUnit).toSwiftString()
    }
}
