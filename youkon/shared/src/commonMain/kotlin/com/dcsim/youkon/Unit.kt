package com.dcsim.youkon

enum class YkUnit(val toBase: Double) {
    KILOGRAMS(1.0), POUNDS(0.453592), SLUGS(14.5939),
    METERS(1.0), FEET(0.3048), INCHES(0.3048),
    NEWTONS(1.0), POUND_FORCE(4.44822),
    WATTS(1.0), HORSEPOWER(745.7),
    JOULES(1.0), BTU(1055.06),
    PASCALS(1.0), PSI(6894.76), ATM(101325.0), BARS(100000.0);

    val fromBase: Double get() = 1.0 / toBase

    val shortUnit: String
        get() = when(this) {
            KILOGRAMS -> "kg"
            POUNDS -> "lbm"
            SLUGS -> "slug"
            METERS -> "m"
            FEET -> "ft"
            INCHES -> "in"
            NEWTONS -> "N"
            POUND_FORCE -> "lbf"
            WATTS -> "W"
            HORSEPOWER -> "HP"
            JOULES -> "J"
            BTU -> "BTU"
            PASCALS -> "Pa"
            PSI -> "psi"
            ATM -> "atm"
            BARS -> "bar"
        }

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