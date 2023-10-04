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
        .alert(isPresented: $vc.showSubtractAlert) {
            SubtractAlert(
                title: vc.projectToDelete?.name ?? "",
                confirmAction: vc.confirmDelete,
                cancelAction: vc.cancelDelete
            )
        }
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
        Button(action: vc.onSubtractButtonTap) {
            Image(systemName: "minus")
                .frame(height: 24)
        }
        .buttonStyle(.bordered)
    }
    
    @ViewBuilder
    private var projectContent: some View {
        ScrollView {
            LazyVStack(spacing: 16) {
                ForEach(vc.projects, id: \.id) { project in
                    HStack {
                        subtractProjectButton(project)
                        ProjectView(project: project)
                    }
                    .animation(.easeInOut, value: vc.canSubtract)
                }
            }
        }
    }
    
    /// The red `X` that shows up to the left of a project when the user has enabled subtracting projects
    @ViewBuilder
    private func subtractProjectButton(_ project: YkProject) -> some View {
        if vc.canSubtract {
            Button(
                action: {
                    vc.subtract(project: project)
                }
            ) {
                Image(systemName: "x.circle.fill")
                    .foregroundColor(.pink)
                    .font(.title2)
            }
        }
    }
}


struct ProjectsCard_Previews: PreviewProvider {
    static var previews: some View {
        ProjectsCard()
    }
}
