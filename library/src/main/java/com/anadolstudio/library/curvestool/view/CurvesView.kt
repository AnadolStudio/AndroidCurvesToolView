package com.anadolstudio.library.curvestool.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.anadolstudio.library.curvestool.data.CurvePoint
import com.anadolstudio.library.curvestool.data.CurvePoint.Companion.MAX_VALUE
import com.anadolstudio.library.curvestool.util.*
import kotlin.math.max
import kotlin.math.min

class CurvesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val AFTER_FIRST = 1
        private const val INTENSITY = 0.15F
        private const val X_RANGE_PERCENT = 0.04167F // 4.167 %
        private const val Y_RANGE_PERCENT = 0.1F // 10 %
        private val PADDING = 8.dpToPx()
    }

    private var viewState: CurvesViewState = CurvesViewState.RED_STATE
        set(value) {
            field = value
            themeManager.curvePaint.color = value.toColor()
            themeManager.pointStrokePaint.color = value.toColor()

            currentPoints = when (value) {
                CurvesViewState.WHITE_STATE -> whitePoints
                CurvesViewState.RED_STATE -> redPoints
                CurvesViewState.GREEN_STATE -> greenPoints
                CurvesViewState.BLUE_STATE -> bluePoints
            }

            requestLayout()
        }

    private val themeManager = ThemeManager(viewState)

    private val whitePoints = mutableListOf<CurvePoint>()
    private val redPoints = mutableListOf<CurvePoint>()
    private val greenPoints = mutableListOf<CurvePoint>()
    private val bluePoints = mutableListOf<CurvePoint>()
    private var currentPoints = mutableListOf<CurvePoint>()
    private var selectedPoint: CurvePoint? = null
        set(value) {
            field?.isSelected = false
            field = value
            field?.isSelected = true
        }

    private var pointSize: Int = 30
    private var startX: Int = 0
    private var startY: Int = 0
    private var endX: Int = 0
    private var endY: Int = 0

    init {
        setBackgroundColor(Color.TRANSPARENT)
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        startX = PADDING
        startY = PADDING
        endX = width - PADDING
        endY = height - PADDING

        if (changed && currentPoints.isEmpty()) {
            currentPoints.add(CurvePoint(startX, endY, width, height))
            currentPoints.addAll(createTestPoints())
            currentPoints.add(CurvePoint(endX, startY, width, height))
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBorder(canvas)

        drawCurveCubicBezier(canvas, currentPoints.map { it.viewPoint })
        drawAllPoints(canvas, currentPoints)
    }

    private fun drawAllPoints(canvas: Canvas, points: List<CurvePoint>) {
        points.forEach { current -> drawPoint(canvas, current.viewPoint, current.isSelected) }
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

    private fun drawCurveCubicBezier(canvas: Canvas, points: List<PointF>) {
        val path = Path()

        val first = points.first()
        path.moveTo(first.x, first.y)

        points
            .drop(AFTER_FIRST)
            .forEachWithPreviousAndNext { prevPrevious, previous, current, next ->
                val prevDx = (current.x - prevPrevious.x) * INTENSITY
                val prevDy = (current.y - prevPrevious.y) * INTENSITY
                val curDx = (next.x - previous.x) * INTENSITY
                val curDy = (next.y - previous.y) * INTENSITY

                path.cubicTo(
                    previous.x + prevDx,
                    previous.y + prevDy,
                    current.x - curDx,
                    current.y - curDy,
                    current.x,
                    current.y
                )
            }

        canvas.drawInRect(startX, startY, endX, endY) {
            drawPath(path, themeManager.curvePaint)
        }
    }

    private fun Canvas.drawInRect(
        left: Int, top: Int, right: Int, bottom: Int, action: Canvas.() -> Unit
    ) {
        save()
        clipRect(left, top, right, bottom)
        action.invoke(this)
        restore()
    }

    private fun drawBorder(canvas: Canvas) {
        canvas.drawLine(startX, endY, endX, startY, themeManager.borderPaint) // Draw diagonal line
        canvas.drawLine(startX, startY, endX, startY, themeManager.borderPaint) // Draw top line
        canvas.drawLine(endX, startY, endX, endY, themeManager.borderPaint) // Draw right line
        canvas.drawLine(endX, endY, startX, endY, themeManager.borderPaint) // Draw bottom line
        canvas.drawLine(startX, endY, startX, startY, themeManager.borderPaint) // Draw left line
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean = true.also {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> onTap(event)
            MotionEvent.ACTION_MOVE -> onMove(event)
            MotionEvent.ACTION_UP -> onUp(event)
        }
    }

    private fun onTap(event: MotionEvent) = onTouchEventAction {
        val scaleX = event.x.scaleTo(width, MAX_VALUE)
        val scaleY = event.y.scaleTo(height, MAX_VALUE)

        val pointInRange = inXRange(scaleX)

        when (pointInRange != null) {
            true -> selectIfYInTouchRange(scaleY, pointInRange)
            false -> selectNewPoint(event)
        }

    }

    private fun selectNewPoint(event: MotionEvent) {
        selectedPoint = CurvePoint(event.x, event.y, width, height, true)
            .also(this::addNewPointAndSort)
    }

    private fun selectIfYInTouchRange(y: Int, pointInRange: CurvePoint) {
        val range = (height * Y_RANGE_PERCENT).scaleTo(height, MAX_VALUE).toFloat()

        if (y.inRange(pointInRange.curvePoint.y, range)) {
            selectedPoint = pointInRange
        }
    }

    private fun inXRange(scaleX: Int) = currentPoints.find { curvePoint ->
        val range = (width * X_RANGE_PERCENT).scaleTo(width, MAX_VALUE).toFloat()
        scaleX.inRange(curvePoint.curvePoint.x, range)
    }

    private fun addNewPointAndSort(newPoint: CurvePoint) {
        currentPoints.add(newPoint)
        currentPoints.sortBy { curvePoint -> curvePoint.curvePoint.x }
    }

    private fun onMove(event: MotionEvent) = onTouchEventAction {
        //TODO изменять точку
    }

    private fun onUp(event: MotionEvent) = onTouchEventAction {
        //TODO очистить текущую точку
    }

    private fun onTouchEventAction(action: () -> Unit) {
        action.invoke()
        invalidate()
    }


    private fun createTestPoints() = mutableListOf<CurvePoint>().apply {
        for (i in 1..7) {
            val viewX = i * 75
            val viewY = min(max((1000 * Math.random()).toInt(), startY), endY)

            add(
                CurvePoint(viewX, viewY, width, height)
            )
        }
    }

}
