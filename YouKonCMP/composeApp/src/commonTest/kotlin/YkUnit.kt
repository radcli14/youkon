package com.dcsim.youkon

import model.YkUnit
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class YkUnitTest {
    private val absTol = 1e-4

    @Test
    fun testKilograms() {
        val kilograms = YkUnit.KILOGRAMS
        assertContains(kilograms.equivalentUnits(), YkUnit.KILOGRAMS)
        assertContains(kilograms.equivalentUnits(), YkUnit.POUNDS)
        assertContains(kilograms.equivalentUnits(), YkUnit.SLUGS)
        assertEquals(1.0, kilograms.conversionFactor(YkUnit.KILOGRAMS), absTol)
        assertEquals(2.20462, kilograms.conversionFactor(YkUnit.POUNDS), absTol)
        assertEquals(0.0685218, kilograms.conversionFactor(YkUnit.SLUGS), absTol)
    }

    @Test
    fun testPounds() {
        val pounds = YkUnit.POUNDS
        assertContains(pounds.equivalentUnits(), YkUnit.KILOGRAMS)
        assertContains(pounds.equivalentUnits(), YkUnit.POUNDS)
        assertContains(pounds.equivalentUnits(), YkUnit.SLUGS)
        assertEquals(0.453592, pounds.conversionFactor(YkUnit.KILOGRAMS), absTol)
        assertEquals(1.0, pounds.conversionFactor(YkUnit.POUNDS), absTol)
        assertEquals(0.0310809, pounds.conversionFactor(YkUnit.SLUGS), absTol)
    }

    @Test
    fun testSlugs() {
        val slugs = YkUnit.SLUGS
        assertContains(slugs.equivalentUnits(), YkUnit.KILOGRAMS)
        assertContains(slugs.equivalentUnits(), YkUnit.POUNDS)
        assertContains(slugs.equivalentUnits(), YkUnit.SLUGS)
        assertEquals(14.5939, slugs.conversionFactor(YkUnit.KILOGRAMS), absTol)
        assertEquals(32.174, slugs.conversionFactor(YkUnit.POUNDS), absTol)
        assertEquals(1.0, slugs.conversionFactor(YkUnit.SLUGS), absTol)
    }


    @Test
    fun testMeters() {
        val meters = YkUnit.METERS
        assertContains(meters.equivalentUnits(), YkUnit.FEET)
        assertContains(meters.equivalentUnits(), YkUnit.INCHES)
        assertEquals(3.28084, meters.conversionFactor(YkUnit.FEET), absTol)
        assertEquals(39.3701, meters.conversionFactor(YkUnit.INCHES), absTol)
    }

    @Test
    fun testFeet() {
        val feet = YkUnit.FEET
        assertContains(feet.equivalentUnits(), YkUnit.METERS)
        assertContains(feet.equivalentUnits(), YkUnit.INCHES)
        assertEquals(0.3048, feet.conversionFactor(YkUnit.METERS), absTol)
        assertEquals(12.0, feet.conversionFactor(YkUnit.INCHES), absTol)
    }

    @Test
    fun testInches() {
        val inches = YkUnit.INCHES
        assertContains(inches.equivalentUnits(), YkUnit.METERS)
        assertContains(inches.equivalentUnits(), YkUnit.FEET)
        assertEquals(0.0254, inches.conversionFactor(YkUnit.METERS), absTol)
        assertEquals(0.0833333, inches.conversionFactor(YkUnit.FEET), absTol)
    }

    @Test
    fun testNewtons() {
        val newtons = YkUnit.NEWTONS
        assertContains(newtons.equivalentUnits(), YkUnit.NEWTONS)
        assertContains(newtons.equivalentUnits(), YkUnit.POUND_FORCE)
        assertEquals(1.0, newtons.conversionFactor(YkUnit.NEWTONS), absTol)
        assertEquals(0.224809, newtons.conversionFactor(YkUnit.POUND_FORCE), absTol)
    }

    @Test
    fun testPoundForce() {
        val poundForce = YkUnit.POUND_FORCE
        assertContains(poundForce.equivalentUnits(), YkUnit.NEWTONS)
        assertContains(poundForce.equivalentUnits(), YkUnit.POUND_FORCE)
        assertEquals(4.44822, poundForce.conversionFactor(YkUnit.NEWTONS), absTol)
        assertEquals(1.0, poundForce.conversionFactor(YkUnit.POUND_FORCE), absTol)
    }

    @Test
    fun testKilogramsPerMeterCubed() {
        val kgm3 = YkUnit.KILOGRAMS_PER_METER_CUBED
        assertContains(kgm3.equivalentUnits(), YkUnit.KILOGRAMS_PER_METER_CUBED)
        assertContains(kgm3.equivalentUnits(), YkUnit.SLUGS_PER_FOOT_CUBED)
        assertEquals(1.0, kgm3.conversionFactor(YkUnit.KILOGRAMS_PER_METER_CUBED), absTol)
        // TODO: Verify conversion factors for density
        assertEquals(0.00194, kgm3.conversionFactor(YkUnit.SLUGS_PER_FOOT_CUBED), absTol)
    }

    @Test
    fun testSlugssPerFootCubed() {
        val slugFt3 = YkUnit.SLUGS_PER_FOOT_CUBED
        assertContains(slugFt3.equivalentUnits(), YkUnit.KILOGRAMS_PER_METER_CUBED)
        assertContains(slugFt3.equivalentUnits(), YkUnit.SLUGS_PER_FOOT_CUBED)
        assertEquals(515.379, slugFt3.conversionFactor(YkUnit.KILOGRAMS_PER_METER_CUBED), absTol)
        assertEquals(1.0, slugFt3.conversionFactor(YkUnit.SLUGS_PER_FOOT_CUBED), absTol)
    }

    @Test
    fun testWatts() {
        val watts = YkUnit.WATTS
        assertContains(watts.equivalentUnits(), YkUnit.WATTS)
        assertContains(watts.equivalentUnits(), YkUnit.HORSEPOWER)
        assertEquals(1.0, watts.conversionFactor(YkUnit.WATTS), absTol)
        assertEquals(0.00134102, watts.conversionFactor(YkUnit.HORSEPOWER), absTol)
    }

    @Test
    fun testHorsepower() {
        val horsepower = YkUnit.HORSEPOWER
        assertContains(horsepower.equivalentUnits(), YkUnit.WATTS)
        assertContains(horsepower.equivalentUnits(), YkUnit.HORSEPOWER)
        assertEquals(745.7, horsepower.conversionFactor(YkUnit.WATTS), absTol)
        assertEquals(1.0, horsepower.conversionFactor(YkUnit.HORSEPOWER), absTol)
    }

    @Test
    fun testJoules() {
        val joules = YkUnit.JOULES
        assertContains(joules.equivalentUnits(), YkUnit.JOULES)
        assertContains(joules.equivalentUnits(), YkUnit.FOOT_POUND_ENERGY)
        assertContains(joules.equivalentUnits(), YkUnit.BRITISH_THERMAL_UNIT)
        assertEquals(1.0, joules.conversionFactor(YkUnit.JOULES), absTol)
        assertEquals(0.737562, joules.conversionFactor(YkUnit.FOOT_POUND_ENERGY), absTol)
        assertEquals(0.000947817, joules.conversionFactor(YkUnit.BRITISH_THERMAL_UNIT), absTol)
    }

    @Test
    fun testFootPoundEnergy() {
        val footPound = YkUnit.FOOT_POUND_ENERGY
        assertContains(footPound.equivalentUnits(), YkUnit.JOULES)
        assertContains(footPound.equivalentUnits(), YkUnit.BRITISH_THERMAL_UNIT)
        assertContains(footPound.equivalentUnits(), YkUnit.FOOT_POUND_ENERGY)
        assertEquals(1.35582, footPound.conversionFactor(YkUnit.JOULES), absTol)
        assertEquals(1.0, footPound.conversionFactor(YkUnit.FOOT_POUND_ENERGY), absTol)
        assertEquals(0.00128507, footPound.conversionFactor(YkUnit.BRITISH_THERMAL_UNIT), absTol)
    }

    @Test
    fun testBTU() {
        val btu = YkUnit.BRITISH_THERMAL_UNIT
        assertContains(btu.equivalentUnits(), YkUnit.JOULES)
        assertContains(btu.equivalentUnits(), YkUnit.FOOT_POUND_ENERGY)
        assertContains(btu.equivalentUnits(), YkUnit.BRITISH_THERMAL_UNIT)
        assertEquals(1055.06, btu.conversionFactor(YkUnit.JOULES), absTol)
        assertEquals(778.169, btu.conversionFactor(YkUnit.FOOT_POUND_ENERGY), 0.01)
        assertEquals(1.0, btu.conversionFactor(YkUnit.BRITISH_THERMAL_UNIT), absTol)
    }

    @Test
    fun testPascals() {
        val pascals = YkUnit.PASCALS
        assertContains(pascals.equivalentUnits(), YkUnit.PASCALS)
        assertContains(pascals.equivalentUnits(), YkUnit.POUNDS_PER_SQUARE_INCH)
        assertContains(pascals.equivalentUnits(), YkUnit.ATM)
        assertContains(pascals.equivalentUnits(), YkUnit.BARS)

        assertEquals(1.0, pascals.conversionFactor(YkUnit.PASCALS), absTol)
        assertEquals(0.000145037737732368, pascals.conversionFactor(YkUnit.POUNDS_PER_SQUARE_INCH), absTol)
        assertEquals(9.86923266716012e-06, pascals.conversionFactor(YkUnit.ATM), absTol)
        assertEquals(1.0e-05, pascals.conversionFactor(YkUnit.BARS), absTol)
    }

    @Test
    fun testPSI() {
        val psi = YkUnit.POUNDS_PER_SQUARE_INCH
        assertContains(psi.equivalentUnits(), YkUnit.PASCALS)
        assertContains(psi.equivalentUnits(), YkUnit.POUNDS_PER_SQUARE_INCH)
        assertContains(psi.equivalentUnits(), YkUnit.ATM)
        assertContains(psi.equivalentUnits(), YkUnit.BARS)

        assertEquals(6894.76, psi.conversionFactor(YkUnit.PASCALS), absTol)
        assertEquals(1.0, psi.conversionFactor(YkUnit.POUNDS_PER_SQUARE_INCH), absTol)
        assertEquals(0.068045963534013, psi.conversionFactor(YkUnit.ATM), absTol)
        assertEquals(0.0689476, psi.conversionFactor(YkUnit.BARS), absTol)
    }

    @Test
    fun testATM() {
        val atm = YkUnit.ATM
        assertContains(atm.equivalentUnits(), YkUnit.PASCALS)
        assertContains(atm.equivalentUnits(), YkUnit.POUNDS_PER_SQUARE_INCH)
        assertContains(atm.equivalentUnits(), YkUnit.ATM)
        assertContains(atm.equivalentUnits(), YkUnit.BARS)

        assertEquals(101325.0, atm.conversionFactor(YkUnit.PASCALS), absTol)
        assertEquals(14.696, atm.conversionFactor(YkUnit.POUNDS_PER_SQUARE_INCH), absTol)
        assertEquals(1.0, atm.conversionFactor(YkUnit.ATM), absTol)
        assertEquals(1.01325, atm.conversionFactor(YkUnit.BARS), absTol)
    }

    @Test
    fun testBars() {
        val bars = YkUnit.BARS
        assertContains(bars.equivalentUnits(), YkUnit.PASCALS)
        assertContains(bars.equivalentUnits(), YkUnit.POUNDS_PER_SQUARE_INCH)
        assertContains(bars.equivalentUnits(), YkUnit.ATM)
        assertContains(bars.equivalentUnits(), YkUnit.BARS)

        assertEquals(100000.0, bars.conversionFactor(YkUnit.PASCALS), absTol)
        assertEquals(14.5038, bars.conversionFactor(YkUnit.POUNDS_PER_SQUARE_INCH), absTol)
        assertEquals(0.98692326716012, bars.conversionFactor(YkUnit.ATM), absTol)
        assertEquals(1.0, bars.conversionFactor(YkUnit.BARS), absTol)
    }

    @Test
    fun testCelsius() {
        val celsius = YkUnit.CELSIUS
        assertContains(celsius.equivalentUnits(), YkUnit.FAHRENHEIT)
        assertContains(celsius.equivalentUnits(), YkUnit.KELVIN)
        assertContains(celsius.equivalentUnits(), YkUnit.RANKINE)

        assertEquals(68.0 , celsius.convert(20.0, YkUnit.FAHRENHEIT))
        assertEquals(273.15, celsius.convert(0.0, YkUnit.KELVIN))
        assertEquals(491.67, celsius.convert(0.0, YkUnit.RANKINE))
    }

    @Test
    fun testFahrenheit() {
        val fahrenheit = YkUnit.CELSIUS
        assertContains(fahrenheit.equivalentUnits(), YkUnit.CELSIUS)
        assertContains(fahrenheit.equivalentUnits(), YkUnit.KELVIN)
        assertContains(fahrenheit.equivalentUnits(), YkUnit.RANKINE)

        assertEquals(20.0 , fahrenheit.convert(68.0, YkUnit.CELSIUS))
        assertEquals(255.37, fahrenheit.convert(0.0, YkUnit.KELVIN))
        assertEquals(459.67, fahrenheit.convert(0.0, YkUnit.RANKINE))
    }
}