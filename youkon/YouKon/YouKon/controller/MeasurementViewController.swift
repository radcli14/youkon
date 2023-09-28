//
//  MeasurementViewController.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 9/6/23.
//  Copyright © 2023 DC Simulation Studio. All rights reserved.
//

import Foundation
import shared

class MeasurementViewController: ObservableObject {
    @Published var measurement: YkMeasurement
    @Published var availableUnits: [YkUnit]

    init(for measurement: YkMeasurement) {
        self.measurement = measurement
        availableUnits = measurement.unit.allUnits.asSwiftArray
    }
}
