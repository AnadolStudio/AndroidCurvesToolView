package com.anadolstudio.library.curvestool.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.anadolstudio.library.curvestool.util.dpToPx
import com.anadolstudio.library.curvestool.util.drawLine
import com.anadolstudio.library.curvestool.util.forEachWithPrevious
import kotlin.math.abs
import kotlin.random.Random

class CurvesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val MIN_SIZE = 2
        private val PADDING = 8.dpToPx()
    }

    private var viewState: CurvesViewState = CurvesViewState.RED_STATE
        set(value) {
            field = value
            themeManager.curvePaint.color = value.toColor()
            themeManager.pointStrokePaint.color = value.toColor()
            requestLayout()
        }

    private val themeManager = ThemeManager(viewState)
    private val whitePoints = mutableListOf<PointF>()
    private var pointSize: Int = 30

    private val startX: Int
        get() = PADDING

    private val startY: Int
        get() = PADDING

    private val endX: Int
        get() = width - PADDING

    private val endY: Int
        get() = height - PADDING

    init {
        setBackgroundColor(Color.TRANSPARENT)
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (changed && whitePoints.isEmpty()) {
            whitePoints.add(PointF(startX.toFloat(), endY.toFloat()))
            whitePoints.add(PointF(endX.toFloat(), startY.toFloat()))
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBorder(canvas)
        drawCurve(canvas, whitePoints)
        drawAllPoints(canvas, whitePoints)
    }

    private fun drawAllPoints(canvas: Canvas, points: MutableList<PointF>) {
        points.forEach { current -> drawPoint(canvas, current, Random.nextBoolean()) }
    }

    private fun drawPoint(canvas: Canvas, current: PointF, isSelected: Boolean = false) {
        val rect = rectF(current, pointSize / 2)
        canvas.drawOval(rect, themeManager.getPointFillPaint(isSelected)) // Fill
        canvas.drawOval(rect, themeManager.pointStrokePaint) // Stroke
    }

    private fun rectF(current: PointF, halfSize: Int) = RectF(
        current.x - halfSize,
        current.y - halfSize,
        current.x + halfSize,
        current.y + halfSize
    )

    private fun drawCurve(canvas: Canvas, points: List<PointF>) {
        val path = Path()

        if (points.size > MIN_SIZE) {
            points.forEachWithPrevious { previous, current ->
                previous ?: let {
                    path.moveTo(current.x, current.y)

                    return@forEachWithPrevious
                }

                val center = getCenterPoint(previous, current)
                val delta = abs(center.x - current.x) / 2

                path.quadTo(center.x - delta, previous.y, center.x, center.y)
                path.quadTo(center.x + delta, current.y, current.x, current.y)
            }
        } else { // if two points draw line
            val first = points.first()
            val last = points.last()
            path.moveTo(first.x, first.y)
            path.lineTo(last.x, last.y)
        }

        canvas.drawPath(path, themeManager.curvePaint)
    }

    private fun getCenterPoint(previous: PointF, current: PointF): PointF {
        val x = listOf(previous.x, current.x).average().toFloat()
        val y = listOf(previous.y, current.y).average().toFloat()

        return PointF(x, y)
    }

    private fun drawBorder(canvas: Canvas) {
        canvas.drawLine(startX, endY, endX, startY, themeManager.borderPaint) // Draw diagonal line
        canvas.drawLine(startX, startY, endX, startY, themeManager.borderPaint) // Draw top line
        canvas.drawLine(endX, startY, endX, endY, themeManager.borderPaint) // Draw right line
        canvas.drawLine(endX, endY, startX, endY, themeManager.borderPaint) // Draw bottom line
        canvas.drawLine(startX, endY, startX, startY, themeManager.borderPaint) // Draw left line
    }

}
