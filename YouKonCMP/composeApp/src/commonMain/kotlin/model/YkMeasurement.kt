package model
import UUIDGenerator
import kotlinx.serialization.*
import kotlinx.serialization.json.*

/// Holds a variable with units assigned to it
@Serializable
data class YkMeasurement(
    var value: Double,
    var unit: YkUnit,
    var name: String = "New Variable",
    var about: String = "",
    val id: String = UUIDGenerator().generateUUID()
) {

    companion object {
        fun new(): YkMeasurement {
            return YkMeasurement(1.0, YkUnit.METERS, "New Measurement", "Description")
        }
    }

    fun asJsonString(): String {
        return Json{ prettyPrint = true }.encodeToString(this)
    }

    /// Convert from the current measurement unit into a different unit, using the Unit type as input
    fun convertTo(targetUnit: YkUnit): YkMeasurement {
        return YkMeasurement(unit.convert(value, targetUnit), targetUnit)
    }

    /// Converts the measurement to a consistent system of measurements, like SI (kg-m-N), Imperial (slug-ft-pound), etc
    fun convertToSystem(targetSystem: YkSystem): YkMeasurement {
        return when (unit) {
            in YkType.MASS.units -> convertTo(targetSystem.mass)
            in YkType.LENGTH.units -> convertTo(targetSystem.length)
            in YkType.SPEED.units -> convertTo(targetSystem.speed)
            in YkType.ACCELERATION.units -> convertTo(targetSystem.acceleration)
            in YkType.FORCE.units -> convertTo(targetSystem.force)
            in YkType.DENSITY.units -> convertTo(targetSystem.density)
            in YkType.POWER.units -> convertTo(targetSystem.power)
            in YkType.ENERGY.units -> convertTo(targetSystem.energy)
            in YkType.PRESSURE.units -> convertTo(targetSystem.pressure)
            in YkType.TEMPERATURE.units -> convertTo(targetSystem.temperature)
            in YkType.AREA.units -> convertTo(targetSystem.area)
            in YkType.VOLUME.units -> convertTo(targetSystem.volume)
            in YkType.TORQUE.units -> convertTo(targetSystem.torque)
            in YkType.STIFFNESS.units -> convertTo(targetSystem.stiffness)
            else -> return this
        }
    }

    val valueString: String get() = "${niceNumber(value)} ${unit.shortUnit}"

    fun unitAndConversion(targetUnit: YkUnit): String {
        return "➜  ${convertTo(targetUnit).valueString}"
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

/*
var wembyWeight = YkMeasurement(
    value = 95.0,
    unit = YkUnit.KILOGRAMS,
    name = "Weight",
    about = "How much does Wemby weigh"
)
*/

var shuttleWeight = YkMeasurement(
    value = 4480000.0,
    unit = YkUnit.POUNDS,
    name = "Weight",
    about = "Weight of the Space Shuttle"
)
