package com.dcsim.youkon

enum class YKType(val units: Array<YkUnit>) {
    MASS(arrayOf(YkUnit.KILOGRAMS, YkUnit.POUNDS, YkUnit.SLUGS)),
    LENGTH(arrayOf(YkUnit.METERS, YkUnit.FEET, YkUnit.INCHES)),
    FORCE(arrayOf(YkUnit.NEWTONS, YkUnit.POUND_FORCE)),
    POWER(arrayOf(YkUnit.WATTS, YkUnit.HORSEPOWER)),
    ENERGY(arrayOf(YkUnit.JOULES, YkUnit.BTU)),
    PRESSURE(arrayOf(YkUnit.PASCALS, YkUnit.ATM, YkUnit.BARS))
}
