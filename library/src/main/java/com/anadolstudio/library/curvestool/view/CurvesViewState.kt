package com.anadolstudio.library.curvestool.view

import android.graphics.Color

enum class CurvesViewState {
    WHITE_STATE,
    RED_STATE,
    GREEN_STATE,
    BLUE_STATE
}

private val RED = Color.parseColor("#CC0000")
private val GREEN = Color.parseColor("#669900")
private val BLUE = Color.parseColor("#0099CC")

internal fun CurvesViewState.toColor() = when (this) {
    CurvesViewState.WHITE_STATE -> Color.WHITE
    CurvesViewState.RED_STATE -> RED
    CurvesViewState.GREEN_STATE -> GREEN
    CurvesViewState.BLUE_STATE -> BLUE
}

internal fun CurvesViewState.toPointFillSelectedColor() = when (this) {
    CurvesViewState.WHITE_STATE -> Color.DKGRAY
    else -> Color.LTGRAY
}
