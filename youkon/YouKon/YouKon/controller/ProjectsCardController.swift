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
    @Published var canSubtract = false

    /// Initialize with a generic user
    init() {
        user = YkUser()
        updateProjects()
    }
    
    /// Initialize with the user provided by a higher level view
    init(with user: YkUser) {
        self.user = user
        updateProjects()
    }
    
    /// Update the public list of `YkProject` items by assuring that the Kotlin version is Swift formatted
    private func updateProjects() {
        projects = user.projects as! [YkProject]
    }
    
    /// Add a new, blank, `YkProject` to the `YkUser`
    func addProject() {
        user.addProject()
        updateProjects()
        print(projects)
    }
    
    /// Make the button to remove any of the `YkProject`s visible
    func onSubtractButtonTap() {
        canSubtract.toggle()
    }
    
    /// Remove the specified `YkProject` from the `YkUser`
    func subtract(project: YkProject) {
        user.removeProject(project: project)
        updateProjects()
    }
}
