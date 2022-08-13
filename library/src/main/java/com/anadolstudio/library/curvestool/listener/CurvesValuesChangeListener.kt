package com.anadolstudio.library.curvestool.listener

import android.graphics.Point

interface CurvesValuesChangeListener {

    fun onWhiteChanelChanged(points: List<Point>)

    fun onRedChanelChanged(points: List<Point>)

    fun onGreenChanelChanged(points: List<Point>)

    fun onBlueChanelChanged(points: List<Point>)

    fun onReset()


    class Simple(
        private val onWhiteChanelChanged: (List<Point>) -> Unit,
        private val onRedChanelChanged: (List<Point>) -> Unit,
        private val onGreenChanelChanged: (List<Point>) -> Unit,
        private val onBlueChanelChanged: (List<Point>) -> Unit,
        private val onReset: () -> Unit
    ) : CurvesValuesChangeListener {

        override fun onWhiteChanelChanged(points: List<Point>) = onWhiteChanelChanged.invoke(points)

        override fun onRedChanelChanged(points: List<Point>) = onRedChanelChanged.invoke(points)

        override fun onGreenChanelChanged(points: List<Point>) = onGreenChanelChanged.invoke(points)

        override fun onBlueChanelChanged(points: List<Point>) = onBlueChanelChanged.invoke(points)

        override fun onReset() = onReset.invoke()
    }

    class Save(
        private val onChanged: (List<Point>, List<Point>, List<Point>, List<Point>) -> Unit,
        private val onReset: () -> Unit
    ) : CurvesValuesChangeListener {

        private var whitePoints = listOf<Point>()
        private var redPoints = listOf<Point>()
        private var greenPoints = listOf<Point>()
        private var bluePoints = listOf<Point>()

        override fun onWhiteChanelChanged(points: List<Point>) {
            whitePoints = points
            notifyListener()
        }

        override fun onRedChanelChanged(points: List<Point>) {
            redPoints = points
            notifyListener()
        }

        override fun onGreenChanelChanged(points: List<Point>) {
            greenPoints = points
            notifyListener()
        }

        override fun onBlueChanelChanged(points: List<Point>) {
            bluePoints = points
            notifyListener()
        }

        override fun onReset() {
            onReset.invoke()

            whitePoints = listOf()
            redPoints = listOf()
            greenPoints = listOf()
            bluePoints = listOf()
        }

        private fun notifyListener() {
            onChanged.invoke(whitePoints, redPoints, greenPoints, bluePoints)
        }
    }

}
