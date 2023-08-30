package com.dcsim.youkon

enum class MeasurementUnit {
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

    val allUnits get() = MeasurementUnit.values()

    val massUnits get() = arrayOf(KILOGRAMS, POUNDS, SLUGS)
    val lengthUnits get() = arrayOf(METERS, FEET, INCHES)
    val forceUnits get() = arrayOf(NEWTONS, POUND_FORCE)
    val powerUnits get() = arrayOf(WATTS, HORSEPOWER)
    val energyUnits get() = arrayOf(JOULES, BTU)
    val pressureUnits get() = arrayOf(PASCALS, PSI, ATM, BARS)

    /// Provide an array of units that the measurement may be converted to
    fun equivalentUnits(): Array<MeasurementUnit> {
        return when (this) {
            in massUnits -> massUnits
            in lengthUnits -> lengthUnits
            in forceUnits -> forceUnits
            in powerUnits -> powerUnits
            in energyUnits -> energyUnits
            in pressureUnits -> pressureUnits
            else -> arrayOf()
        }
    }

    /// Conversion factor from the current measurement unit into a different unit
    fun conversionFactor(targetUnit: MeasurementUnit): Double {
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