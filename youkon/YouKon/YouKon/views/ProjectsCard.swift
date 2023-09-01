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
    var body: some View {
        GroupBox(
            label: Text("Projects")
        ) {
            ScrollView {
                LazyVStack(spacing: 16) {
                    /*ForEach(projects) { project in
                        ProjectView(project: project)
                    }*/
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
