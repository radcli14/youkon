//
//  QuickConvertCard.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 6/20/23.
//  Copyright © 2023 DC Simulation Studio. All rights reserved.
//

import SwiftUI
import shared


struct QuickConvertCard: View {
    @State private var measurement = Measurement(
        value: 2.26,
        unit: .meters,
        name: "Quick Convert",
        description: "Card on top of the screen"
    )
    @State private var equivalentUnits = [MeasurementUnit.feet]
    @State private var targetUnit = MeasurementUnit.feet
    @State private var convertedText = "7.4147"

    var body: some View {
        GroupBox {
            VStack(alignment: .leading, spacing: 16) {
                Text("Quick Convert")
                    .font(.headline)
                HStack(spacing: 8) {
                    // The field that takes the user input on the numeric value of the measurement
                    MeasurementTextField(measurement: $measurement, updateMeasurement: {
                        convertedText = measurement.convertTo(targetUnit: targetUnit).description
                    })

                    // Selection for which type of unit to convert from
                    FromDropdown(measurement: $measurement) /* { unit in
                        if let unit = unit {
                            measurement.unit = unit
                            equivalentUnits = measurement.equivalentUnits()
                            if let newTargetUnit = equivalentUnits.first(where: { $0 != unit }) {
                                targetUnit = newTargetUnit
                                convertedText = measurement.convertTo(targetUnit).description
                            }
                        }
                    } */

                    ToDropdown(equivalentUnits: equivalentUnits) //, targetUnit: $targetUnit)
                }

                // The display of the measurement after conversion
                Text(convertedText)
            }
        }
        .frame(width: 360)
        .padding(16)
    }
}


struct QuickConvertCard_Previews: PreviewProvider {
    static var previews: some View {
        QuickConvertCard()
    }
}
