package com.dcsim.youkon

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class YkUnitTest {
    private val absTol = 1e-6

    @Test
    fun testMeters() {
        val meters = YkUnit.FEET
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
}