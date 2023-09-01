//
//  ProjectView.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 8/30/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

struct ProjectView: View {
    var project: Project
    
    @State private var editedName: String
    @State private var editedDescription: String
    @State private var measurements: [shared.Measurement]
    @State private var isExpanded = false

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
            
            if isExpanded {
                ForEach(measurements, id: \.self) { measurement in
                    MeasurementView(measurement: measurement)
                }
            }
        }
        .padding()
        .background(Color.gray.opacity(0.1))
        .cornerRadius(8)
    }
    
    @ViewBuilder
    private var nameField: some View {
        TextField("Name", text: $editedName)
            .font(.title2)
            .fontWeight(.bold)
            .onChange(of: editedName) { name in
                project.name = name
            }
        Spacer()
    }
    
    @ViewBuilder
    private var descriptionField: some View {
        TextField("Description", text: $editedDescription)
            .font(.body)
            .onChange(of: editedDescription) { description in
                project.about = description
            }
    }
    
    @ViewBuilder
    private var expandButton: some View {
        Button(action: {
            isExpanded.toggle()
        }) {
            Image(systemName: isExpanded ? "chevron.up" : "chevron.down")
        }
        .buttonStyle(PlainButtonStyle())
    }
}


struct ProjectsView_Previews: PreviewProvider {
    static var previews: some View {
        ProjectView(project: testProject())
    }
    
    static func testProject() -> Project {
        let project = shared.Project()
        project.about = "Attributes of Victor"
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
