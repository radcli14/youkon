//
//  Header.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 6/20/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct Header: View {
    var body: some View {
        HStack {
            Image("Icon_clearbackground")
                .resizable()
                .frame(width: 64, height: 64)
                .colorInvert()
                .colorMultiply(.primary)
                
            Text("YouKon")
                .font(.largeTitle)
                .fontWeight(.heavy)
            
            Spacer()
                .frame(width: 72)
        }
    }
}

struct Header_Previews: PreviewProvider {
    static var previews: some View {
        Header()
    }
}
