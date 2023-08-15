package com.dcsim.youkon

/// Holds a variable with units assigned to it
class Measurement(
    var value: Double,
    var unit: MeasurementUnit,
    var name: String = "New Variable",
    var description: String = ""
) {

    companion object {
        fun new(): Measurement {
            return Measurement(1.0, MeasurementUnit.METERS, "New Measurement", "Description")
        }
    }

    /// Convert from the current measurement unit into a different unit, using the Unit type as input
    fun convertTo(targetUnit: MeasurementUnit): Measurement {
        return Measurement(value * unit.conversionFactor(targetUnit), targetUnit)
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
            in unit.massUnits -> convertTo(MeasurementUnit.KILOGRAMS)
            in unit.lengthUnits -> convertTo(MeasurementUnit.METERS)
            in unit.forceUnits -> convertTo(MeasurementUnit.NEWTONS)
            in unit.powerUnits -> convertTo(MeasurementUnit.WATTS)
            in unit.energyUnits -> convertTo(MeasurementUnit.JOULES)
            in unit.pressureUnits -> convertTo(MeasurementUnit.PASCALS)
            else -> return this
        }
    }

    override fun toString(): String {
        return "${niceNumber(value)} ${unit.shortUnit}"
    }

    fun nameAndValueInSystem(system: String): String {
        return name + ": " + convertToSystem(system).toString()
    }
}


// Examples used for testing

var wembyHeight = Measurement(
    value = 2.26,
    unit = MeasurementUnit.METERS,
    name = "Height",
    description = "How tall is Wemby"
)

var wembyWeight = Measurement(
    value = 95.0,
    unit = MeasurementUnit.KILOGRAMS,
    name = "Weight",
    description = "How much does Wemby weigh"
)

var shuttleWeight = Measurement(
    value = 4480000.0,
    unit = MeasurementUnit.POUNDS,
    name = "Weight",
    description = "Weight of the Space Shuttle"
)