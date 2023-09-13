//
//  ProjectsCard.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 6/20/23.
//  Copyright © 2023 DC Simulation Studio. All rights reserved.
//

import SwiftUI
import shared

struct ProjectsCard: View {
    let userData: YkUser
    
    @State private var projects: [YkProject]
    
    init() {
        userData = YkUser()
        userData.projects.add(YkProject())
        let project = userData.projects.firstObject as! YkProject
        project.name = "Victor Wembenyama"
        project.about = "Real Facts"
        let measurement = YkMeasurement(
            value: 2.26,
            unit: .meters,
            name: "Height",
            about: "How tall is Wemby"
        )
        project.measurements.add(measurement)
        projects = userData.projects as! [YkProject]
    }
    
    var body: some View {
        GroupBox(
            label: labelStack
        ) {
            projectContent
        }
        .frame(maxHeight: .infinity)
        .padding()
    }
    
    @ViewBuilder
    private var labelStack: some View {
        HStack(spacing: 8) {
            Text("Projects")
            Spacer()
            plusButton
            minusButton
        }
    }
    
    @ViewBuilder
    private var plusButton: some View {
        Button(action: {}) {
            Image(systemName: "plus")
                .frame(height: 24)
        }
        .buttonStyle(.bordered)
    }
    
    @ViewBuilder
    private var minusButton: some View {
        Button(action: {}) {
            Image(systemName: "minus")
                .frame(height: 24)
        }
        .buttonStyle(.bordered)
    }
    
    @ViewBuilder
    private var projectContent: some View {
        ScrollView {
            LazyVStack(spacing: 16) {
                ForEach(projects, id: \.self) { project in
                    ProjectView(project: project)
                }
            }
        }
    }
}


struct ProjectsCard_Previews: PreviewProvider {
    static var previews: some View {
        ProjectsCard()
    }
}
