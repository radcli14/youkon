package com.dcsim.youkon

/// Holds a variable with units assigned to it
class Measurement(
    var value: Double,
    var unit: Unit,
    var name: String = "New Variable",
    var description: String = ""
) {

    enum class Unit {
        KILOGRAMS, POUNDS,
        METERS, FEET,
        NEWTONS, POUND_FORCE
    }

    private val shortUnit: String
        get() = when(unit) {
            Unit.KILOGRAMS -> "kg"
            Unit.POUNDS -> "lbm"
            Unit.METERS -> "m"
            Unit.FEET -> "ft"
            Unit.NEWTONS -> "N"
            Unit.POUND_FORCE -> "lbf"
        }

    val allUnits = arrayOf(
        Unit.KILOGRAMS, Unit.POUNDS,
        Unit.METERS, Unit.FEET,
        Unit.NEWTONS, Unit.POUND_FORCE
    )
    val massUnits = arrayOf(Unit.KILOGRAMS, Unit.POUNDS)
    val lengthUnits = arrayOf(Unit.METERS, Unit.FEET)
    val forceUnits = arrayOf(Unit.NEWTONS, Unit.POUND_FORCE)

    /// Provide an array of units that the measurement may be converted to
    fun equivalentUnits(): Array<Unit> {
        return when (unit) {
            in massUnits -> massUnits
            in lengthUnits -> lengthUnits
            in forceUnits -> forceUnits
            else -> arrayOf()
        }
    }

    /// Convert from the current measurement unit into a different unit, using the Unit type as input
    fun convertTo(targetUnit: Unit): Measurement {
        val convertedValue: Double = when (unit) {
            Unit.KILOGRAMS -> when (targetUnit) {
                Unit.POUNDS -> value * 2.20462
                else -> value
            }
            Unit.POUNDS -> when (targetUnit) {
                Unit.KILOGRAMS -> value / 2.20462
                else -> value
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
        }
        return Measurement(convertedValue, targetUnit)
    }

    /// Convert from the current measurement unit into a different unit, using the String type as input
    fun convertTo(targetUnit: String): Measurement {
        return convertTo(conversionUnit(targetUnit))
    }

    /// Converts the measurement to a consistent system of measurements, like SI (kg-m-N), Imperial (slug-ft-pound), etc
    fun convertToSystem(targetSystem: String): Measurement {
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