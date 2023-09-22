//
//  ContentViewController.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 9/5/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

/// The `ContentViewController` is instantiated inside the primary `ContentView`, and provides the `YkUser` data to lower level controllers. Also, if a call is received to toggle editing, this will trigger opening the bottom sheet.
class ContentViewController: ObservableObject {
    @Published var isEditingProject = false
    var project: YkProject? = nil
    var user = YkUser()
    
    init() {
        user = defaultUser
    }
    
    /// The default user for someone opening the app for the first time is stored in `resources/defaultuser.json`
    var defaultUser: YkUser {
        if let path = Bundle.main.path(forResource: "defaultuser", ofType: "json"),
            let contents = try? String(contentsOfFile: path),
            let defaultUser = user.fromJsonString(jsonString: contents) {
            return defaultUser
        } else {
            print("Failed to load the default YkUser, falling back to an empty YkUser()")
            return YkUser()
        }
    }
    
    /// The user tapped the measuments in a project's disclosure group, toggle editable measurements sheet
    func toggleEdit(to project: YkProject) {
        isEditingProject.toggle()
        self.project = isEditingProject ? project : nil
    }
}
