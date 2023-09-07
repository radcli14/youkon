//
//  MeasurementViewController.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 9/6/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class MeasurementViewController: ObservableObject {
    @Published var measurement: shared.Measurement
    @Published var availableUnits: [MeasurementUnit]

    init(for measurement: shared.Measurement) {
        self.measurement = measurement
        availableUnits = kotlinToSwiftArray(measurement.unit.allUnits)
    }
}
