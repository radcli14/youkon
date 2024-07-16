package model

/**
 * Defines a consistent system of units, such as SI (kg, m, N), imperial (slug, ft, lbf), etc.
 * Must be ordered (MASS, LENGTH, SPEED, FORCE, DENSITY, POWER, ENERGY, PRESSURE, TEMPERATURE).
 * May access the mass, length, speed, force, power, energy, or pressure units for this system
 * via named, get-only parameters.
 *
 * @param units the array of `YkUnit` objects belonging to this system of units
 */
enum class YkSystem(val text: String, private val units: Array<YkUnit>) {
    SI(text = "kg-m-N", units=arrayOf(
        YkUnit.KILOGRAMS,
        YkUnit.METERS,
        YkUnit.METERS_PER_SECOND,
        YkUnit.NEWTONS,
        YkUnit.KILOGRAMS_PER_METER_CUBED,
        YkUnit.WATTS,
        YkUnit.JOULES,
        YkUnit.PASCALS,
        YkUnit.CELSIUS
    )),
    IMPERIAL(text = "slug-ft-lbf", units = arrayOf(
        YkUnit.SLUGS,
        YkUnit.FEET,
        YkUnit.FEET_PER_SECOND,
        YkUnit.POUND_FORCE,
        YkUnit.SLUGS_PER_FOOT_CUBED,
        YkUnit.FOOT_POUND_PER_SECOND,
        YkUnit.FOOT_POUND_ENERGY,
        YkUnit.POUNDS_PER_SQUARE_FOOT,
        YkUnit.FAHRENHEIT
    )),
    INCH(text = "in-lbf", units = arrayOf(
        YkUnit.SLINCH,
        YkUnit.INCHES,
        YkUnit.INCHES_PER_SECOND,
        YkUnit.POUND_FORCE,
        YkUnit.SLINCH_PER_INCH_CUBED,
        YkUnit.INCH_POUND_PER_SECOND,
        YkUnit.INCH_POUND_ENERGY,
        YkUnit.POUNDS_PER_SQUARE_INCH,
        YkUnit.FAHRENHEIT
    )),
    ROBLOX(text = "studs-Rowtons", units = arrayOf(
        YkUnit.RMU,
        YkUnit.STUDS,
        YkUnit.STUDS_PER_SECOND,
        YkUnit.ROWTONS,
        YkUnit.RMU_PER_STUD_CUBED,
        YkUnit.ROWTON_STUD_PER_SECOND,
        YkUnit.ROWTON_STUD,
        YkUnit.ROWTON_PER_STUD_SQUARED,
        YkUnit.CELSIUS
    ))
    ;

    val mass get() = units[0]
    val length get() = units[1]
    val speed get() = units[2]
    val force get() = units[3]
    val density get() = units[4]
    val power get() = units[5]
    val energy get() = units[6]
    val pressure get() = units[7]
    val temperature get() = units[8]
}
