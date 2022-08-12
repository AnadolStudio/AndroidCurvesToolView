package com.anadolstudio.library.curvestool.data

import android.graphics.PointF
import com.anadolstudio.library.curvestool.util.scaleTo

data class CurvePoint(
    var viewPoint: PointF,
    var curvePoint: PointF,
    var isSelected: Boolean = false
) {

    constructor(
        viewX: Int,
        viewY: Int,
        width: Int,
        height: Int,
        isSelected: Boolean = false
    ) : this(
        viewX.toFloat(),
        viewY.toFloat(),
        width,
        height,
        isSelected
    )

    constructor(
        viewX: Float,
        viewY: Float,
        width: Int,
        height: Int,
        isSelected: Boolean = false
    ) : this(
        PointF(viewX, viewY),
        PointF(
            viewX.scaleTo(width, MAX_VALUE).toFloat(),
            viewY.scaleTo(height, MAX_VALUE).toFloat()
        ),
        isSelected
    )

    companion object {

        const val MAX_VALUE = 255
    }
}
