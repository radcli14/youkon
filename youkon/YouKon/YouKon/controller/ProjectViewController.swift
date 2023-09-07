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
    var project: Project
    
    @Published var editedName: String
    @Published var editedDescription: String
    @Published var convertToSystem = "SI"
    @Published var measurements: [shared.Measurement]
    @Published var expansion: ProjectExpansionLevel = .compact
    @Published var isExpanded = false

    init() {
        self.project = Project()
        editedName = project.name
        editedDescription = project.about
        measurements = project.measurements as! [shared.Measurement]
    }
    
    init(for project: Project) {
        self.project = project
        editedName = project.name
        editedDescription = project.about
        measurements = project.measurements as! [shared.Measurement]
    }
    
    func refresh(with viewController: ProjectViewController) {
        self.project = viewController.project
        editedName = project.name
        editedDescription = project.about
        measurements = project.measurements as! [shared.Measurement]
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
    
    func addMeasurement() {
        project.addMeasurement(
            value: 0.0,
            unit: .meters,
            name: "New Measurement",
            about: ""
        )
        measurements = project.measurements as! [shared.Measurement]
    }
    
    func subtractMeasurement() {
        // TODO: create the subtractMeasurement method
    }
}
