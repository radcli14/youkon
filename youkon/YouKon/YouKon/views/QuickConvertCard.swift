//
//  QuickConvertCard.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 6/20/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct QuickConvertCard: View {
    var body: some View {
        GroupBox(
            label: Text("Quick Convert")
        ) {
            VStack {
                Text("...")
            }
            .frame(minHeight: 60)
        }
        .frame(maxWidth: 340)
    }
}

struct QuickConvertCard_Previews: PreviewProvider {
    static var previews: some View {
        QuickConvertCard()
    }
}
