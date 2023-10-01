package com.dcsim.youkon

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class YkUnitTest {
    private val absTol = 1e-6

    @Test
    fun testFeet() {
        val feet = YkUnit.FEET
        assertContains(feet.equivalentUnits(), YkUnit.METERS)
        assertContains(feet.equivalentUnits(), YkUnit.INCHES)
        assertEquals(0.3048, feet.conversionFactor(YkUnit.METERS), absTol)
        assertEquals(12.0, feet.conversionFactor(YkUnit.INCHES), absTol)
    }
}