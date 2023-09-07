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

    init(measurement: shared.Measurement) {
        vc = MeasurementViewController(for: measurement)
    }
    
    var body: some View {
        VStack {
            TextField("Name", text: $vc.measurement.name)
                .font(.headline)
            
            TextField("Description", text: $vc.measurement.about)
                .font(.subheadline)
            
            HStack {
                MeasurementTextField(measurement: $vc.measurement) {
                    // Update the views
                }
                UnitDropdown(unit: $vc.measurement.unit, availableUnits: $vc.availableUnits) { _ in }
                Spacer()
            }
        }
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
