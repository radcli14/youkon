//
//  ProjectView.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 8/30/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

struct ProjectView: View {
    var project: Project
    
    @State private var isExpanded = false
    
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
                Text(project.about)
                    .font(.body)
            }
        }
        .padding()
        .background(Color.gray.opacity(0.1))
        .cornerRadius(8)
    }
}


struct ProjectsView_Previews: PreviewProvider {
    static var previews: some View {
        ProjectView(project: testProject())
    }
    
    static func testProject() -> Project {
        let project = shared.Project()
        project.about = "Attributes of Victor"
        project.measurements.add(
            shared.Measurement(
                value: 2.26,
                unit: .meters,
                name: "WembyHeight",
                about: "How tall is Wemby"
            )
        )
        return project
    }
}
