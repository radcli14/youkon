package com.dcsim.youkon

/// Holds a variable with units assigned to it
class Measurement(
    var value: Double,
    var unit: Unit,
    var name: String = "New Variable",
    var description: String = ""
) {

    enum class Unit {
        KILOGRAMS, POUNDS, SLUGS,
        METERS, FEET,
        NEWTONS, POUND_FORCE,
        WATTS, HORSEPOWER,
        JOULES, BTU,
        PASCALS, PSI, ATM, BARS
    }

    private val shortUnit: String
        get() = when(unit) {
            Unit.KILOGRAMS -> "kg"
            Unit.POUNDS -> "lbm"
            Unit.SLUGS -> "slug"
            Unit.METERS -> "m"
            Unit.FEET -> "ft"
            Unit.NEWTONS -> "N"
            Unit.POUND_FORCE -> "lbf"
            Unit.WATTS -> "W"
            Unit.HORSEPOWER -> "HP"
            Unit.JOULES -> "J"
            Unit.BTU -> "BTU"
            Unit.PASCALS -> "Pa"
            Unit.PSI -> "psi"
            Unit.ATM -> "atm"
            Unit.BARS -> "bar"
        }

    val allUnits = arrayOf(
        Unit.KILOGRAMS, Unit.POUNDS,
        Unit.METERS, Unit.FEET,
        Unit.NEWTONS, Unit.POUND_FORCE
    )
    private val massUnits = arrayOf(Unit.KILOGRAMS, Unit.POUNDS)
    private val lengthUnits = arrayOf(Unit.METERS, Unit.FEET)
    private val forceUnits = arrayOf(Unit.NEWTONS, Unit.POUND_FORCE)
    private val powerUnits = arrayOf(Unit.WATTS, Unit.HORSEPOWER)
    private val energyUnits = arrayOf(Unit.JOULES, Unit.BTU)
    private val pressureUnits = arrayOf(Unit.PASCALS, Unit.PSI, Unit.BARS)

    /// Provide an array of units that the measurement may be converted to
    fun equivalentUnits(): Array<Unit> {
        return when (unit) {
            in massUnits -> massUnits
            in lengthUnits -> lengthUnits
            in forceUnits -> forceUnits
            in powerUnits -> powerUnits
            in energyUnits -> energyUnits
            in pressureUnits -> pressureUnits
            else -> arrayOf()
        }
    }

    /// Convert from the current measurement unit into a different unit, using the Unit type as input
    fun convertTo(targetUnit: Unit): Measurement {
        val convertedValue: Double = when (unit) {
            Unit.KILOGRAMS -> when (targetUnit) {
                Unit.POUNDS -> value * 2.20462
                Unit.SLUGS -> value * 14.5939
                else -> value
            }
            Unit.POUNDS -> when (targetUnit) {
                Unit.KILOGRAMS -> value / 2.20462
                Unit.SLUGS -> value * 32.174
                else -> value
            }
            Unit.SLUGS -> {
                when (unit) {
                    Unit.KILOGRAMS -> value * 0.0685218
                    Unit.POUNDS -> value * 0.0310809
                    else -> value
                }
            }
            Unit.METERS -> when (targetUnit) {
                Unit.FEET -> value * 3.28084
                else -> value
            }
            Unit.FEET -> when (targetUnit) {
                Unit.METERS -> value / 3.28084
                else -> value
            }
            Unit.NEWTONS -> when (targetUnit) {
                Unit.POUND_FORCE -> value * 0.224809
                else -> value
            }
            Unit.POUND_FORCE -> when (targetUnit) {
                Unit.NEWTONS -> value / 0.224809
                else -> value
            }
            Unit.WATTS -> {
                when (unit) {
                    Unit.HORSEPOWER -> value * 0.00134102
                    else -> value
                }
            }
            Unit.HORSEPOWER -> {
                when (unit) {
                    Unit.WATTS -> value * 745.7
                    else -> value
                }
            }
            Unit.JOULES -> {
                when (unit) {
                    Unit.BTU -> value * 0.00094781712
                    else -> value
                }
            }
            Unit.BTU -> {
                when (unit) {
                    Unit.JOULES -> value * 1055.06
                    else -> value
                }
            }
            Unit.PASCALS -> {
                when (unit) {
                    Unit.PSI -> value * 0.00014503773773375
                    Unit.ATM -> value * 9.8692e-6
                    Unit.BARS -> value * 1e-5
                    else -> value
                }
            }
            Unit.PSI -> {
                when (unit) {
                    Unit.PASCALS -> value * 6894.76
                    Unit.ATM -> value * 0.068046
                    Unit.BARS -> value * 0.0689476
                    else -> value
                }
            }
            Unit.ATM -> {
                when (unit) {
                    Unit.PASCALS -> value * 101325
                    Unit.PSI -> value * 14.696
                    Unit.BARS -> value * 1.01325
                    else -> value
                }
            }
            Unit.BARS -> {
                when (unit) {
                    Unit.PASCALS -> value * 100000
                    Unit.PSI -> value * 14.5038
                    Unit.ATM -> value * 0.986923
                    else -> value
                }
            }
        }
        return Measurement(convertedValue, targetUnit)
    }

    /// Converts the measurement to a consistent system of measurements, like SI (kg-m-N), Imperial (slug-ft-pound), etc
    private fun convertToSystem(targetSystem: String): Measurement {
        return when (targetSystem) {
            "SI" -> convertToSI()
            else -> convertToSI()
        }
    }

    /// Converts this measurement to SI units (kg-m-N)
    private fun convertToSI(): Measurement {
        return when (unit) {
            Unit.POUNDS -> convertTo(Unit.KILOGRAMS)
            Unit.FEET -> convertTo(Unit.METERS)
            Unit.POUND_FORCE -> convertTo(Unit.NEWTONS)
            else -> return this
        }
    }

    /// Convert a string name of the unit to a Unit type
    private fun conversionUnit(targetUnit: String): Unit {
        val conversionUnit: Unit = when (targetUnit.lowercase()) {
            "kilograms" -> Unit.KILOGRAMS
            "pounds" -> Unit.POUNDS
            "meters" -> Unit.METERS
            "feet" -> Unit.FEET
            "newtons" -> Unit.NEWTONS
            "pound_force" -> Unit.POUND_FORCE
            else -> unit
        }
        return conversionUnit
    }

    override fun toString(): String {
        return "${niceNumber(value)} $shortUnit"
    }

    fun nameAndValueInSystem(system: String): String {
        return name + ": " + convertToSystem(system).toString()
    }
}


// Examples used for testing

var wembyHeight = Measurement(
    value = 2.26,
    unit = Measurement.Unit.METERS,
    name = "Height",
    description = "How tall is Wemby"
)

var wembyWeight = Measurement(
    value = 95.0,
    unit = Measurement.Unit.KILOGRAMS,
    name = "Weight",
    description = "How much does Wemby weigh"
)

var shuttleWeight = Measurement(
    value = 4480000.0,
    unit = Measurement.Unit.POUNDS,
    name = "Weight",
    description = "Weight of the Space Shuttle"
)