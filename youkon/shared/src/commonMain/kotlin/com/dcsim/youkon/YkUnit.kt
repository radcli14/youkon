package com.dcsim.youkon

/**
 * The `YkUnit` enum class defines a unit of measurement, and conversion factors from that unit to
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
    METERS(1.0, "m"), FEET(0.3048, "ft"), INCHES(0.0254, "in"),
    NEWTONS(1.0, "N"), POUND_FORCE(4.44822, "lbf"),
    KILOGRAMS_PER_METER_CUBED(1.0, "kg/m^3"), SLUGS_PER_FOOT_CUBED(515.379, "slug/ft^3"),
    WATTS(1.0, "W"), FOOT_POUND_PER_SECOND(1.35582, "ft*lbf/s"), HORSEPOWER(745.7, "HP"),
    JOULES(1.0, "J"), FOOT_POUND_ENERGY(1.35582, "ft*lbf"), BTU(1055.06, "BTU"),
    PASCALS(1.0, "Pa"), PSF(47.8803, "psf"), PSI(6894.76, "psi"), ATM(101325.0, "atm"), BARS(100000.0, "bar");

    private val fromBase: Double get() = 1.0 / toBase

    val allUnits get() = YkUnit.values()

    /// Provide an array of units that the measurement may be converted to
    fun equivalentUnits(): Array<YkUnit> {
        return when (this) {
            in YkType.MASS.units -> YkType.MASS.units
            in YkType.LENGTH.units -> YkType.LENGTH.units
            in YkType.FORCE.units -> YkType.FORCE.units
            in YkType.DENSITY.units ->  YkType.DENSITY.units
            in YkType.POWER.units -> YkType.POWER.units
            in YkType.ENERGY.units -> YkType.ENERGY.units
            in YkType.PRESSURE.units -> YkType.PRESSURE.units
            else -> arrayOf()
        }
    }

    /// Conversion factor from the current measurement unit into a different unit
    fun conversionFactor(targetUnit: YkUnit): Double {
        return toBase * targetUnit.fromBase
    }

    /// Check that the target unit is not a duplicate of the selected unit,
    /// and is valid as the same type of measure
    fun getNewTargetUnit(oldTarget: YkUnit): YkUnit {
        if (oldTarget == this || oldTarget !in equivalentUnits()) {
            return newTargetUnit
        }
        return oldTarget
    }
    val newTargetUnit: YkUnit get() = equivalentUnits().first { it != this }
}