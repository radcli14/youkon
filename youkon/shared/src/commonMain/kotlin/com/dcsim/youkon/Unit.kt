package com.dcsim.youkon

enum class YkUnit {
    KILOGRAMS, POUNDS, SLUGS,
    METERS, FEET, INCHES,
    NEWTONS, POUND_FORCE,
    WATTS, HORSEPOWER,
    JOULES, BTU,
    PASCALS, PSI, ATM, BARS;

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
        return when (this to targetUnit) {
            KILOGRAMS to POUNDS -> 2.20462
            KILOGRAMS to SLUGS -> 0.0685218
            POUNDS to KILOGRAMS -> 1.0 / 2.20462
            POUNDS to SLUGS -> 0.0310809
            SLUGS to KILOGRAMS -> 14.5939
            SLUGS to POUNDS -> 32.174
            METERS to FEET -> 3.28084
            METERS to INCHES -> 39.3701
            FEET to METERS -> 0.3048
            FEET to INCHES -> 12.0
            INCHES to METERS -> 0.0254
            INCHES to FEET -> 0.0833333
            NEWTONS to POUND_FORCE -> 0.224808943
            POUND_FORCE to NEWTONS -> 4.4482216153
            WATTS to HORSEPOWER -> 0.00134102
            HORSEPOWER to WATTS -> 745.7
            JOULES to BTU -> 0.00094781712
            BTU to JOULES -> 1055.06
            PASCALS to PSI -> 0.00014503773773375
            PASCALS to ATM -> 9.8692e-6
            PASCALS to BARS -> 1e-5
            PSI to PASCALS -> 6894.76
            PSI to ATM -> 0.068046
            PSI to BARS -> 0.0689476
            ATM to PASCALS -> 101325.0
            ATM to PSI -> 14.696
            ATM to BARS -> 1.01325
            BARS to PASCALS -> 100000.0
            BARS to PSI -> 14.5038
            BARS to ATM -> 0.986923
            else -> 1.0
        }
    }
}