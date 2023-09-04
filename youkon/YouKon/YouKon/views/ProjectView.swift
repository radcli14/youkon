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
    @ObservedObject var viewController: ProjectViewController

    init(project: Project) {
        viewController = ProjectViewController(for: project)
    }
    
    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                nameField
                expandButton
            }
            descriptionField
            if viewController.expansion != .compact {
                Divider()
            }
            HStack {
                expansionView
                Spacer()
                expansionMenu
            }
        }
        .padding()
        .background(Color.gray.opacity(0.1))
        .cornerRadius(8)
    }
    
    @ViewBuilder
    private var nameField: some View {
        switch (viewController.expansion) {
        case .editable:
            TextField("Name", text: $viewController.editedName)
                .font(.headline)
                .fontWeight(.bold)
                .onChange(of: viewController.editedName) { name in
                    viewController.project.name = name
                }
        default:
            Text(viewController.editedName)
                .font(.headline)
                .fontWeight(.bold)
        }
    }
    
    @ViewBuilder
    private var descriptionField: some View {
        switch (viewController.expansion) {
        case .editable:
            TextField("Description", text: $viewController.editedDescription)
                .onChange(of: viewController.editedDescription) { description in
                    viewController.project.about = description
                }
                .font(.caption)
        default:
            Text(viewController.editedDescription)
                .font(.caption)
        }
    }
    
    @ViewBuilder
    private var expandButton: some View {
        Spacer()
        Image(systemName: viewController.expansionIcon)
            .onTapGesture {
                viewController.toggleExpansion()
            }
    }
    
    @ViewBuilder
    private var expansionView: some View {
        VStack(alignment: .leading) {
            ForEach(viewController.measurements, id: \.self) { measurement in
                switch (viewController.expansion) {
                case .static_: Text(measurement.nameAndValueInSystem(system: "SI"))
                case .editable: MeasurementView(measurement: measurement)
                default: EmptyView()
                }
            }
        }
    }
    
    @ViewBuilder
    private var expansionMenu: some View {
        if viewController.expansion == .static_ || viewController.expansion == .editable {
            VStack(spacing: 16) {
                Button(action: viewController.toggleEdit) {
                    Image(systemName: "pencil")
                }
                if viewController.expansion == .editable {
                    Button(action: viewController.addMeasurement) {
                        Image(systemName: "plus")
                    }
                    Button(action: viewController.subtractMeasurement) {
                        Image(systemName: "minus")
                    }
                }
            }
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
                name: "Height",
                about: "How tall is Wemby"
            )
        )
        return project
    }
}
