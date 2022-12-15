package com.heightmeter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class OverlayInfo constructor(context: Context?, attributeSet: AttributeSet?) :
    View(context, attributeSet) {

    private val rectBounds: MutableList<RectF> = mutableListOf()
    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context!!, android.R.color.holo_green_light)
        strokeWidth = 5f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Pass it a list of RectF (rectBounds)
        rectBounds.forEach { canvas.drawRect(it, paint) }
    }

    fun drawRectBounds(rectBounds: List<RectF>) {
        this.rectBounds.clear()
        this.rectBounds.addAll(rectBounds)
        invalidate()
    }

    private fun getStatusBarHeight(): Int {
        val resourceId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else 0
    }

}





