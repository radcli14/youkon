//
//  ProjectsCard.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 6/20/23.
//  Copyright © 2023 orgName. All rights reserved.
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


struct ProjectView: View {
    @State private var isExpanded = false
    
    var project: Project
    
    var body: some View {
        VStack(spacing: 16) {
            HStack {
                Text(project.name)
                    .font(.title)
                    .fontWeight(.bold)
                
                Spacer()
                
                Button(action: {
                    isExpanded.toggle()
                }) {
                    Image(systemName: isExpanded ? "chevron.up" : "chevron.down")
                }
                .buttonStyle(PlainButtonStyle())
            }
            
            if isExpanded {
                /*ForEach(project.measurements) { measurement in
                    MeasurementView(measurement: measurement)
                }*/
                Text("TODO: BE EXPANDED")
            } else {
                Text(project.description)
                    .font(.body)
            }
        }
        .padding()
        .background(Color.gray.opacity(0.1))
        .cornerRadius(8)
    }
}


struct ProjectsCard_Previews: PreviewProvider {
    static var previews: some View {
        ProjectsCard()
    }
}
