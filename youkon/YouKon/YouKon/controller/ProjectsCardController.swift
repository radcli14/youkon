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
    @Published var projects: [YkProject] = []
    
    init() {
        user = YkUser()
        user.setAsTestUser()
        updateProjects()
    }
    
    func updateProjects() {
        projects = user.projects as! [YkProject]
    }
    
    func addProject() {
        print("add project")
        user.addProject()
        updateProjects()
        print(projects)
    }
    
    func subtractProject() {
        print("subtract project")
    }
}
