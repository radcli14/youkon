//
//  KotlinArray+Extensions.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 7/20/23.
//  Copyright © 2023 DC Simulation Studio. All rights reserved.
//

import Foundation
import ComposeApp

extension KotlinArray<YkUnit> {
    var asSwiftArray: [YkUnit] {
        let n = Int(self.size)
        var out: [YkUnit] = []
        for i in 0 ..< n {
            if let unit = self.get(index: Int32(i)) {
                out.append(unit)
            }
        }
        return out
    }
}
