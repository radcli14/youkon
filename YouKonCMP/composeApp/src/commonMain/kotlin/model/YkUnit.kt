package model

/**
 * The `YkUnit` enum class defines a unit of measurement, and conversion factors from that unit to
 * another unit representing the same type of dimension. The `enum class` is initialized with
 * a `Double` value `toBase` which represents the conversion factor from this unit to a base
 * unit, with the latter generally in SI. The conversion factor is the product of this `toBase`
 * with a calculated value `fromBase` for a second unit, with the latter being the inverse of
 * the second unit's `toBase` value. The `enum class` is also initiated with a `shortUnit`
 * string, which is a abbreviation of the unit used for display purposes. For conversions where
 * there is an offset, specifically temperature, there is an `offsetToBase` value.
 *
 * @param toBase the conversion factor to go from this unit to the base unit, typically in SI
 * @param shortUnit the more compact string representation of this unit
 * @param offsetToBase the offset to apply when converting from this unit to the base unit,
 * for example 32 degrees for temperature conversions from Fahrenheit to Celcius
 */
enum class YkUnit(private val toBase: Double, val shortUnit: String, private val offsetToBase: Double = 0.0) {
    UNITLESS(1.0, ""),
    // Mass
    KILOGRAMS(1.0, "kg"),
    GRAMS(1e-3, "g"),
    METRIC_TONS(1e3, "tons"),
    POUNDS(0.453592, "lbm"),
    SLUGS(14.5939, "slug"),
    SLINCH(175.126836, "lbf·s²/in"),
    RMU(21.952, "RMU"),
    // Length
    METERS(1.0, "m"),
    KILOMETERS(1e3, "km"),
    CENTIMETERS(0.01, "cm"),
    MILLIMETERS(1e-3, "mm"),
    FEET(0.3048, "ft"),
    INCHES(0.0254, "in"),
    MILES(0.3048*5280.0, "miles"),
    MILS(2.54e-5, "mils"),
    STUDS(0.28, "stud"),
    // Area
    METERS_SQUARED(1.0, "m²"),
    KILOMETERS_SQUARED(1e6, "km²"),
    CENTIMETERS_SQUARED(1e-4, "cm²"),
    MILLIMETERS_SQUARED(1e-6, "mm²"),
    FEET_SQUARED(0.092903, "ft²"),
    INCHES_SQUARED(6.4516e-4, "in²"),
    MILES_SQUARED(2589988.11, "miles²"),
    ACRES(4046.86, "acres"),
    HECTARES(10000.0, "hectares"),
    STUDS_SQUARED(0.0784, "stud²"),
    // Volume
    METERS_CUBED(1.0, "m³"),
    CENTIMETERS_CUBED(1e-6, "cm³"),
    MILLIMETERS_CUBED(1e-9, "mm³"),
    FEET_CUBED(0.0283168, "ft³"),
    INCHES_CUBED(1.63871e-5, "in³"),
    STUDS_CUBED(0.021952, "stud³"),
    LITERS(1e-3, "L"),
    US_GALLONS(3.78541e-3, "US gal"),
    IMPERIAL_GALLONS(4.54609e-3, "Imp gal"),
    US_QUARTS(0.946353e-3, "US qt"),
    IMPERIAL_QUARTS(1.13652e-3, "Imp qt"),
    US_PINTS(0.473176e-3, "US pt"),
    IMPERIAL_PINTS(0.568261e-3, "Imp pt"),
    US_CUPS(0.00024, "US cup"),
    IMPERIAL_CUPS(0.284131e-3, "Imp cup"),
    US_FLUID_OUNCES(0.0295735e-3, "US fl oz"),
    IMPERIAL_FLUID_OUNCES(0.0284131e-3, "Imp fl oz"),
    US_TABLESPOONS(14.78676478125e-6, "US tbsp"),
    IMPERIAL_TABLESPOONS(17.7581640625e-6, "Imp tbsp"),
    US_TEASPOONS(0.00492892e-3, "US tsp"),
    IMPERIAL_TEASPOONS(0.00591939e-3, "Imp tsp"),
    // Speed
    METERS_PER_SECOND(1.0, "m/s"),
    KILOMETERS_PER_HOUR(1.0/3.6, "km/h"),
    FEET_PER_SECOND(0.3048, "ft/s"),
    INCHES_PER_SECOND(0.0254, "in/s"),
    MILES_PER_HOUR(0.3048 * 5280.0 / 3600.0, "mph"),
    STUDS_PER_SECOND(0.28, "studs/s"),
    // Force
    NEWTONS(1.0, "N"),
    POUND_FORCE(4.44822, "lbf"),
    DYNES(1e-5, "dyne"),
    ROWTONS(1.0/0.163, "Rowtons"),
    // Density
    KILOGRAMS_PER_METER_CUBED(1.0, "kg/m³"),
    SLUGS_PER_FOOT_CUBED(515.379, "slug/ft³"),
    SLINCH_PER_INCH_CUBED(515.379*20736.0, "lbf·s⁴/in⁴"),
    POUND_MASS_PER_FOOT_CUBED(16.0185, "lbm/ft³"),
    POUND_MASS_PER_INCH_CUBED(27679.9, "lbm/in³"),
    RMU_PER_STUD_CUBED(1000.0, "RMU/stud³"),
    // Power
    WATTS(1.0, "W"),
    KILOWATTS(1000.0, "kW"),
    FOOT_POUND_PER_SECOND(1.35582, "ft·lbf/s"),
    INCH_POUND_PER_SECOND(1.35582/12.0, "in·lbf/s"),
    HORSEPOWER(745.7, "HP"),
    ROWTON_STUD_PER_SECOND(1.0/0.581, "R·S/s"),
    // Energy
    JOULES(1.0, "J"),
    FOOT_POUND_ENERGY(1.35582, "ft·lbf"),
    INCH_POUND_ENERGY(1.35582/12.0, "in·lbf"),
    BRITISH_THERMAL_UNIT(1055.06, "BTU"),
    ROWTON_STUD(1.0/0.581, "R·S"),
    // Pressure
    PASCALS(1.0, "Pa"),
    KILOPASCALS(1e3, "kPa"),
    MEGAPASCALS(1e6, "MPa"),
    GIGAPASCALS(1e9, "GPa"),
    POUNDS_PER_SQUARE_FOOT(47.8803, "psf"),
    KILOPOUNDS_PER_SQUARE_FOOT(47.8803e3, "psf"),
    POUNDS_PER_SQUARE_INCH(6894.76, "psi"),
    KILOPOUNDS_PER_SQUARE_INCH(6894.76e3, "ksi"),
    ATMOSPHERES(101325.0, "atm"),
    BARS(100000.0, "bar"),
    ROWTON_PER_STUD_SQUARED(101325.0/1290.0, "R/stud²"),
    // Temperature
    CELSIUS(1.0, "C"),
    FAHRENHEIT(5.0/9.0, "F", 32.0),
    KELVIN(1.0, "K", 273.15),
    RANKINE(5.0/9.0, "R", 491.67)
    ;

    val basicUnits: Array<YkUnit> get() {
        return arrayOf(UNITLESS,
            KILOGRAMS, POUNDS, SLUGS,
            METERS, FEET, INCHES,
            KILOMETERS_PER_HOUR, MILES_PER_HOUR,
            NEWTONS, POUND_FORCE
        )
    }

    /// Convert a numeric value from this unit to a target unit
    fun convert(value: Double, targetUnit: YkUnit): Double {
        return conversionFactor(targetUnit) * (value - offsetToBase) + targetUnit.offsetToBase
    }

    /// The string representation of this unit, with first letter capitalized, all others lowercase
    val lowercasedString: String get() = this
        .toString()
        .replace("_", " ")
        .lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

    /// Conversion factor to go from the base unit of this type to this unit
    private val fromBase: Double get() = 1.0 / toBase

    /// All of the unit types that exist in YouKon
    val allUnits get() = entries.toTypedArray()

    /// Provide an array of units that the measurement may be converted to
    fun equivalentUnits(): Array<YkUnit> {
        return when (this) {
            in YkType.MASS.units -> YkType.MASS.units
            in YkType.LENGTH.units -> YkType.LENGTH.units
            in YkType.SPEED.units -> YkType.SPEED.units
            in YkType.FORCE.units -> YkType.FORCE.units
            in YkType.DENSITY.units ->  YkType.DENSITY.units
            in YkType.POWER.units -> YkType.POWER.units
            in YkType.ENERGY.units -> YkType.ENERGY.units
            in YkType.PRESSURE.units -> YkType.PRESSURE.units
            in YkType.TEMPERATURE.units -> YkType.TEMPERATURE.units
            in YkType.AREA.units -> YkType.AREA.units
            in YkType.VOLUME.units -> YkType.VOLUME.units
            else -> arrayOf()
        }
    }

    /// Conversion factor from the current measurement unit into a different unit
    fun conversionFactor(targetUnit: YkUnit): Double {
        return toBase * targetUnit.fromBase
    }

    /// Check that the target unit is not a duplicate of the selected unit,
    /// and is valid as the same type of measure
    fun getNewTargetUnit(oldTarget: YkUnit, isExtended: Boolean = true): YkUnit {
        if (oldTarget == this || oldTarget !in equivalentUnits()) {
            return newTargetUnit(isExtended)
        }
        return oldTarget
    }

    /// When the user modifies the `From` dropdown, this provides the first option for a target unit
    /// that can be converted from the `measurement.unit` but is not the same unit
    fun newTargetUnit(isExtended: Boolean): YkUnit {
        return try {
            when(isExtended) {
                true -> equivalentUnits().first { it != this }
                false -> equivalentUnits().filter { basicUnits.contains(it) }.first { it != this }
            }
        } catch(error: NoSuchElementException) {
            Log.e("YkUnit", "newTargetUnit could not find a unit matching filter, returning itself")
            this
        }
    }
}
