package com.dcsim.youkon

/// Holds a variable with units assigned to it
class YkMeasurement(
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

    /// Convert from the current measurement unit into a different unit, using the Unit type as input
    fun convertTo(targetUnit: YkUnit): YkMeasurement {
        return YkMeasurement(value * unit.conversionFactor(targetUnit), targetUnit)
    }

    /// Converts the measurement to a consistent system of measurements, like SI (kg-m-N), Imperial (slug-ft-pound), etc
    fun convertToSystem(targetSystem: String): YkMeasurement {
        return when (targetSystem) {
            "SI" -> convertToSI()
            else -> convertToSI()
        }
    }

    /// Converts this measurement to SI units (kg-m-N)
    private fun convertToSI(): YkMeasurement {
        return when (unit) {
            in unit.massUnits -> convertTo(YkUnit.KILOGRAMS)
            in unit.lengthUnits -> convertTo(YkUnit.METERS)
            in unit.forceUnits -> convertTo(YkUnit.NEWTONS)
            in unit.powerUnits -> convertTo(YkUnit.WATTS)
            in unit.energyUnits -> convertTo(YkUnit.JOULES)
            in unit.pressureUnits -> convertTo(YkUnit.PASCALS)
            else -> return this
        }
    }

    fun valueString(): String {
        return "${niceNumber(value)} ${unit.shortUnit}"
    }

    fun nameAndValueInSystem(system: String): String {
        return name + ": " + convertToSystem(system).valueString()
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