//
//  ContentViewController.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 9/5/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class ContentViewController: ObservableObject {
    @Published var isEditingProject = false
    var project: YkProject? = nil
    var user: YkUser
    
    init() {
        user = YkUser()
        user.setAsTestUser()
        print(user.asJsonString())
    }
    
    func toggleEdit(to project: YkProject) {
        isEditingProject.toggle()
        self.project = isEditingProject ? project : nil
    }
}
