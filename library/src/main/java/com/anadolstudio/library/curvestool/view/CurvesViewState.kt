package com.anadolstudio.library.curvestool.view

import android.graphics.Color

enum class CurvesViewState {
    WHITE_STATE,
    RED_STATE,
    GREEN_STATE,
    BLUE_STATE;

}

internal fun CurvesViewState.toColor() = when (this) {
    CurvesViewState.WHITE_STATE -> Color.WHITE
    CurvesViewState.RED_STATE -> Color.RED
    CurvesViewState.GREEN_STATE -> Color.GREEN
    CurvesViewState.BLUE_STATE -> Color.BLUE
}

internal fun CurvesViewState.toPointFillSelectedColor() = when (this) {
    CurvesViewState.WHITE_STATE -> Color.DKGRAY
    CurvesViewState.RED_STATE -> Color.DKGRAY
    CurvesViewState.GREEN_STATE -> Color.DKGRAY
    CurvesViewState.BLUE_STATE -> Color.DKGRAY
}
