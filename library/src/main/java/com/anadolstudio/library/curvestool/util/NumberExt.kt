package com.anadolstudio.library.curvestool.util


fun Float.scaleTo(currentMax: Int, needMax: Int): Int  = ((this / currentMax) * needMax).toInt()

fun Int.scaleTo(currentMax: Int, needMax: Int): Int = this.toFloat().scaleTo(currentMax, needMax)

fun Float.inRange(center: Float, range: Float): Boolean = this in center - range..center + range

fun Int.inRange(center: Float, range: Float): Boolean = this.toFloat().inRange(center, range)
