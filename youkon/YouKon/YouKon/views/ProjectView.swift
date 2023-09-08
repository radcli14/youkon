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

    init(project: YkProject, editing: Bool = false) {
        vc = ProjectViewController(for: project)
        if editing {
            vc.expansion = .editable
        }
    }
    
    var body: some View {
        if vc.expansion == .editable {
            mainStackWhenEditing
        } else {
            disclosureGroupWhenNotEditing
        }
    }
    
    @ViewBuilder
    private var mainStackWhenEditing: some View {
        VStack(alignment: .leading) {
            labelStack
            expansionStack
        }
    }
    
    @ViewBuilder
    private var disclosureGroupWhenNotEditing: some View {
        DisclosureGroup(isExpanded: $vc.isExpanded) {
            expansionStack
        } label: {
            labelStack
        }
        .padding()
        .background(Color.gray.opacity(0.1))
        .cornerRadius(8)
    }
    
    @ViewBuilder
    private var labelStack: some View {
        VStack(alignment: .leading) {
            nameField
            descriptionField
        }
        .foregroundStyle(.foreground)
    }
    
    @ViewBuilder
    private var expansionStack: some View {
        VStack {
            Divider()
            HStack {
                expansionView
                Spacer()
                expansionMenu
            }
        }
    }
    
    @ViewBuilder
    private var nameField: some View {
        switch (vc.expansion) {
        case .editable:
            TextField("Name", text: $vc.editedName)
                .font(.title3)
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
                .font(.body)
        default:
            Text(vc.editedDescription)
                .font(.caption)
        }
    }
    
    @ViewBuilder
    private var expansionView: some View {
        VStack(alignment: .leading) {
            ForEach(vc.measurements, id: \.self) { measurement in
                switch (vc.expansion) {
                case .editable: MeasurementView(measurement: measurement)
                default:
                    Text(measurement.nameAndValueInSystem(
                        system: vc.convertToSystem)
                    )
                }
            }
        }
    }
    
    @ViewBuilder
    private var expansionMenu: some View {
        if vc.expansion == .editable {
            expansionPlusMinusStack
        } else {
            expansionEditButton
        }
    }
    
    @ViewBuilder
    private var expansionPlusMinusStack: some View {
        VStack {
            Button(action: vc.addMeasurement) {
                Image(systemName: "plus")
                    .frame(height: 24)
            }
            Button(action: vc.subtractMeasurement) {
                Image(systemName: "minus")
                    .frame(height: 24)
            }
        }
        .buttonStyle(.bordered)
        .foregroundColor(.indigo)
    }
    
    @ViewBuilder
    private var expansionEditButton: some View {
        Button(action: {
            contentViewController.toggleEdit(to: vc.project)
        }) {
            Image(systemName: "pencil")
                .frame(height: 24)
        }
        .buttonStyle(.bordered)
        .foregroundColor(.indigo)
    }
}


struct ProjectsView_Previews: PreviewProvider {
    static var previews: some View {
        ProjectView(project: testProject())
            .environmentObject(contentViewController)
    }
    
    static func testProject() -> YkProject {
        let project = YkProject()
        project.name = "Victor Wembenyama"
        project.about = "Attributes of an amazing prospect"
        project.measurements.add(
            YkMeasurement(
                value: 2.26,
                unit: .meters,
                name: "Height",
                about: "How tall is Wemby"
            )
        )
        return project
    }
    
    static let contentViewController = ContentViewController()
}
