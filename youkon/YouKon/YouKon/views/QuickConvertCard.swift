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
    @ObservedObject var vc = QuickConvertCardController()
    
    var body: some View {
        GroupBox {
            VStack(alignment: .leading, spacing: 16) {
                Text("Quick Convert")
                    .font(.headline)
                userInputRow

                // The display of the measurement after conversion
                Text(vc.convertedText)
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
            measurement: $vc.measurement,
            updateMeasurement: {
                vc.setConvertedText()
            }
        )
    }
    
    /// Selection for which type of unit to convert from
    @ViewBuilder
    private var fromDropdown: some View {
        UnitDropdown(
            unit: $vc.measurement.unit,
            availableUnits: $vc.allUnits,
            headerText: "From"
        ) { unit in
            vc.updateUnit(to: unit)
        }
    }
    
    /// Selection for which type of unit to convert to
    @ViewBuilder
    private var toDropdown: some View {
        UnitDropdown(
            unit: $vc.measurement.targetUnit,
            availableUnits: $vc.equivalentUnits,
            headerText: "To"
        ) { unit in
            //vc.targetUnit = unit
            vc.setConvertedText()
        }
    }
    
}


struct QuickConvertCard_Previews: PreviewProvider {
    static var previews: some View {
        QuickConvertCard()
    }
}
