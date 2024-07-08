package com.dcsim.youkon

//import com.dcengineer.youkon.YkSystem
//import com.dcengineer.youkon.YkType
//import com.dcengineer.youkon.YkUnit
import model.YkSystem
import model.YkType
import model.YkUnit
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class YkSystemTest {
    @Test
    fun testSI() {
        val system = YkSystem.SI
        assertEquals(system.mass, YkUnit.KILOGRAMS)
        assertEquals(system.length, YkUnit.METERS)
        assertEquals(system.force, YkUnit.NEWTONS)
        assertEquals(system.density, YkUnit.KILOGRAMS_PER_METER_CUBED)
        assertEquals(system.power, YkUnit.WATTS)
        assertEquals(system.energy, YkUnit.JOULES)
        assertEquals(system.pressure, YkUnit.PASCALS)
        assertEquals(system.temperature, YkUnit.CELSIUS)

        assertContains(YkType.MASS.units, system.mass)
        assertContains(YkType.LENGTH.units, system.length)
        assertContains(YkType.FORCE.units, system.force)
        assertContains(YkType.DENSITY.units, system.density)
        assertContains(YkType.POWER.units, system.power)
        assertContains(YkType.ENERGY.units, system.energy)
        assertContains(YkType.PRESSURE.units, system.pressure)
        assertContains(YkType.TEMPERATURE.units, system.temperature)
    }

    @Test
    fun testImperial() {
        val system = YkSystem.IMPERIAL
        assertEquals(system.mass, YkUnit.SLUGS)
        assertEquals(system.length, YkUnit.FEET)
        assertEquals(system.force, YkUnit.POUND_FORCE)
        assertEquals(system.density, YkUnit.SLUGS_PER_FOOT_CUBED)
        assertEquals(system.power, YkUnit.FOOT_POUND_PER_SECOND)
        assertEquals(system.energy, YkUnit.FOOT_POUND_ENERGY)
        assertEquals(system.pressure, YkUnit.POUNDS_PER_SQUARE_FOOT)
        assertEquals(system.temperature, YkUnit.FAHRENHEIT)

        assertContains(YkType.MASS.units, system.mass)
        assertContains(YkType.LENGTH.units, system.length)
        assertContains(YkType.FORCE.units, system.force)
        assertContains(YkType.DENSITY.units, system.density)
        assertContains(YkType.POWER.units, system.power)
        assertContains(YkType.ENERGY.units, system.energy)
        assertContains(YkType.PRESSURE.units, system.pressure)
        assertContains(YkType.TEMPERATURE.units, system.temperature)
    }
}