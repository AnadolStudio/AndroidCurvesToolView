package com.anadolstudio.library.curvestool.view

import android.content.Context
import androidx.core.content.ContextCompat
import com.anadolstudio.library.R

enum class CurvesViewState {
    WHITE_STATE,
    RED_STATE,
    GREEN_STATE,
    BLUE_STATE
}

internal fun CurvesViewState.toColor(context: Context): Int = ContextCompat.getColor(
    context,
    when (this) {
        CurvesViewState.WHITE_STATE -> R.color.whiteCurveColor
        CurvesViewState.RED_STATE -> R.color.redCurveColor
        CurvesViewState.GREEN_STATE -> R.color.greenCurveColor
        CurvesViewState.BLUE_STATE -> R.color.blueCurveColor
    }
)

internal fun CurvesViewState.toPointFillSelectedColor(context: Context): Int =
    ContextCompat.getColor(
        context,
        when (this) {
            CurvesViewState.WHITE_STATE -> R.color.pointSelectedWhiteFillColor
            CurvesViewState.RED_STATE -> R.color.pointSelectedRedFillColor
            CurvesViewState.GREEN_STATE -> R.color.pointSelectedGreenFillColor
            CurvesViewState.BLUE_STATE -> R.color.pointSelectedBlueFillColor
        }
    )
