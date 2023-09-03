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
    let userData: shared.UserData
    
    @State private var projects: [shared.Project]
    
    init() {
        userData = shared.UserData()
        userData.projects.add(Project())
        let project = userData.projects.firstObject as! shared.Project
        project.name = "Victor Wembenyama"
        project.about = "Real Facts"
        let measurement = shared.Measurement(
            value: 2.26,
            unit: .meters,
            name: "WembyHeight",
            about: "How tall is Wemby"
        )
        project.measurements.add(measurement)
        projects = userData.projects as! [Project]
    }
    
    var body: some View {
        GroupBox(
            label: Text("Projects")
        ) {
            ScrollView {
                LazyVStack(spacing: 16) {
                    ForEach(projects, id: \.self) { project in
                        ProjectView(project: project)
                    }
                }
                .padding()
            }
        }
        .frame(maxHeight: .infinity)
        .padding()
    }
}


struct ProjectsCard_Previews: PreviewProvider {
    static var previews: some View {
        ProjectsCard()
    }
}
