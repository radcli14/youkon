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
        user = savedUser
        saveUserToJson()
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
    
    /// The `YkUser` that is saved from a previous session
    var savedUser: YkUser {
        if let contents = try? String(contentsOf: workingFile),
            let savedUser = user.fromJsonString(jsonString: contents)  {
            return savedUser
        } else {
            print("Failed to load the saved YkUser, falling back to a default user")
            return defaultUser
        }
    }
    
    /// The URL where the user data file will be stored
    var documentsUrl: URL {
        // find all possible documents directories for this user
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        
        // just send back the first one, which ought to be the only one
        return paths[0]
    }
    
    /// The working file, which will be imported on startup, and saved any time the user modifies the data
    var workingFile: URL {
        return documentsUrl.appendingPathComponent("userdata.json")
    }

    /// Save the current `YkUser` to a `.json` file
    func saveUserToJson() {
        let str = user.asJsonString()
        do {
            try str.write(to: workingFile, atomically: true, encoding: .utf8)
        } catch {
            print(error.localizedDescription)
        }
    }
    
    /// The user tapped the measuments in a project's disclosure group, toggle editable measurements sheet
    func toggleEdit(to project: YkProject) {
        isEditingProject.toggle()
        self.project = isEditingProject ? project : nil
    }
}
