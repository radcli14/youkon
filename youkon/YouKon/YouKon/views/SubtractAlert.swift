//
//  SubtractAlert.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 9/26/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

func SubtractAlert(
    title: String,
    confirmAction: @escaping () -> Void,
    cancelAction: @escaping () -> Void
) -> Alert {
    return Alert(
        title: Text("Delete \(title)"),
        message: Text("Are you sure?"),
        primaryButton: .destructive(Text("Delete")) {
            confirmAction()
        },
        secondaryButton: .cancel() {
            cancelAction()
        }
    )
}
