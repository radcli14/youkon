//
//  ProjectsCard.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 6/20/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct ProjectsCard: View {
    var body: some View {
        GroupBox(
            label: Text("Projects")
        ) {
            VStack {
                Text("...")
            }
            .frame(maxHeight: .infinity)
        }
    }
}

struct ProjectsCard_Previews: PreviewProvider {
    static var previews: some View {
        ProjectsCard()
    }
}
