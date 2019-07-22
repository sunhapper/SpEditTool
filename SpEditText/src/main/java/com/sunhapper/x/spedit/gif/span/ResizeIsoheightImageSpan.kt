package com.sunhapper.x.spedit.gif.span

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.Uri

import com.sunhapper.x.spedit.gif.drawable.ResizeDrawable


class ResizeIsoheightImageSpan : GifIsoheightImageSpan, RefreshSpan {


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

    override fun getResizedDrawable(): Drawable {
        val d = drawable
        if (drawableHeight == 0) {
            return d
        }
        if (d is ResizeDrawable && (d.needResize || !resized)) {
            resizeSpan(d)
        } else if (!resized) {
            resizeSpan(d)
        }
        return d
    }

    private fun resizeSpan(d: Drawable) {
        resized = true
        d.bounds = Rect(0, 0,
                (1f * drawableHeight * d.intrinsicWidth / d.intrinsicHeight).toInt(),
                drawableHeight)
    }

}
