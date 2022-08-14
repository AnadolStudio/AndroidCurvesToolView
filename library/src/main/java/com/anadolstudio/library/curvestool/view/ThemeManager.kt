package com.anadolstudio.library.curvestool.view

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import com.anadolstudio.library.curvestool.util.AntialiasPaint
import com.anadolstudio.library.curvestool.util.dpToPx

class ThemeManager(context: Context, viewState: CurvesViewState) {

    internal var borderStrokeColor = Color.LTGRAY
        set(value) {
            field = value
            borderStrokePaint.color = value
        }

    internal var borderFillColor = Color.TRANSPARENT
        set(value) {
            field = value
            borderFillPaint.color = value
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
        }

    internal var pointStrokeWidth = 2F.dpToPx()
        set(value) {
            field = value
            pointStrokePaint.strokeWidth = value
        }

    internal val borderStrokePaint = AntialiasPaint().apply {
        color = borderStrokeColor
        strokeWidth = borderWidth
        style = Paint.Style.STROKE
    }

    internal val borderFillPaint = AntialiasPaint().apply {
        color = borderFillColor
        style = Paint.Style.FILL
    }

    internal val curvePaint = AntialiasPaint().apply {
        color = viewState.toColor(context)
        strokeWidth = curveWidth
        style = Paint.Style.STROKE
    }

    internal val curveFillPaint = AntialiasPaint().apply {
        color = viewState.toColor(context)
        style = Paint.Style.FILL
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    internal val pointStrokePaint = AntialiasPaint().apply {
        color = viewState.toColor(context)
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
