//
//  MeasurementView.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 8/30/23.
//  Copyright © 2023 DC Simulation Studio. All rights reserved.
//

import Foundation
import SwiftUI
import shared


struct MeasurementView: View {
    @ObservedObject var vc: MeasurementViewController
    @State private var opacity = 0.0

    init(measurement: YkMeasurement) {
        vc = MeasurementViewController(for: measurement)
    }
    
    var body: some View {
        VStack {
            nameField
            descriptionField
            valueAndUnitStack
        }
        .opacity(opacity)
        .onAppear {
            withAnimation {
                opacity = 1.0
            }
        }
    }
    
    /// Editable field for the name of the `YkMeasurement`
    @ViewBuilder
    private var nameField: some View {
        TextField("Name", text: $vc.measurement.name)
            .font(.headline)
    }
    
    /// Editable field for the `about` string of the `YkMeasurement`
    @ViewBuilder
    private var descriptionField: some View {
        TextField("Description", text: $vc.measurement.about)
            .font(.subheadline)
    }
    
    /// Numeric field on the left to modify `value`, and dropdown on the right to modify `unit` of the `YkMeasurement`
    @ViewBuilder
    private var valueAndUnitStack: some View {
        HStack {
            MeasurementTextField(measurement: $vc.measurement) { }
            UnitDropdown(unit: $vc.measurement.unit, availableUnits: $vc.availableUnits) { _ in }
            Spacer()
        }
    }
}


struct MeasurementView_Previews: PreviewProvider {
    static var previews: some View {
        MeasurementView(
            measurement: YkMeasurement(
                value: 2.26,
                unit: .meters,
                name: "WembyHeight",
                about: "How tall is Wemby",
                id: "Preview-Measurement"
            )
        )
    }
}
