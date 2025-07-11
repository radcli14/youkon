package model

import org.jetbrains.compose.resources.StringResource
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.*

/**
 * The `YkType` enum class defines groupings of `YkUnit` types that represent that same form
 * of measurement. Specifically, there are mass, length, force, power, energy, and pressure groups.
 *
 * @param units an array of `YkUnit` that all represent the same type of measurement
 */
enum class YkType(val units: Array<YkUnit>, val stringResId: StringResource) {
    MASS(arrayOf(YkUnit.KILOGRAMS, YkUnit.GRAMS, YkUnit.METRIC_TONS,
        YkUnit.POUNDS, YkUnit.SLUGS, YkUnit.SLINCH,
        YkUnit.RMU, YkUnit.MILLIGRAMS, YkUnit.STONES, YkUnit.AVOIRDUPOIS_OUNCES,
        YkUnit.TROY_POUNDS), Res.string.type_mass),
    LENGTH(arrayOf(
        YkUnit.METERS, YkUnit.KILOMETERS, YkUnit.CENTIMETERS, YkUnit.MILLIMETERS,
        YkUnit.FEET, YkUnit.INCHES, YkUnit.MILES, YkUnit.MILS,
        YkUnit.STUDS), Res.string.type_length),
    SPEED(arrayOf(
        YkUnit.METERS_PER_SECOND, YkUnit.KILOMETERS_PER_HOUR,
        YkUnit.FEET_PER_SECOND, YkUnit.INCHES_PER_SECOND, YkUnit.MILES_PER_HOUR,
        YkUnit.STUDS_PER_SECOND, YkUnit.KNOTS), Res.string.type_speed),
    ACCELERATION(arrayOf(
        YkUnit.METERS_PER_SECOND_SQUARED, YkUnit.FEET_PER_SECOND_SQUARED, YkUnit.INCHES_PER_SECOND_SQUARED,
        YkUnit.STUDS_PER_SECOND_SQUARED, YkUnit.EARTH_GRAVITY
    ), Res.string.type_acceleration),
    FORCE(arrayOf(YkUnit.NEWTONS, YkUnit.POUND_FORCE, YkUnit.DYNES, YkUnit.ROWTONS,
        YkUnit.KILONEWTONS, YkUnit.KILOPOUNDS_FORCE), Res.string.type_force),
    AREA(arrayOf(
        YkUnit.METERS_SQUARED, YkUnit.KILOMETERS_SQUARED, YkUnit.CENTIMETERS_SQUARED, YkUnit.MILLIMETERS_SQUARED,
        YkUnit.FEET_SQUARED, YkUnit.INCHES_SQUARED, YkUnit.MILES_SQUARED,
        YkUnit.ACRES, YkUnit.HECTARES, YkUnit.STUDS_SQUARED
    ), Res.string.type_area),
    VOLUME(arrayOf(
        YkUnit.METERS_CUBED, YkUnit.CENTIMETERS_CUBED, YkUnit.MILLIMETERS_CUBED,
        YkUnit.FEET_CUBED, YkUnit.INCHES_CUBED,
        YkUnit.STUDS_CUBED,
        YkUnit.LITERS, YkUnit.US_GALLONS, YkUnit.IMPERIAL_GALLONS,
        YkUnit.US_QUARTS, YkUnit.IMPERIAL_QUARTS,
        YkUnit.US_PINTS, YkUnit.IMPERIAL_PINTS,
        YkUnit.US_CUPS, YkUnit.IMPERIAL_CUPS,
        YkUnit.US_FLUID_OUNCES, YkUnit.IMPERIAL_FLUID_OUNCES,
        YkUnit.US_TABLESPOONS, YkUnit.IMPERIAL_TABLESPOONS,
        YkUnit.US_TEASPOONS, YkUnit.IMPERIAL_TEASPOONS
    ), Res.string.type_volume),
    TEMPERATURE(arrayOf(YkUnit.CELSIUS, YkUnit.FAHRENHEIT, YkUnit.KELVIN, YkUnit.RANKINE), Res.string.type_temperature),
    DENSITY(arrayOf(
        YkUnit.KILOGRAMS_PER_METER_CUBED,
        YkUnit.SLUGS_PER_FOOT_CUBED, YkUnit.SLINCH_PER_INCH_CUBED,
        YkUnit.POUND_MASS_PER_FOOT_CUBED, YkUnit.POUND_MASS_PER_INCH_CUBED,
        YkUnit.RMU_PER_STUD_CUBED), Res.string.type_density),
    POWER(arrayOf(YkUnit.WATTS, YkUnit.KILOWATTS,
        YkUnit.FOOT_POUND_PER_SECOND, YkUnit.INCH_POUND_PER_SECOND, YkUnit.HORSEPOWER,
        YkUnit.ROWTON_STUD_PER_SECOND), Res.string.type_power),
    ENERGY(arrayOf(YkUnit.JOULES,
        YkUnit.FOOT_POUND_ENERGY, YkUnit.INCH_POUND_ENERGY, YkUnit.BRITISH_THERMAL_UNIT,
        YkUnit.ROWTON_STUD_ENERGY, YkUnit.KILOJOULES, YkUnit.MEGAJOULES), Res.string.type_energy),
    PRESSURE(arrayOf(
        YkUnit.PASCALS, YkUnit.KILOPASCALS, YkUnit.MEGAPASCALS, YkUnit.GIGAPASCALS,
        YkUnit.POUNDS_PER_SQUARE_FOOT, YkUnit.KILOPOUNDS_PER_SQUARE_FOOT,
        YkUnit.POUNDS_PER_SQUARE_INCH, YkUnit.KILOPOUNDS_PER_SQUARE_INCH,
        YkUnit.ATMOSPHERES, YkUnit.BARS,
        YkUnit.ROWTON_PER_STUD_SQUARED), Res.string.type_pressure),
    STIFFNESS(arrayOf(
        YkUnit.NEWTONS_PER_METER, YkUnit.KILONEWTONS_PER_METER,
        YkUnit.POUNDS_PER_FOOT, YkUnit.KILOPOUNDS_PER_FOOT,
        YkUnit.POUNDS_PER_INCH, YkUnit.KILOPOUNDS_PER_INCH,
        YkUnit.ROWTONS_PER_STUD
    ), Res.string.type_stiffness),
    TORQUE(arrayOf(
        YkUnit.NEWTON_METERS, YkUnit.KILONEWTON_METERS,
        YkUnit.FOOT_POUNDS_TORQUE, YkUnit.KILOFOOT_POUNDS_TORQUE,
        YkUnit.INCH_POUNDS_TORQUE, YkUnit.KILOINCH_POUNDS_TORQUE,
        YkUnit.ROWTON_STUD_TORQUE
    ), Res.string.type_torque)
    ;

    val basicTypes: Array<YkType> get() = arrayOf(MASS, LENGTH, SPEED, ACCELERATION, FORCE, TEMPERATURE)

    // val lowercasedString: String get() = this
    //    .toString()
    //    .replace("_", " ")
    //    .lowercase()
    //    .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
