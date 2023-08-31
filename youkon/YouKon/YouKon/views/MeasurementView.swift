//
//  MeasurementView.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 8/30/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared


struct MeasurementView: View {
    @State var measurement: shared.Measurement
    
    @State private var unit: MeasurementUnit
    @State private var availableUnits: [MeasurementUnit]
    @State private var editedName: String
    @State private var editedDescription: String
    
    init(measurement: shared.Measurement) {
        self.measurement = measurement
        _unit = State(initialValue: measurement.unit)
        _availableUnits = State(initialValue: kotlinToSwiftArray(measurement.unit.equivalentUnits()))
        _editedName = State(initialValue: measurement.name)
        _editedDescription = State(initialValue: measurement.about)
    }
    
    var body: some View {
        VStack(spacing: 8) {
            TextField("Name", text: $editedName)
                .font(.headline)
                .onChange(of: editedName) { name in
                    measurement.name = name
                }
            
            TextField("Description", text: $editedDescription)
                .font(.subheadline)
                .onChange(of: editedDescription) { description in
                    measurement.about = description
                }
            
            HStack {
                MeasurementTextField(measurement: $measurement) {
                    // Update the views
                }
                UnitDropdown(unit: $unit, availableUnits: $availableUnits) { newUnit in
                    unit = newUnit
                    availableUnits = kotlinToSwiftArray(newUnit.equivalentUnits())
                }
                Spacer()
            }
        }
        .padding()
        .background(Color.white)
        .cornerRadius(8)
    }
}


struct MeasurementView_Previews: PreviewProvider {
    static var previews: some View {
        MeasurementView(
            measurement: shared.Measurement(
                value: 2.26,
                unit: .meters,
                name: "WembyHeight",
                about: "How tall is Wemby"
            )
        )
    }
}
