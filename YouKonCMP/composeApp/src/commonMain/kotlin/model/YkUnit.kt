package model

import org.jetbrains.compose.resources.StringResource
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.*

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
enum class YkUnit(
    private val toBase: Double,
    val shortUnit: String,
    private val offsetToBase: Double = 0.0,
    val stringResId: StringResource
) {
    UNITLESS(1.0, "", stringResId = Res.string.unit_unitless),
    // Mass
    KILOGRAMS(1.0, "kg", stringResId = Res.string.unit_kilograms),
    GRAMS(1e-3, "g", stringResId = Res.string.unit_grams),
    METRIC_TONS(1e3, "tons", stringResId = Res.string.unit_metric_tons),
    POUNDS(0.453592, "lbm", stringResId = Res.string.unit_pounds),
    SLUGS(14.5939, "slug", stringResId = Res.string.unit_slugs),
    SLINCH(175.126836, "lbf·s²/in", stringResId = Res.string.unit_slinch),
    RMU(21.952, "RMU", stringResId = Res.string.unit_rmu),
    MILLIGRAMS(1e-6, "mg", stringResId = Res.string.unit_milligrams),
    STONES(6.35029318, "st", stringResId = Res.string.unit_stones),
    AVOIRDUPOIS_OUNCES(0.028349523125, "oz", stringResId = Res.string.unit_avoirdupois_ounces),
    TROY_POUNDS(0.3732417216, "lb t", stringResId = Res.string.unit_troy_pounds),
    // Length
    METERS(1.0, "m", stringResId = Res.string.unit_meters),
    KILOMETERS(1e3, "km", stringResId = Res.string.unit_kilometers),
    CENTIMETERS(0.01, "cm", stringResId = Res.string.unit_centimeters),
    MILLIMETERS(1e-3, "mm", stringResId = Res.string.unit_millimeters),
    FEET(0.3048, "ft", stringResId = Res.string.unit_feet),
    INCHES(0.0254, "in", stringResId = Res.string.unit_inches),
    MILES(0.3048*5280.0, "miles", stringResId = Res.string.unit_miles),
    MILS(2.54e-5, "mils", stringResId = Res.string.unit_mils),
    STUDS(0.28, "stud", stringResId = Res.string.unit_studs),
    // Area
    METERS_SQUARED(1.0, "m²", stringResId = Res.string.unit_meters_squared),
    KILOMETERS_SQUARED(1e6, "km²", stringResId = Res.string.unit_kilometers_squared),
    CENTIMETERS_SQUARED(1e-4, "cm²", stringResId = Res.string.unit_centimeters_squared),
    MILLIMETERS_SQUARED(1e-6, "mm²", stringResId = Res.string.unit_millimeters_squared),
    FEET_SQUARED(0.092903, "ft²", stringResId = Res.string.unit_feet_squared),
    INCHES_SQUARED(6.4516e-4, "in²", stringResId = Res.string.unit_inches_squared),
    MILES_SQUARED(2589988.11, "miles²", stringResId = Res.string.unit_miles_squared),
    ACRES(4046.86, "acres", stringResId = Res.string.unit_acres),
    HECTARES(10000.0, "hectares", stringResId = Res.string.unit_hectares),
    STUDS_SQUARED(0.0784, "stud²", stringResId = Res.string.unit_studs_squared),
    // Volume
    METERS_CUBED(1.0, "m³", stringResId = Res.string.unit_meters_cubed),
    CENTIMETERS_CUBED(1e-6, "cm³", stringResId = Res.string.unit_centimeters_cubed),
    MILLIMETERS_CUBED(1e-9, "mm³", stringResId = Res.string.unit_millimeters_cubed),
    FEET_CUBED(0.0283168, "ft³", stringResId = Res.string.unit_feet_cubed),
    INCHES_CUBED(1.63871e-5, "in³", stringResId = Res.string.unit_inches_cubed),
    STUDS_CUBED(0.021952, "stud³", stringResId = Res.string.unit_studs_cubed),
    LITERS(1e-3, "L", stringResId = Res.string.unit_liters),
    US_GALLONS(3.78541e-3, "US gal", stringResId = Res.string.unit_us_gallons),
    IMPERIAL_GALLONS(4.54609e-3, "Imp gal", stringResId = Res.string.unit_imperial_gallons),
    US_QUARTS(0.946353e-3, "US qt", stringResId = Res.string.unit_us_quarts),
    IMPERIAL_QUARTS(1.13652e-3, "Imp qt", stringResId = Res.string.unit_imperial_quarts),
    US_PINTS(0.473176e-3, "US pt", stringResId = Res.string.unit_us_pints),
    IMPERIAL_PINTS(0.568261e-3, "Imp pt", stringResId = Res.string.unit_imperial_pints),
    US_CUPS(0.00024, "US cup", stringResId = Res.string.unit_us_cups),
    IMPERIAL_CUPS(0.284131e-3, "Imp cup", stringResId = Res.string.unit_imperial_cups),
    US_FLUID_OUNCES(0.0295735e-3, "US fl oz", stringResId = Res.string.unit_us_fluid_ounces),
    IMPERIAL_FLUID_OUNCES(0.0284131e-3, "Imp fl oz", stringResId = Res.string.unit_imperial_fluid_ounces),
    US_TABLESPOONS(14.78676478125e-6, "US tbsp", stringResId = Res.string.unit_us_tablespoons),
    IMPERIAL_TABLESPOONS(17.7581640625e-6, "Imp tbsp", stringResId = Res.string.unit_imperial_tablespoons),
    US_TEASPOONS(0.00492892e-3, "US tsp", stringResId = Res.string.unit_us_teaspoons),
    IMPERIAL_TEASPOONS(0.00591939e-3, "Imp tsp", stringResId = Res.string.unit_imperial_teaspoons),
    // Speed
    METERS_PER_SECOND(1.0, "m/s", stringResId = Res.string.unit_meters_per_second),
    KILOMETERS_PER_HOUR(1.0/3.6, "km/h", stringResId = Res.string.unit_kilometers_per_hour),
    FEET_PER_SECOND(0.3048, "ft/s", stringResId = Res.string.unit_feet_per_second),
    INCHES_PER_SECOND(0.0254, "in/s", stringResId = Res.string.unit_inches_per_second),
    MILES_PER_HOUR(0.3048 * 5280.0 / 3600.0, "mph", stringResId = Res.string.unit_miles_per_hour),
    STUDS_PER_SECOND(0.28, "studs/s", stringResId = Res.string.unit_studs_per_second),
    KNOTS(0.5144444444, "knots", stringResId = Res.string.unit_knots),
    // Acceleration
    METERS_PER_SECOND_SQUARED(1.0, "m/s²", stringResId = Res.string.unit_meters_per_second_squared),
    FEET_PER_SECOND_SQUARED(0.3048, "ft/s²", stringResId = Res.string.unit_feet_per_second_squared),
    INCHES_PER_SECOND_SQUARED(0.0254, "in/s²", stringResId = Res.string.unit_inches_per_second_squared),
    STUDS_PER_SECOND_SQUARED(0.28, "stud/s²", stringResId = Res.string.unit_studs_per_second_squared),
    EARTH_GRAVITY(9.80665, "g", stringResId = Res.string.unit_earth_gravity),
    // Force
    NEWTONS(1.0, "N", stringResId = Res.string.unit_newtons),
    KILONEWTONS(1000.0, "kN", stringResId = Res.string.unit_kilonewtons),
    POUND_FORCE(4.4482216152548, "lbf", stringResId = Res.string.unit_pound_force),
    KILOPOUNDS_FORCE(4448.2216152548, "kipf", stringResId = Res.string.unit_kilopounds_force),
    DYNES(1e-5, "dyne", stringResId = Res.string.unit_dynes),
    ROWTONS(1.0/0.163, "Rowtons", stringResId = Res.string.unit_rowtons),
    // Density
    KILOGRAMS_PER_METER_CUBED(1.0, "kg/m³", stringResId = Res.string.unit_kilograms_per_meter_cubed),
    SLUGS_PER_FOOT_CUBED(515.379, "slug/ft³", stringResId = Res.string.unit_slugs_per_foot_cubed),
    SLINCH_PER_INCH_CUBED(515.379*20736.0, "lbf·s⁴/in⁴", stringResId = Res.string.unit_slinch_per_inch_cubed),
    POUND_MASS_PER_FOOT_CUBED(16.0185, "lbm/ft³", stringResId = Res.string.unit_pound_mass_per_foot_cubed),
    POUND_MASS_PER_INCH_CUBED(27679.9, "lbm/in³", stringResId = Res.string.unit_pound_mass_per_inch_cubed),
    RMU_PER_STUD_CUBED(1000.0, "RMU/stud³", stringResId = Res.string.unit_rmu_per_stud_cubed),
    // Power
    WATTS(1.0, "W", stringResId = Res.string.unit_watts),
    KILOWATTS(1000.0, "kW", stringResId = Res.string.unit_kilowatts),
    FOOT_POUND_PER_SECOND(1.35582, "ft·lbf/s", stringResId = Res.string.unit_foot_pound_per_second),
    INCH_POUND_PER_SECOND(1.35582/12.0, "in·lbf/s", stringResId = Res.string.unit_inch_pound_per_second),
    HORSEPOWER(745.7, "HP", stringResId = Res.string.unit_horsepower),
    ROWTON_STUD_PER_SECOND(1.0/0.581, "R·S/s", stringResId = Res.string.unit_rowton_stud_per_second),
    // Energy
    JOULES(1.0, "J", stringResId = Res.string.unit_joules),
    KILOJOULES(1e3, "kJ", stringResId = Res.string.unit_kilojoules),
    MEGAJOULES(1e6, "MJ", stringResId = Res.string.unit_megajoules),
    FOOT_POUND_ENERGY(1.35582, "ft·lbf", stringResId = Res.string.unit_foot_pound_energy),
    INCH_POUND_ENERGY(1.35582/12.0, "in·lbf", stringResId = Res.string.unit_inch_pound_energy),
    BRITISH_THERMAL_UNIT(1055.06, "BTU", stringResId = Res.string.unit_british_thermal_unit),
    ROWTON_STUD_ENERGY(1.0/0.581, "R·S", stringResId = Res.string.unit_rowton_stud_energy),
    // Pressure
    PASCALS(1.0, "Pa", stringResId = Res.string.unit_pascals),
    KILOPASCALS(1e3, "kPa", stringResId = Res.string.unit_kilopascals),
    MEGAPASCALS(1e6, "MPa", stringResId = Res.string.unit_megapascals),
    GIGAPASCALS(1e9, "GPa", stringResId = Res.string.unit_gigapascals),
    POUNDS_PER_SQUARE_FOOT(47.8803, "psf", stringResId = Res.string.unit_pounds_per_square_foot),
    KILOPOUNDS_PER_SQUARE_FOOT(47.8803e3, "psf", stringResId = Res.string.unit_kilopounds_per_square_foot),
    POUNDS_PER_SQUARE_INCH(6894.76, "psi", stringResId = Res.string.unit_pounds_per_square_inch),
    KILOPOUNDS_PER_SQUARE_INCH(6894.76e3, "ksi", stringResId = Res.string.unit_kilopounds_per_square_inch),
    ATMOSPHERES(101325.0, "atm", stringResId = Res.string.unit_atmospheres),
    BARS(100000.0, "bar", stringResId = Res.string.unit_bars),
    ROWTON_PER_STUD_SQUARED(101325.0/1290.0, "R/stud²", stringResId = Res.string.unit_rowton_per_stud_squared),
    // Temperature
    CELSIUS(1.0, "C", stringResId = Res.string.unit_celsius),
    FAHRENHEIT(5.0/9.0, "F", 32.0, stringResId = Res.string.unit_fahrenheit),
    KELVIN(1.0, "K", 273.15, stringResId = Res.string.unit_kelvin),
    RANKINE(5.0/9.0, "R", 491.67, stringResId = Res.string.unit_rankine),
    // Torque
    NEWTON_METERS(1.0, "N·m", stringResId = Res.string.unit_newton_meters),
    FOOT_POUNDS_TORQUE(1.3558179483, "ft·lbf", stringResId = Res.string.unit_foot_pounds_torque),
    INCH_POUNDS_TORQUE(0.112985, "in·lbf", stringResId = Res.string.unit_inch_pounds_torque),
    KILONEWTON_METERS(1000.0, "kN·m", stringResId = Res.string.unit_kilonewton_meters),
    KILOFOOT_POUNDS_TORQUE(1355.8179483, "kft·lbf", stringResId = Res.string.unit_kilofoot_pounds_torque),
    KILOINCH_POUNDS_TORQUE(112.985, "kin·lbf", stringResId = Res.string.unit_kiloinch_pounds_torque),
    ROWTON_STUD_TORQUE(0.28 / 0.163, "R·S", stringResId = Res.string.unit_rowton_stud_torque),
    // Stiffness
    NEWTONS_PER_METER(1.0, "N/m", stringResId = Res.string.unit_newtons_per_meter),
    POUNDS_PER_FOOT(14.593902887139, "lbf/ft", stringResId = Res.string.unit_pounds_per_foot),
    POUNDS_PER_INCH(175.126835, "lbf/in", stringResId = Res.string.unit_pounds_per_inch),
    ROWTONS_PER_STUD(21.91060473, "R/S", stringResId = Res.string.unit_rowtons_per_stud),
    KILONEWTONS_PER_METER(1000.0, "kN/m", stringResId = Res.string.unit_kilonewtons_per_meter),
    KILOPOUNDS_PER_FOOT(14593.902887139, "klbf/ft", stringResId = Res.string.unit_kilopounds_per_foot),
    KILOPOUNDS_PER_INCH(175126.835, "klbf/in", stringResId = Res.string.unit_kilopounds_per_inch),
    ;

    val basicUnits: Array<YkUnit> get() {
        return arrayOf(UNITLESS,
            KILOGRAMS, POUNDS, SLUGS,
            METERS, FEET, INCHES,
            KILOMETERS_PER_HOUR, MILES_PER_HOUR,
            METERS_PER_SECOND_SQUARED, FEET_PER_SECOND_SQUARED, INCHES_PER_SECOND_SQUARED, EARTH_GRAVITY,
            NEWTONS, POUND_FORCE,
            CELSIUS, FAHRENHEIT
        )
    }

    /// Convert a numeric value from this unit to a target unit
    fun convert(value: Double, targetUnit: YkUnit): Double {
        return conversionFactor(targetUnit) * (value - offsetToBase) + targetUnit.offsetToBase
    }

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
            in YkType.TORQUE.units -> YkType.TORQUE.units
            in YkType.STIFFNESS.units -> YkType.STIFFNESS.units
            in YkType.TEMPERATURE.units -> YkType.TEMPERATURE.units
            in YkType.AREA.units -> YkType.AREA.units
            in YkType.VOLUME.units -> YkType.VOLUME.units
            in YkType.ACCELERATION.units -> YkType.ACCELERATION.units
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
