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

    fun convertTo(targetUnit: String): Measurement {
        return convertTo(conversionUnit(targetUnit))
    }

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
        return "$value $unit"
    }
}

fun testMeasurement(): Measurement {
    return Measurement(
        value = 2.26,
        unit = Measurement.Unit.METERS,
        name = "WembyHeight",
        description = "How tall is Victor Wembanyama in meters"
    )
}