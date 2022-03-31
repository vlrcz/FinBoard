package com.vlad.finboard.feature.charts

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.vlad.finboard.R

class PieChartView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private lateinit var pies: Map<Int, Float>

    @RequiresApi(VERSION_CODES.M)
    val secondaryColor = context.getColor(R.color.colorSecondary)

    fun setValues(pieChartMap: Map<Int, Float>) {
        this.pies = pieChartMap
    }

    init {
        isFocusable = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(secondaryColor)
        drawArcs(canvas)
        drawCenter(canvas)
    }

    private fun drawArcs(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
        }

        val rectF = RectF(0f, 0f, width.toFloat(), width.toFloat())
        var start = 0f
        pies.keys.forEach { colorInt ->
            paint.apply {
                color = colorInt
            }

            val weight = pies[colorInt] ?: return
            canvas.drawArc(rectF, start, weight, true, paint)
            start += weight
        }
    }

    private fun drawCenter(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = secondaryColor
        }
        val cx = (width / 2).toFloat()
        canvas.drawCircle(cx, cx, cx - 100f, paint)
    }
}