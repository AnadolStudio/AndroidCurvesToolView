package com.anadolstudio.library

import com.anadolstudio.library.curvestool.util.scaleTo
import org.junit.Test

import org.junit.Assert.*

class ScaleTest {

    @Test
    fun scaleToTestMaxValue() {
        val actual = 1000F.scaleTo(1000, 255)

        assertEquals(255, actual)
    }
    @Test
    fun scaleToTestMediumValue() {
        val actual = 500F.scaleTo(1000, 255)

        assertEquals(127, actual)
    }
    @Test
    fun scaleToTestMinValue() {
        val actual = 0F.scaleTo(1000, 255)

        assertEquals(0, actual)
    }
}
