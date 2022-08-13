package com.anadolstudio.library.curvestool.listener

import android.graphics.Point

interface CurvesValuesChangeListener {

    fun onWhiteChanelChanged(points: List<Point>)

    fun onRedChanelChanged(points: List<Point>)

    fun onGreenChanelChanged(points: List<Point>)

    fun onBlueChanelChanged(points: List<Point>)


    class Simple(
        private val onWhiteChanelChanged: (List<Point>) -> Unit,
        private val onRedChanelChanged: (List<Point>) -> Unit,
        private val onGreenChanelChanged: (List<Point>) -> Unit,
        private val onBlueChanelChanged: (List<Point>) -> Unit
    ) : CurvesValuesChangeListener {

        override fun onWhiteChanelChanged(points: List<Point>) = onWhiteChanelChanged.invoke(points)

        override fun onRedChanelChanged(points: List<Point>) = onRedChanelChanged.invoke(points)

        override fun onGreenChanelChanged(points: List<Point>) = onGreenChanelChanged.invoke(points)

        override fun onBlueChanelChanged(points: List<Point>) = onBlueChanelChanged.invoke(points)
    }
}
