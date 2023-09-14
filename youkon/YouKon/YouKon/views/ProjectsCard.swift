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
    @ObservedObject var vc: ProjectsCardController

    init() {
        vc = ProjectsCardController()
    }
    
    init(with user: YkUser) {
        vc = ProjectsCardController(with: user)
    }
    
    var body: some View {
        GroupBox(
            label: labelStack
        ) {
            projectContent
        }
        .backgroundStyle(Color(UIColor.systemBackground).opacity(0.5))
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
        Button(action: vc.addProject) {
            Image(systemName: "plus")
                .frame(height: 24)
        }
        .buttonStyle(.bordered)
    }
    
    @ViewBuilder
    private var minusButton: some View {
        Button(action: vc.subtractProject) {
            Image(systemName: "minus")
                .frame(height: 24)
        }
        .buttonStyle(.bordered)
    }
    
    @ViewBuilder
    private var projectContent: some View {
        ScrollView {
            LazyVStack(spacing: 16) {
                ForEach(vc.projects, id: \.self) { project in
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
