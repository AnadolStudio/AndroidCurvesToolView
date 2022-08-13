package com.anadolstudio.library.curvestool.util

import kotlin.math.roundToInt

fun Float.scaleTo(currentMax: Int, needMax: Int): Int {
    val result = ((this / currentMax) * needMax)

    return when (result > needMax / 2) {
        true -> result.roundToInt()
        false -> result.toInt()
    }
}

fun Int.scaleTo(currentMax: Int, needMax: Int): Int = this.toFloat().scaleTo(currentMax, needMax)

fun Float.inRange(center: Float, range: Float): Boolean = this in center - range..center + range

fun Int.inRange(center: Float, range: Float): Boolean = this.toFloat().inRange(center, range)
