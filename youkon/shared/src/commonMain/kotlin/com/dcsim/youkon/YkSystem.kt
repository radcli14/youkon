package com.dcsim.youkon

/// Defines a consistent system of units, such as SI (kg-m-N), imperial (slug, ft, lbf), etc.
/// Must be ordered (MASS, LENGTH, FORCE, POWER, ENERGY, PRESSURE).
enum class YKSystem(val units: Array<YkUnit>) {
    SI(arrayOf(YkUnit.KILOGRAMS, YkUnit.METERS, YkUnit.NEWTONS, YkUnit.WATTS, YkUnit.JOULES, YkUnit.PASCALS)),
    IMPERIAL(arrayOf(YkUnit.SLUGS, YkUnit.FEET, YkUnit.POUND_FORCE, YkUnit.HORSEPOWER, YkUnit.BTU, YkUnit.ATM))
    ;

    // TODO: IMPERIAL set is not consistent

    val mass get() = units[0]
    val length get() = units[1]
    val force get() = units[2]
    val power get() = units[3]
    val energy get() = units[4]
    val pressure get() = units[5]
}