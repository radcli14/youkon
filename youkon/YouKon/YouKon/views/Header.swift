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
                .frame(width: 81, height: 81)
                .colorInvert()
                .colorMultiply(.primary)
                .padding(-5)
                
            Text("YouKon")
                .font(.system(size: 54))
                .fontWeight(.bold)
            
            Spacer()
                .frame(width: 76)
        }
    }
}

struct Header_Previews: PreviewProvider {
    static var previews: some View {
        Header()
    }
}
