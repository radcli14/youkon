//
//  Header.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 6/20/23.
//  Copyright © 2023 DC Simulation Studio. All rights reserved.
//

import SwiftUI

struct Header: View {
    var body: some View {
        ZStack {
            Image("Icon_clearbackground")
                .resizable()
                .frame(width: 81, height: 81)
                .colorInvert()
                .colorMultiply(.primary)
                .offset(CGSize(width: -140, height: 0))

            Text("YouKon")
                .font(.custom("philosopher-bold", size: 54))
                //.shadow(color: .indigo, radius: 1, x: -1, y: 1)
                .foregroundStyle(.primary.shadow(.inner(color: .indigo, radius: 3)))
        }
    }
}

struct Header_Previews: PreviewProvider {
    static var previews: some View {
        Header()
    }
}
