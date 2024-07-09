package model

/**
 * Defines a consistent system of units, such as SI (kg, m, N), imperial (slug, ft, lbf), etc.
 * Must be ordered (MASS, LENGTH, SPEED, FORCE, DENSITY, POWER, ENERGY, PRESSURE, TEMPERATURE).
 * May access the mass, length, speed, force, power, energy, or pressure units for this system
 * via named, get-only parameters.
 *
 * @param units the array of `YkUnit` objects belonging to this system of units
 */
enum class YkSystem(private val units: Array<YkUnit>) {
    SI(arrayOf
        (
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
    IMPERIAL(arrayOf(
        YkUnit.SLUGS,
        YkUnit.FEET,
        YkUnit.FEET_PER_SECOND,
        YkUnit.POUND_FORCE,
        YkUnit.SLUGS_PER_FOOT_CUBED,
        YkUnit.FOOT_POUND_PER_SECOND,
        YkUnit.FOOT_POUND_ENERGY,
        YkUnit.POUNDS_PER_SQUARE_FOOT,
        YkUnit.FAHRENHEIT
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
