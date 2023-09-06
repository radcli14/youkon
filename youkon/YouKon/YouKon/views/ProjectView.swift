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
    @ObservedObject var vc: ProjectViewController
    @EnvironmentObject var contentViewController: ContentViewController

    init(project: Project, editing: Bool = false) {
        vc = ProjectViewController(for: project)
        if editing {
            vc.expansion = .editable
        }
    }
    
    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                nameField
                expandButton
            }
            descriptionField
            if vc.expansion != .compact {
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
        switch (vc.expansion) {
        case .editable:
            TextField("Name", text: $vc.editedName)
                .font(.headline)
                .fontWeight(.bold)
                .onChange(of: vc.editedName) { name in
                    vc.project.name = name
                }
        default:
            Text(vc.editedName)
                .font(.headline)
                .fontWeight(.bold)
        }
    }
    
    @ViewBuilder
    private var descriptionField: some View {
        switch (vc.expansion) {
        case .editable:
            TextField("Description", text: $vc.editedDescription)
                .onChange(of: vc.editedDescription) { description in
                    vc.project.about = description
                }
                .font(.caption)
        default:
            Text(vc.editedDescription)
                .font(.caption)
        }
    }
    
    @ViewBuilder
    private var expandButton: some View {
        Spacer()
        Image(systemName: vc.expansionIcon)
            .onTapGesture {
                vc.toggleExpansion()
            }
    }
    
    @ViewBuilder
    private var expansionView: some View {
        VStack(alignment: .leading) {
            ForEach(vc.measurements, id: \.self) { measurement in
                switch (vc.expansion) {
                case .static_: Text(measurement.nameAndValueInSystem(system: "SI"))
                case .editable: MeasurementView(measurement: measurement)
                default: EmptyView()
                }
            }
        }
    }
    
    @ViewBuilder
    private var expansionMenu: some View {
        if vc.expansion == .static_ || vc.expansion == .editable {
            VStack(spacing: 16) {
                Button(action: {
                    //vc.toggleEdit()
                    contentViewController.toggleEdit(to: vc.project)
                }) {
                    Image(systemName: "pencil")
                }
                if vc.expansion == .editable {
                    Button(action: vc.addMeasurement) {
                        Image(systemName: "plus")
                    }
                    Button(action: vc.subtractMeasurement) {
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
