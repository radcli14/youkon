//
//  KotlinArray+Extensions.swift
//  YouKon
//
//  Created by Eliott Radcliffe on 7/20/23.
//  Copyright © 2023 DC Simulation Studio. All rights reserved.
//

import Foundation
import shared

// TODO: I would prefer this as an extension
//extension KotlinArray {
//    @objc func toSwiftArray() -> [Any] {
func kotlinToSwiftArray(
    _ kotlinArray: KotlinArray<YkUnit>
) -> [YkUnit] {
    let n = Int(kotlinArray.size)
    var out: [YkUnit] = []
    for i in 0 ..< n {
        if let unit = kotlinArray.get(index: Int32(i)) {
            out.append(unit)
        }
    }
    return out
}
