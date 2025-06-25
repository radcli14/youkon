package model

/**
 * Defines a consistent system of units, such as SI (kg, m, N), imperial (slug, ft, lbf), etc.
 * Must be ordered (MASS, LENGTH, SPEED, FORCE, DENSITY, POWER, ENERGY, PRESSURE, TEMPERATURE).
 * May access the mass, length, speed, force, power, energy, or pressure units for this system
 * via named, get-only parameters.
 *
 * @param units the array of `YkUnit` objects belonging to this system of units
 */
enum class YkSystem(val text: String, private val units: ConsistentUnits) {
    SI(text = "kg-m-N", units=ConsistentUnits(
        mass = YkUnit.KILOGRAMS,
        length = YkUnit.METERS,
        speed = YkUnit.METERS_PER_SECOND,
        acceleration = YkUnit.METERS_PER_SECOND_SQUARED,
        force = YkUnit.NEWTONS,
        density = YkUnit.KILOGRAMS_PER_METER_CUBED,
        power = YkUnit.WATTS,
        energy = YkUnit.JOULES,
        pressure = YkUnit.PASCALS,
        temperature = YkUnit.CELSIUS,
        area = YkUnit.METERS_SQUARED,
        volume = YkUnit.METERS_CUBED,
        torque = YkUnit.NEWTON_METERS,
        stiffness = YkUnit.NEWTONS_PER_METER
    )),
    IMPERIAL(text = "slug-ft-lbf", units = ConsistentUnits(
        mass = YkUnit.SLUGS,
        length = YkUnit.FEET,
        speed = YkUnit.FEET_PER_SECOND,
        acceleration = YkUnit.FEET_PER_SECOND_SQUARED,
        force = YkUnit.POUND_FORCE,
        density = YkUnit.SLUGS_PER_FOOT_CUBED,
        power = YkUnit.FOOT_POUND_PER_SECOND,
        energy = YkUnit.FOOT_POUND_ENERGY,
        pressure = YkUnit.POUNDS_PER_SQUARE_FOOT,
        temperature = YkUnit.FAHRENHEIT,
        area = YkUnit.FEET_SQUARED,
        volume = YkUnit.FEET_CUBED,
        torque = YkUnit.FOOT_POUNDS_TORQUE,
        stiffness = YkUnit.POUNDS_PER_FOOT
    )),
    INCH(text = "in-lbf", units = ConsistentUnits(
        mass = YkUnit.SLINCH,
        length = YkUnit.INCHES,
        speed = YkUnit.INCHES_PER_SECOND,
        acceleration = YkUnit.INCHES_PER_SECOND_SQUARED,
        force = YkUnit.POUND_FORCE,
        density = YkUnit.SLINCH_PER_INCH_CUBED,
        power = YkUnit.INCH_POUND_PER_SECOND,
        energy = YkUnit.INCH_POUND_ENERGY,
        pressure = YkUnit.POUNDS_PER_SQUARE_INCH,
        temperature = YkUnit.FAHRENHEIT,
        area = YkUnit.INCHES_SQUARED,
        volume = YkUnit.INCHES_CUBED,
        torque = YkUnit.INCH_POUNDS_TORQUE,
        stiffness = YkUnit.POUNDS_PER_INCH
    )),
    ROBLOX(text = "RMU-s-R", units = ConsistentUnits(
        mass = YkUnit.RMU,
        length = YkUnit.STUDS,
        speed = YkUnit.STUDS_PER_SECOND,
        acceleration = YkUnit.STUDS_PER_SECOND_SQUARED,
        force = YkUnit.ROWTONS,
        density = YkUnit.RMU_PER_STUD_CUBED,
        power = YkUnit.ROWTON_STUD_PER_SECOND,
        energy = YkUnit.ROWTON_STUD_ENERGY,
        pressure = YkUnit.ROWTON_PER_STUD_SQUARED,
        temperature = YkUnit.CELSIUS,
        area = YkUnit.STUDS_SQUARED,
        volume = YkUnit.STUDS_CUBED,
        torque = YkUnit.ROWTON_STUD_TORQUE,
        stiffness = YkUnit.ROWTONS_PER_STUD
    ))
    ;

    val basicSystems: Array<YkSystem> get() = arrayOf(SI, IMPERIAL)

    val mass get() = units.mass
    val length get() = units.length
    val speed get() = units.speed
    val acceleration get() = units.acceleration
    val force get() = units.force
    val density get() = units.density
    val power get() = units.power
    val energy get() = units.energy
    val pressure get() = units.pressure
    val temperature get() = units.temperature
    val area get() = units.area
    val volume get() = units.volume
    val torque get() = units.torque
    val stiffness get() = units.stiffness

    /**
     * Defines a set of units that are consistent with one another for simulation modeling purposes
     */
    class ConsistentUnits(
        val mass: YkUnit,
        val length: YkUnit,
        val speed: YkUnit,
        val acceleration: YkUnit,
        val force: YkUnit,
        val density: YkUnit,
        val power: YkUnit,
        val energy: YkUnit,
        val pressure: YkUnit,
        val temperature: YkUnit,
        val area: YkUnit,
        val volume: YkUnit,
        val torque: YkUnit,
        val stiffness: YkUnit
    )
}
