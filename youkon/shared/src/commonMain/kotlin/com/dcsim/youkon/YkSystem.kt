package com.dcsim.youkon

/// Defines a consistent system of units, such as SI (kg-m-N), imperial (slug, ft, lbf), etc
enum class YKSystem(val units: Array<YkUnit>) {
    SI(arrayOf(YkUnit.KILOGRAMS, YkUnit.METERS, YkUnit.NEWTONS, YkUnit.WATTS, YkUnit.JOULES, YkUnit.PASCALS)),
    IMPERIAL(arrayOf(YkUnit.SLUGS, YkUnit.FEET, YkUnit.POUND_FORCE, YkUnit.HORSEPOWER, YkUnit.BTU, YkUnit.ATM))

    // TODO: IMPERIAL set is not consistent
}