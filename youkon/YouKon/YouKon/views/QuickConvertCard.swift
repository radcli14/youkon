//
//  QuickConvertCard.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 6/20/23.
//  Copyright © 2023 DC Simulation Studio. All rights reserved.
//

import SwiftUI
import shared

/// The quick convert card is shown at the top of the screen, and lets the user convert a single measurement value from any unit to any equivalent unit.
struct QuickConvertCard: View {
    @State private var measurement: shared.Measurement
    @State private var allUnits = MeasurementUnit.meters.allAvailableUnits
    @State private var equivalentUnits = kotlinToSwiftArray(MeasurementUnit.meters.equivalentUnits())
    @State private var fromUnit = MeasurementUnit.meters
    @State private var targetUnit = MeasurementUnit.feet
    @State private var convertedText: String

    init() {
        let measurement = Measurement(
            value: 2.26,
            unit: .meters,
            name: "Quick Convert",
            about: "Card on top of the screen"
        )
        self.measurement = measurement
        convertedText = measurement.toSwiftString(in: MeasurementUnit.feet)
    }
    
    var body: some View {
        GroupBox {
            VStack(alignment: .leading, spacing: 16) {
                Text("Quick Convert")
                    .font(.headline)
                userInputRow

                // The display of the measurement after conversion
                Text(convertedText)
                    .padding(.leading, 8)
                    .font(.subheadline)
            }
        }
        .frame(width: 360)
        .padding(16)
    }
    
    /// The row with the text field on the left, and menu buttons for "From" and "To" units
    @ViewBuilder
    var userInputRow: some View {
        HStack(spacing: 8) {
            textField
            fromDropdown
            toDropdown
        }
    }
    
    /// The field that takes the user input on the numeric value of the measurement
    @ViewBuilder
    private var textField: some View {
        MeasurementTextField(
            measurement: $measurement,
            updateMeasurement: {
                setConvertedText()
            }
        )
    }
    
    /// Selection for which type of unit to convert from
    @ViewBuilder
    private var fromDropdown: some View {
        UnitDropdown(
            unit: $fromUnit,
            availableUnits: $allUnits,
            headerText: "From"
        ) { unit in
            measurement.unit = unit
            fromUnit = unit
            equivalentUnits = kotlinToSwiftArray(unit.equivalentUnits())
            if targetUnit == unit || !equivalentUnits.contains(targetUnit),
                let newTargetUnit {
                targetUnit = newTargetUnit
            }
            setConvertedText()
        }
    }
    
    /// Selection for which type of unit to convert to
    @ViewBuilder
    private var toDropdown: some View {
        UnitDropdown(
            unit: $targetUnit,
            availableUnits: $equivalentUnits,
            headerText: "To"
        ) { unit in
            targetUnit = unit
            setConvertedText()
        }
    }
    
    /// When a new value is received, update the text at the bottom of the card
    private func setConvertedText() {
        convertedText = measurement.toSwiftString(in: targetUnit)
    }
    
    private var newTargetUnit: MeasurementUnit? {
        return equivalentUnits.first(where: { $0 != targetUnit })
    }
}


struct QuickConvertCard_Previews: PreviewProvider {
    static var previews: some View {
        QuickConvertCard()
    }
}
