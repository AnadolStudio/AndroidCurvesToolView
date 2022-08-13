package com.anadolstudio.library.curvestool.view

import android.graphics.*
import com.anadolstudio.library.curvestool.util.AntialiasPaint
import com.anadolstudio.library.curvestool.util.dpToPx

class ThemeManager(viewState: CurvesViewState) {

    internal var borderColor = Color.LTGRAY
        set(value) {
            field = value
            borderPaint.color = value
        }

    internal var curveWidth = 2F.dpToPx()
        set(value) {
            field = value
            borderPaint.strokeWidth = value
            curvePaint.strokeWidth = value
        }

    internal var pointStrokeWidth = 2F.dpToPx()
        set(value) {
            field = value
            pointStrokePaint.strokeWidth = value
        }

    internal val borderPaint = AntialiasPaint().apply {
        color = borderColor
        strokeWidth = curveWidth
    }

    internal val curvePaint = AntialiasPaint().apply {
        color = viewState.toColor()
        strokeWidth = curveWidth
        style = Paint.Style.STROKE
    }

    internal val pointStrokePaint = AntialiasPaint().apply {
        color = viewState.toColor()
        strokeWidth = pointStrokeWidth
        style = Paint.Style.STROKE
    }

    internal val pointFillPaint = AntialiasPaint().apply {
        style = Paint.Style.FILL
    }

    internal val pointFillSelectedPaint = AntialiasPaint().apply {
        style = Paint.Style.FILL
    }

    internal fun getPointFillPaint(isSelected: Boolean): Paint = when (isSelected) {
        true -> pointFillSelectedPaint
        false -> pointFillPaint
    }

}
