//
//  ProjectViewController.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 9/4/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

/// Controller for the `ProjectView` with methods that are invoked when editing fields or tapping buttons
class ProjectViewController: ObservableObject {
    var project: YkProject
    
    @Published var editedName: String
    @Published var editedDescription: String
    @Published var convertToSystem: YkSystem = .si
    @Published var measurements: [YkMeasurement]
    @Published var expansion: ProjectExpansionLevel = .compact
    @Published var isExpanded = false
    @Published var canSubtract = false

    init() {
        self.project = YkProject()
        editedName = project.name
        editedDescription = project.about
        measurements = project.measurements as! [YkMeasurement]
    }
    
    init(for project: YkProject) {
        self.project = project
        editedName = project.name
        editedDescription = project.about
        measurements = project.measurements as! [YkMeasurement]
    }
    
    func refresh(with viewController: ProjectViewController) {
        self.project = viewController.project
        editedName = project.name
        editedDescription = project.about
        measurements = project.measurements as! [YkMeasurement]
    }
    
    var expansionIcon: String {
        switch (expansion) {
        case .compact: return "chevron.down"
        default: return "chevron.up"
        }
    }
    
    func toggleExpansion() {
        switch (expansion) {
        case .compact: expansion = .static_
        case .static_: expansion = .compact
        case .editable: expansion = .static_
        default: expansion = .compact
        }
    }
    
    func toggleEdit() {
        switch (expansion) {
        case .editable: expansion = .static_
        default: expansion = .editable
        }
    }
    
    func toggleSystem() {
        switch (convertToSystem) {
        case .si: convertToSystem = .imperial
        case .imperial: convertToSystem = .si
        default: convertToSystem = .si
        }
    }
    
    func addMeasurement() {
        project.addMeasurement(
            value: 0.0,
            unit: .meters,
            name: "",
            about: ""
        )
        measurements = project.measurements as! [YkMeasurement]
    }
    
    func subtractMeasurement() {
        canSubtract.toggle()
    }
    
    func subtract(measurement: YkMeasurement) {
        project.removeMeasurement(measurement: measurement)
        measurements = project.measurements as! [YkMeasurement]
        if measurements.count == 0 {
            canSubtract = false
        }
    }
}
