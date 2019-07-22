package com.sunhapper.x.spedit.gif.span

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.style.ImageSpan

import com.sunhapper.x.spedit.mention.span.IntegratedSpan

/**
 * 和文字等高的ImageSpan
 * 在选择模式下，光标的index可能在Span对应的文字内部，标记为IntegratedSpan可以防止光标
 * 在内部，在replace其他文字时可以正常删除该span
 */
open class IsoheightImageSpan : ImageSpan, IntegratedSpan {
    protected var resized = false
    protected var drawableHeight = 0

    constructor(d: Drawable) : super(d)

    constructor(d: Drawable, verticalAlignment: Int) : super(d, verticalAlignment)

    constructor(d: Drawable, source: String) : super(d, source)

    constructor(d: Drawable, source: String, verticalAlignment: Int) : super(d, source, verticalAlignment)

    constructor(context: Context, uri: Uri) : super(context, uri)

    constructor(context: Context, uri: Uri, verticalAlignment: Int) : super(context, uri, verticalAlignment)
    constructor(context: Context, resourceId: Int) : super(context, resourceId)

    constructor(context: Context, resourceId: Int, verticalAlignment: Int) : super(context, resourceId, verticalAlignment)

    constructor(b: Bitmap) : super(b)

    constructor(b: Bitmap, verticalAlignment: Int) : super(b, verticalAlignment)

    constructor(context: Context, b: Bitmap) : super(context, b)

    constructor(context: Context, b: Bitmap, verticalAlignment: Int) : super(context, b, verticalAlignment)

    override fun getSize(paint: Paint, text: CharSequence,
                         start: Int, end: Int, fm: FontMetricsInt?): Int {
        if (fm != null && paint.fontMetricsInt != null) {
            //paint.getFontMetricsInt() 和 参数中FontMetricsInt不是同一个对象，数据不同
            // FontMetricsInt会变化，paint.getFontMetricsInt()会和TextView的文字大小保持一致
            val paintFontMetricsInt = paint.fontMetricsInt
            drawableHeight = paintFontMetricsInt.descent - paintFontMetricsInt.ascent
        }
        val drawable = getResizedDrawable()
        val bounds = drawable.bounds
        return bounds.right
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int,
                      bottom: Int, paint: Paint) {
        val s = text.toString()
        val subS = s.substring(start, end)
        if (ELLIPSIS_NORMAL[0] == subS[0] || ELLIPSIS_TWO_DOTS[0] == subS[0]) {
            canvas.save()
            canvas.drawText(subS, x, y.toFloat(), paint)
            canvas.restore()
        } else {
            val d = getResizedDrawable()
            canvas.save()
            val transY: Int
            val fm = paint.fontMetricsInt
            transY = y + fm.ascent
            canvas.translate(x, transY.toFloat())
            d.draw(canvas)
            canvas.restore()
        }
    }

    open fun getResizedDrawable(): Drawable {
        val d = drawable
        if (drawableHeight == 0) {
            return d
        }
        if (!resized) {
            resized = true
            d.bounds = Rect(0, 0,
                    (1f * drawableHeight.toFloat() * d.intrinsicWidth.toFloat() / d.intrinsicHeight).toInt(),
                    drawableHeight)
        }
        return d
    }

    companion object {
        private val ELLIPSIS_NORMAL = charArrayOf('\u2026') // this is "..."
        private val ELLIPSIS_TWO_DOTS = charArrayOf('\u2025') // this is ".."
    }

}
