package com.anadolstudio.library.curvestool.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toPointF
import com.anadolstudio.library.curvestool.util.dpToPx
import com.anadolstudio.library.curvestool.util.drawLine
import com.anadolstudio.library.curvestool.util.forEachWithPreviousAndNext
import kotlin.random.Random

class CurvesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val AFTER_FIRST = 1
        private const val INTENSITY = 0.15F

        //        private const val INTENSITY = 0.085F
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
            whitePoints.add(Point(startX, endY).toPointF())
            whitePoints.addAll(testPoints)
            whitePoints.add(Point(endX, startY).toPointF())
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBorder(canvas)
        drawCurveCubicBezier(canvas, whitePoints)
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

    private fun drawCurveCubicBezier(canvas: Canvas, points: List<PointF>) {
        val path = Path()

        val first = points.first()
        path.moveTo(first.x, first.y)

        points.drop(AFTER_FIRST)
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

        canvas.drawPath(path, themeManager.curvePaint)
    }

    private fun drawBorder(canvas: Canvas) {
        canvas.drawLine(startX, endY, endX, startY, themeManager.borderPaint) // Draw diagonal line
        canvas.drawLine(startX, startY, endX, startY, themeManager.borderPaint) // Draw top line
        canvas.drawLine(endX, startY, endX, endY, themeManager.borderPaint) // Draw right line
        canvas.drawLine(endX, endY, startX, endY, themeManager.borderPaint) // Draw bottom line
        canvas.drawLine(startX, endY, startX, startY, themeManager.borderPaint) // Draw left line
    }

}

val testPoints = listOf(
    Point((100), (300)).toPointF(),
    Point((200), (500)).toPointF(),
    Point((300), (100)).toPointF(),
    Point((400), (500)).toPointF(),
    Point((450), (400)).toPointF(),
    Point((550), (600)).toPointF(),
    Point((650), (900)).toPointF(),
    Point((750), (1300)).toPointF(),
    Point((850), (1300)).toPointF(),
    Point((950), (1300)).toPointF()
)
