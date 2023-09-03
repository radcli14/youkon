//
//  ProjectView.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 8/30/23.
//  Copyright © 2023 DC Simulation Studio. All rights reserved.
//

import Foundation
import SwiftUI
import shared

struct ProjectView: View {
    var project: Project
    
    @State private var editedName: String
    @State private var editedDescription: String
    @State private var measurements: [shared.Measurement]
    @State private var expansion: ProjectExpansionLevel = .compact

    init(project: Project) {
        self.project = project
        editedName = project.name
        editedDescription = project.about
        measurements = project.measurements as! [shared.Measurement]
    }
    
    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                nameField
                expandButton
            }
            descriptionField
            expansionView
        }
        .padding()
        .background(Color.gray.opacity(0.1))
        .cornerRadius(8)
    }
    
    @ViewBuilder
    private var nameField: some View {
        switch (expansion) {
        case .editable:
            TextField("Name", text: $editedName)
                .font(.headline)
                .fontWeight(.bold)
                .onChange(of: editedName) { name in
                    project.name = name
                }
        default:
            Text(editedName)
                .font(.headline)
                .fontWeight(.bold)
        }
    }
    
    @ViewBuilder
    private var descriptionField: some View {
        switch (expansion) {
        case .editable:
            TextField("Description", text: $editedDescription)
                .onChange(of: editedDescription) { description in
                    project.about = description
                }
        default:
            Text(editedDescription)
        }
    }
    
    @ViewBuilder
    private var expandButton: some View {
        Spacer()
        Image(systemName: expansionIcon)
            .onTapGesture {
                toggleExpansion()
            }
    }
    
    @ViewBuilder
    private var expansionView: some View {
        ForEach(measurements, id: \.self) { measurement in
            switch (expansion) {
            case .static_: Text(measurement.nameAndValueInSystem(system: "SI"))
            case .editable: MeasurementView(measurement: measurement)
            default: EmptyView()
            }
            
        }
    }
    
    private var expansionIcon: String {
        switch (expansion) {
        case .compact: return "chevron.down"
        default: return "chevron.up"
        }
    }
    
    private func toggleExpansion() {
        switch (expansion) {
        case .compact: expansion = .static_
        case .static_: expansion = .compact
        case .editable: expansion = .static_
        default: expansion = .compact
        }
    }
}


struct ProjectsView_Previews: PreviewProvider {
    static var previews: some View {
        ProjectView(project: testProject())
    }
    
    static func testProject() -> Project {
        let project = shared.Project()
        project.name = "Victor Wembenyama"
        project.about = "Attributes of an amazing prospect"
        project.measurements.add(
            shared.Measurement(
                value: 2.26,
                unit: .meters,
                name: "WembyHeight",
                about: "How tall is Wemby"
            )
        )
        return project
    }
}
