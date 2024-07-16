package model

/**
 * The `YkType` enum class defines groupings of `YkUnit` types that represent that same form
 * of measurement. Specifically, there are mass, length, force, power, energy, and pressure groups.
 *
 * @param units an array of `YkUnit` that all represent the same type of measurement
 */
enum class YkType(val units: Array<YkUnit>) {
    MASS(arrayOf(YkUnit.KILOGRAMS, YkUnit.GRAMS, YkUnit.METRIC_TONS,
        YkUnit.POUNDS, YkUnit.SLUGS, YkUnit.SLINCH,
        YkUnit.RMU)),
    LENGTH(arrayOf(
        YkUnit.METERS, YkUnit.KILOMETERS, YkUnit.CENTIMETERS, YkUnit.MILLIMETERS,
        YkUnit.FEET, YkUnit.INCHES, YkUnit.MILES, YkUnit.MILS,
        YkUnit.STUDS)),
    SPEED(arrayOf(
        YkUnit.METERS_PER_SECOND, YkUnit.KILOMETERS_PER_HOUR,
        YkUnit.FEET_PER_SECOND, YkUnit.INCHES_PER_SECOND, YkUnit.MILES_PER_HOUR,
        YkUnit.STUDS_PER_SECOND)),
    FORCE(arrayOf(YkUnit.NEWTONS, YkUnit.POUND_FORCE, YkUnit.DYNES, YkUnit.ROWTONS)),
    DENSITY(arrayOf(
        YkUnit.KILOGRAMS_PER_METER_CUBED,
        YkUnit.SLUGS_PER_FOOT_CUBED, YkUnit.SLINCH_PER_INCH_CUBED,
        YkUnit.POUND_MASS_PER_FOOT_CUBED, YkUnit.POUND_MASS_PER_INCH_CUBED,
        YkUnit.RMU_PER_STUD_CUBED)),
    POWER(arrayOf(YkUnit.WATTS, YkUnit.KILOWATTS,
        YkUnit.FOOT_POUND_PER_SECOND, YkUnit.INCH_POUND_PER_SECOND, YkUnit.HORSEPOWER,
        YkUnit.ROWTON_STUD_PER_SECOND)),
    ENERGY(arrayOf(YkUnit.JOULES,
        YkUnit.FOOT_POUND_ENERGY, YkUnit.INCH_POUND_ENERGY, YkUnit.BRITISH_THERMAL_UNIT,
        YkUnit.ROWTON_STUD)),
    PRESSURE(arrayOf(
        YkUnit.PASCALS, YkUnit.KILOPASCALS, YkUnit.MEGAPASCALS, YkUnit.GIGAPASCALS,
        YkUnit.POUNDS_PER_SQUARE_FOOT, YkUnit.KILOPOUNDS_PER_SQUARE_FOOT,
        YkUnit.POUNDS_PER_SQUARE_INCH, YkUnit.KILOPOUNDS_PER_SQUARE_INCH,
        YkUnit.ATMOSPHERES, YkUnit.BARS,
        YkUnit.ROWTON_PER_STUD_SQUARED)),
    TEMPERATURE(arrayOf(YkUnit.CELSIUS, YkUnit.FAHRENHEIT, YkUnit.KELVIN, YkUnit.RANKINE))
    ;

    val lowercasedString: String get() = this
        .toString()
        .replace("_", " ")
        .lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
