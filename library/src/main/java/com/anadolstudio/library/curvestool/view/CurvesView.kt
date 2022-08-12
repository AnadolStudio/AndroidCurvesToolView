package com.anadolstudio.library.curvestool.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.toPoint
import androidx.core.graphics.toPointF
import com.anadolstudio.library.curvestool.data.CurvePoint
import com.anadolstudio.library.curvestool.data.CurvePoint.Companion.MAX_VALUE
import com.anadolstudio.library.curvestool.listener.CurvesValuesChangeListener
import com.anadolstudio.library.curvestool.util.*

class CurvesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val AFTER_FIRST = 1
        private const val INTENSITY = 0.035F
        private const val X_RANGE_PERCENT = 0.04167F // 4.167 %
        private const val Y_RANGE_PERCENT = 0.1F // 10 %
        private val PADDING = 8.dpToPx()
        private val DELETE_ZONE = 100
    }

    private var viewState: CurvesViewState = CurvesViewState.WHITE_STATE
        set(value) {
            field = value
            themeManager.curvePaint.color = value.toColor()
            themeManager.pointStrokePaint.color = value.toColor()
            themeManager.pointFillPaint.color = value.toColor()
            themeManager.pointFillSelectedPaint.color = value.toPointFillSelectedColor()

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
    private var borderWidth: Int = 0
    private var borderHeight: Int = 0

    private var changeListener: CurvesValuesChangeListener? = null

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
        borderWidth = endX - startX
        borderHeight = endY - startY

        if (changed && currentPoints.isEmpty()) {
            whitePoints.init()
            redPoints.init()
            greenPoints.init()
            bluePoints.init()

            showWhiteState()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBorder(canvas)

        val drawPoint = currentPoints.filter { curvePoint -> !curvePoint.candidateToDelete }

        drawCurveCubicBezier(canvas, drawPoint.map { it.viewPoint })
        drawAllPoints(canvas, drawPoint)
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
                val prevDx = (current.x - prevPrevious.x) * (INTENSITY + ratio(points))
                val prevDy = (current.y - prevPrevious.y) * (INTENSITY + ratio(points))
                val curDx = (next.x - previous.x) * (INTENSITY + ratio(points))
                val curDy = (next.y - previous.y) * (INTENSITY + ratio(points))

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

    private fun ratio(points: List<PointF>) = INTENSITY * points.count() / 4

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
            MotionEvent.ACTION_UP -> onUp()
        }
    }

    private fun onTap(event: MotionEvent) = onTouchEventAction {
        val scaleX = event.x.minus(startX).scaleTo(borderWidth, MAX_VALUE)
        val scaleY = event.y.minus(scaleY).scaleTo(borderHeight, MAX_VALUE)

        val pointInXRange = getPointInXRange(scaleX)
        selectedPoint = null

        when (pointInXRange != null) {
            true -> selectIfYInTouchRange(scaleY, pointInXRange)
            false -> selectNewPoint(event)
        }
    }

    private fun selectNewPoint(event: MotionEvent) {
        selectedPoint = CurvePoint(event.x, event.y, width, height, true)
            .also(this::addNewPointAndSort)
    }

    private fun selectIfYInTouchRange(y: Int, pointInRange: CurvePoint) {
        val range = (borderHeight * Y_RANGE_PERCENT).scaleTo(height, MAX_VALUE).toFloat()

        if (y.inRange(pointInRange.curvePoint.y, range)) {
            selectedPoint = pointInRange
        }
    }

    private fun getPointInXRange(scaleX: Int) = currentPoints.find { curvePoint ->
        val range = (borderWidth * X_RANGE_PERCENT).scaleTo(width, MAX_VALUE).toFloat()
        scaleX.inRange(curvePoint.curvePoint.x, range)
    }

    private fun addNewPointAndSort(newPoint: CurvePoint) {
        currentPoints.add(newPoint)
        currentPoints.sortBy { curvePoint -> curvePoint.curvePoint.x }
    }

    private fun onMove(event: MotionEvent) = onTouchEventAction {
        val selectPoint = selectedPoint ?: return@onTouchEventAction

        val scaleX = event.x.minus(PADDING).scaleTo(borderWidth, MAX_VALUE)
        val scaleY = event.y.minus(PADDING).scaleTo(borderHeight, MAX_VALUE)
        var isChanged = false

        if (currentPoints.first() != selectPoint && currentPoints.last() != selectPoint) {
            val range = borderWidth * X_RANGE_PERCENT

            val index = currentPoints.indexOf(selectPoint)
            val leftPoint = currentPoints[index - 1]
            val rightPoint = currentPoints[index + 1]

            if (event.x in leftPoint.viewPoint.x + range..rightPoint.viewPoint.x - range) {
                selectPoint.viewPoint.x = event.x
                selectPoint.curvePoint.x = scaleX.toFloat()
                isChanged = true
            }

            selectPoint.candidateToDelete = event.y.toInt() !in -DELETE_ZONE..height + DELETE_ZONE
        }

        when {
            event.y.toInt() in startY..endY -> {
                selectPoint.viewPoint.y = event.y
                selectPoint.curvePoint.y = scaleY.toFloat()
                isChanged = true
            }
            event.y.toInt() < startY ->{
                selectPoint.viewPoint.y = startY.toFloat()
                selectPoint.curvePoint.y = startY.scaleTo(borderHeight, MAX_VALUE).toFloat()
                isChanged = true
            }
            event.y.toInt() > endY ->{
                selectPoint.viewPoint.y = endY.toFloat()
                selectPoint.curvePoint.y = endY.scaleTo(borderHeight, MAX_VALUE).toFloat()
                isChanged = true
            }
        }

        if (isChanged) {
            notifyListener()
        }
    }

    private fun notifyListener() {
        val changedPoints = currentPoints
            .filter { curvePoint -> !curvePoint.candidateToDelete }
            .map { curvePoint ->
                PointF(curvePoint.curvePoint.x, MAX_VALUE - curvePoint.curvePoint.y).toPoint()
            }

        when (viewState) {
            CurvesViewState.WHITE_STATE -> changeListener?.onWhiteChanelChanged(changedPoints)
            CurvesViewState.RED_STATE -> changeListener?.onRedChanelChanged(changedPoints)
            CurvesViewState.GREEN_STATE -> changeListener?.onGreenChanelChanged(changedPoints)
            CurvesViewState.BLUE_STATE -> changeListener?.onBlueChanelChanged(changedPoints)
        }
    }

    private fun onUp() = onTouchEventAction { removeCandidateToDelete() }

    private fun removeCandidateToDelete() {
        currentPoints
            .find { point -> point.candidateToDelete }
            ?.also { candidateToDelete -> currentPoints.remove(candidateToDelete) }
    }

    private fun onTouchEventAction(action: () -> Unit) {
        action.invoke()
        invalidate()
    }

    fun showRedState() {
        viewState = CurvesViewState.RED_STATE
    }

    fun showGreenState() {
        viewState = CurvesViewState.GREEN_STATE
    }

    fun showBlueState() {
        viewState = CurvesViewState.BLUE_STATE
    }

    fun showWhiteState() {
        viewState = CurvesViewState.WHITE_STATE
    }

    fun setChangeListener(listener: CurvesValuesChangeListener) {
        changeListener = listener
    }

    private fun MutableList<CurvePoint>.init() {
        this.add(CurvePoint(Point(startX, endY).toPointF(), Point(0, MAX_VALUE).toPointF()))
        this.add(CurvePoint(Point(endX, startY).toPointF(), Point(MAX_VALUE, 0).toPointF()))
    }
}
