package com.anadolstudio.library.curvestool.view

import android.graphics.*
import com.anadolstudio.library.curvestool.util.AntialiasPaint
import com.anadolstudio.library.curvestool.util.dpToPx

class ThemeManager(viewState: CurvesViewState) {

    internal var borderColor = Color.LTGRAY
        set(value) {
            field = value
            borderStrokePaint.color = value
            diagonalStrokePaint.color = value
        }

    internal var curveWidth = 2F.dpToPx()
        set(value) {
            field = value
            curvePaint.strokeWidth = value
        }

    internal var borderWidth = 2F.dpToPx()
        set(value) {
            field = value
            borderStrokePaint.strokeWidth = value
            diagonalStrokePaint.strokeWidth = value
        }

    internal var pointStrokeWidth = 2F.dpToPx()
        set(value) {
            field = value
            pointStrokePaint.strokeWidth = value
        }

    internal val borderStrokePaint = AntialiasPaint().apply {
        color = borderColor
        strokeWidth = borderWidth
        style = Paint.Style.STROKE
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
    }

    internal val diagonalStrokePaint = AntialiasPaint().apply {
        color = borderColor
        strokeWidth = borderWidth
        style = Paint.Style.STROKE
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
    }

    internal val borderFillPaint = AntialiasPaint().apply {
        color = Color.TRANSPARENT
        style = Paint.Style.FILL
    }

    internal val curvePaint = AntialiasPaint().apply {
        color = viewState.toColor()
        strokeWidth = curveWidth
        style = Paint.Style.STROKE
    }

    internal val curveFillPaint = AntialiasPaint().apply {
        color = viewState.toColor()
        style = Paint.Style.FILL
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
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
