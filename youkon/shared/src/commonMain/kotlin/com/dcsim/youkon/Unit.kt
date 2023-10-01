package com.dcsim.youkon

/**
 * The `YkUnit` class defines a unit of measurement, and conversion factors from that unit to
 * another unit representing the same type of dimension. The `enum class` is initialized with
 * a `Double` value `toBase` which represents the conversion factor from this unit to a base
 * unit, with the latter generally in SI. The conversion factor is the product of this `toBase`
 * with a calculated value `fromBase` for a second unit, with the latter being the inverse of
 * the second unit's `toBase` value. The `enum class` is also initiated with a `shortUnit`
 * string, which is a abbreviation of the unit used for display purposes.
 *
 * @param toBase the conversion factor to go from this unit to the base unit, typically in SI
 * @param shortUnit the more compact string representation of this unit
 */
enum class YkUnit(private val toBase: Double, val shortUnit: String) {
    KILOGRAMS(1.0, "kg"), POUNDS(0.453592, "lbm"), SLUGS(14.5939, "slug"),
    METERS(1.0, "m"), FEET(0.3048, "ft"), INCHES(0.3048, "in"),
    NEWTONS(1.0, "N"), POUND_FORCE(4.44822, "lbf"),
    WATTS(1.0, "W"), HORSEPOWER(745.7, "HP"),
    JOULES(1.0, "J"), BTU(1055.06, "BTU"),
    PASCALS(1.0, "Pa"), PSI(6894.76, "psi"), ATM(101325.0, "atm"), BARS(100000.0, "bar");

    private val fromBase: Double get() = 1.0 / toBase

    val allUnits get() = YkUnit.values()

    /// Provide an array of units that the measurement may be converted to
    fun equivalentUnits(): Array<YkUnit> {
        return when (this) {
            in YKType.MASS.units -> YKType.MASS.units
            in YKType.LENGTH.units -> YKType.LENGTH.units
            in YKType.FORCE.units -> YKType.FORCE.units
            in YKType.POWER.units -> YKType.POWER.units
            in YKType.ENERGY.units -> YKType.ENERGY.units
            in YKType.PRESSURE.units -> YKType.PRESSURE.units
            else -> arrayOf()
        }
    }

    /// Conversion factor from the current measurement unit into a different unit
    fun conversionFactor(targetUnit: YkUnit): Double {
        return toBase * targetUnit.fromBase
    }
}