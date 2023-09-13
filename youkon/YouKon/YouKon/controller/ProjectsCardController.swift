//
//  ProjectCardController.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 9/12/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared


class ProjectsCardController: ObservableObject {
    var user: YkUser
    @Published var projects: [YkProject]
    
    init() {
        user = YkUser()
        user.projects.add(YkProject())
        let project = user.projects.firstObject as! YkProject
        project.name = "Victor Wembenyama"
        project.about = "Real Facts"
        let measurement = YkMeasurement(
            value: 2.26,
            unit: .meters,
            name: "Height",
            about: "How tall is Wemby"
        )
        project.measurements.add(measurement)
        projects = user.projects as! [YkProject]
    }
    
    func addProject() {
        print("add project")
    }
    
    func subtractProject() {
        print("subtract project")
    }
}
