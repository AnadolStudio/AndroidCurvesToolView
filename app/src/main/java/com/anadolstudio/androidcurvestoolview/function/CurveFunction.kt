package com.anadolstudio.androidcurvestoolview.function

import android.graphics.Point

class CurveFunction(
    private val rgb: List<Point>,
    private val r: List<Point>,
    private val g: List<Point>,
    private val b: List<Point>,
    function: Function = Function.Empty
) : FunctionDecorator(function) {

    private companion object {

        const val SPACE = " "
    }

    override fun type(): String = "@curve"

    override fun getFunction(): String = super.getFunction() + convertAllPointsToString()

    private fun convertAllPointsToString(): String =
        "RGB" + convertPointsToString(rgb) +
                "R" + convertPointsToString(r) +
                "G" + convertPointsToString(g) +
                "B" + convertPointsToString(b)


    private fun convertPointsToString(points: List<Point>): String = points.joinToString(
        separator = SPACE,
        prefix = SPACE,
        postfix = SPACE,
        transform = this::pointValueToString
    )

    private fun pointValueToString(point: Point): String = "(${point.x}, ${point.y})"
}
