package com.dcsim.youkon

/**
 * The `YkType` enum class defines groupings of `YkUnit` types that represent that same form
 * of measurement. Specifically, there are mass, length, force, power, energy, and pressure groups.
 *
 * @param units an array of `YkUnit` that all represent the same type of measurement
 */
enum class YkType(val units: Array<YkUnit>) {
    MASS(arrayOf(YkUnit.KILOGRAMS, YkUnit.POUNDS, YkUnit.SLUGS)),
    LENGTH(arrayOf(YkUnit.METERS, YkUnit.FEET, YkUnit.INCHES)),
    FORCE(arrayOf(YkUnit.NEWTONS, YkUnit.POUND_FORCE)),
    DENSITY(arrayOf(YkUnit.KILOGRAMS_PER_METER_CUBED, YkUnit.SLUGS_PER_FOOT_CUBED)),
    POWER(arrayOf(YkUnit.WATTS, YkUnit.FOOT_POUND_PER_SECOND, YkUnit.HORSEPOWER)),
    ENERGY(arrayOf(YkUnit.JOULES, YkUnit.FOOT_POUND_ENERGY, YkUnit.BTU)),
    PRESSURE(arrayOf(YkUnit.PASCALS, YkUnit.PSF, YkUnit.PSI, YkUnit.ATM, YkUnit.BARS))
}
