//
//  QuickConvertCardController.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 9/7/23.
//  Copyright © 2023 DC Simulation Studio. All rights reserved.
//

import Foundation
import ComposeApp

class QuickConvertCardController: ObservableObject {
    @Published var measurement: YkMeasurement
    @Published var allUnits = YkUnit.meters.allAvailableUnits
    @Published var equivalentUnits = YkUnit.meters.equivalentUnits().asSwiftArray
    @Published var targetUnit = YkUnit.feet
    @Published var convertedText: String = ""

    init() {
        measurement = YkMeasurement(
            value: 2.26,
            unit: .meters,
            name: "Quick Convert",
            about: "Card on top of the screen",
            id: "Quick-Convert-Measurement"
        )
        setConvertedText()
    }
    
    /// When a new value is received, update the text at the bottom of the card
    func setConvertedText() {
        convertedText = measurement.unitAndConversion(targetUnit: targetUnit)
    }
    
    /// When the user modifies the `From` dropdown, update the `measurement.unit`
    func updateUnit(to unit: YkUnit) {
        measurement.unit = unit
        equivalentUnits = unit.equivalentUnits().asSwiftArray
        targetUnit = unit.getNewTargetUnit(oldTarget: targetUnit, isExtended: true)
        setConvertedText()
    }
}
