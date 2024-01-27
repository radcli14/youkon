//
//  QuickConvertCard.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 6/20/23.
//  Copyright © 2023 DC Simulation Studio. All rights reserved.
//

import SwiftUI
import ComposeApp

/// The quick convert card is shown at the top of the screen, and lets the user convert a single measurement value from any unit to any equivalent unit.
struct QuickConvertCard: View {
    @ObservedObject var vc = QuickConvertCardController()
    
    var body: some View {
        GroupBox(
            label: Text("Quick Convert")
        ) {
            contentGrid
        }
        .frame(maxWidth: 400)
        .padding(.horizontal)
        .backgroundStyle(Color(.systemBackground).opacity(0.4))
    }

    /// A 2x2 grid with the from and to dropdowns on top, text field and converted text on bottom
    @ViewBuilder
    private var contentGrid: some View {
        Grid {
            GridRow {
                fromDropdown
                toDropdown
            }
            GridRow {
                textField
                convertedText
            }
        }
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
            unit: $vc.targetUnit,
            availableUnits: $vc.equivalentUnits,
            headerText: "To"
        ) { _ in
            vc.setConvertedText()
        }
    }
    
    /// The field that takes the user input on the numeric value of the measurement
    @ViewBuilder
    private var textField: some View {
        MeasurementTextField(
            measurement: $vc.measurement,
            updateMeasurement: vc.setConvertedText
        )
        .multilineTextAlignment(.trailing)
    }
    
    /// The display of the measurement after conversion
    @ViewBuilder
    private var convertedText: some View {
        HStack {
            Text(vc.convertedText)
                //.padding(.leading, 8)
                .font(.headline)
            Spacer()
        }
    }
}


struct QuickConvertCard_Previews: PreviewProvider {
    static var previews: some View {
        QuickConvertCard()
    }
}
