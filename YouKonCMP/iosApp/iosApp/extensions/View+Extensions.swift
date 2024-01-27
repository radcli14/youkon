//
//  View+Extensions.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 9/8/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

extension View {
    func hideKeyboard() {
        UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
    }
}
