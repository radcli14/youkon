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
    @State private var opacity = 0.0
    
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
                .opacity(opacity)
                .onAppear {
                    withAnimation {
                        opacity = 1.0
                    }
                }
        }
    }
    
    /// The list of editable measurements when the project is opened in a `.sheet` for editing
    @ViewBuilder
    private var mainStackWhenEditing: some View {
        VStack(alignment: .leading) {
            labelStack
            expansionStack
        }
    }
    
    /// The disclosure group with static content inside, with label with name and description
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
    
    /// The name and description shown at the top of the project
    @ViewBuilder
    private var labelStack: some View {
        VStack(alignment: .leading) {
            nameField
            descriptionField
        }
        .foregroundStyle(.foreground)
    }
    
    /// The content that is displayed when the `DisclosureGroup` is expanded
    @ViewBuilder
    private var expansionStack: some View {
        VStack {
            Divider()
            HStack(alignment: .top) {
                expansionView
                Spacer()
                expansionMenu
            }
        }
    }
    
    /// The title of the project, which is the `.name` field in the `YkProject`
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
    
    /// The subtitle of the project, which is the `.about` field in the `YkProject`
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
    
    /// When the `DisclosureGroup` is expanded, this will be inside, and will either contain the editable content when opened in a `.sheet`, or static text
    @ViewBuilder
    private var expansionView: some View {
        VStack(alignment: .leading) {
            ForEach(vc.measurements, id: \.self) { measurement in
                switch (vc.expansion) {
                case .editable:
                    HStack {
                        subtractMeasurementButton(measurement)
                        MeasurementView(measurement: measurement)
                    }
                    .animation(.easeInOut, value: vc.canSubtract)
                default:
                    Text(measurement.nameAndValueInSystem(
                        system: vc.convertToSystem)
                    )
                }
            }
            if vc.measurements.isEmpty {
                Text("Add New Measurements")
            }
        }
        .onTapGesture {
            if vc.expansion != .editable {
                contentViewController.toggleEdit(to: vc.project)
            }
        }
    }
    
    /// If editable, this will display the `expansionPlusMinusStack`
    @ViewBuilder
    private var expansionMenu: some View {
        if vc.expansion == .editable {
            expansionPlusMinusStack
        }
    }
    
    /// The plus and minus buttons on the right hand side when editing, to create or delete measurements
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
    
    /// The red `X` that shows up to the left of a measurement when the user has enabled subtracting measurements
    @ViewBuilder
    private func subtractMeasurementButton(_ measurement: YkMeasurement) -> some View {
        if vc.canSubtract {
            Button(
                action: {
                    vc.subtract(measurement: measurement)
                }
            ) {
                Image(systemName: "x.circle.fill")
                    .foregroundColor(.pink)
                    .font(.title2)
            }
        }
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
