//
//  QuickConvertCardController.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 9/7/23.
//  Copyright © 2023 DC Simulation Studio. All rights reserved.
//

import Foundation
import shared

class QuickConvertCardController: ObservableObject {
    @Published var measurement: YkMeasurement
    @Published var allUnits = YkUnit.meters.allAvailableUnits
    @Published var equivalentUnits = YkUnit.meters.equivalentUnits().asSwiftArray
    @Published var targetUnit = YkUnit.feet
    @Published var convertedText: String

    init() {
        let measurement = YkMeasurement(
            value: 2.26,
            unit: .meters,
            name: "Quick Convert",
            about: "Card on top of the screen"
        )
        self.measurement = measurement
        convertedText = measurement.valueAndConversion(targetUnit: YkUnit.feet)
    }
    
    /// When a new value is received, update the text at the bottom of the card
    func setConvertedText() {
        convertedText = measurement.valueAndConversion(targetUnit: targetUnit)
    }
    
    func updateUnit(to unit: YkUnit) {
        measurement.unit = unit
        equivalentUnits = unit.equivalentUnits().asSwiftArray
        if targetUnit == unit || !equivalentUnits.contains(targetUnit),
           let newUnit = newTargetUnit {
            targetUnit = newUnit
        }
        setConvertedText()
    }
    
    var newTargetUnit: YkUnit? {
        return equivalentUnits.first(where: { $0 != targetUnit })
    }
}
