package com.dcsim.youkon
import kotlinx.serialization.*
import kotlinx.serialization.json.*

/// Holds a variable with units assigned to it
@Serializable
data class YkMeasurement(
    var value: Double,
    var unit: YkUnit,
    var name: String = "New Variable",
    var about: String = ""
) {

    companion object {
        fun new(): YkMeasurement {
            return YkMeasurement(1.0, YkUnit.METERS, "New Measurement", "Description")
        }
    }

    fun asJsonString(): String {
        return Json.encodeToString(this)
    }

    /// Convert from the current measurement unit into a different unit, using the Unit type as input
    fun convertTo(targetUnit: YkUnit): YkMeasurement {
        return YkMeasurement(value * unit.conversionFactor(targetUnit), targetUnit)
    }

    /// Converts the measurement to a consistent system of measurements, like SI (kg-m-N), Imperial (slug-ft-pound), etc
    fun convertToSystem(targetSystem: YkSystem): YkMeasurement {
        return when (unit) {
            in YKType.MASS.units -> convertTo(targetSystem.mass)
            in YKType.LENGTH.units -> convertTo(targetSystem.length)
            in YKType.FORCE.units -> convertTo(targetSystem.force)
            in YKType.POWER.units -> convertTo(targetSystem.power)
            in YKType.ENERGY.units -> convertTo(targetSystem.energy)
            in YKType.PRESSURE.units -> convertTo(targetSystem.pressure)
            else -> return this
        }
    }

    val valueString: String get() = "${niceNumber(value)} ${unit.shortUnit}"

    fun valueAndConversion(targetUnit: YkUnit): String {
        return "$valueString = ${convertTo(targetUnit).valueString}"
    }

    fun nameAndValueInSystem(system: YkSystem): String {
        return name + ": " + convertToSystem(system).valueString
    }
}


// Examples used for testing

var wembyHeight = YkMeasurement(
    value = 2.26,
    unit = YkUnit.METERS,
    name = "Height",
    about = "How tall is Wemby"
)

var wembyWeight = YkMeasurement(
    value = 95.0,
    unit = YkUnit.KILOGRAMS,
    name = "Weight",
    about = "How much does Wemby weigh"
)

var shuttleWeight = YkMeasurement(
    value = 4480000.0,
    unit = YkUnit.POUNDS,
    name = "Weight",
    about = "Weight of the Space Shuttle"
)