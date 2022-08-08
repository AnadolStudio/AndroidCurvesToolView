package com.anadolstudio.library.curvestool.util

import android.graphics.Canvas
import android.graphics.Paint

fun Canvas.drawLine(startX: Int, startY: Int, stopX: Int, stopY: Int, paint: Paint) {
    this.drawLine(startX.toFloat(), startY.toFloat(), stopX.toFloat(), stopY.toFloat(), paint)
}
