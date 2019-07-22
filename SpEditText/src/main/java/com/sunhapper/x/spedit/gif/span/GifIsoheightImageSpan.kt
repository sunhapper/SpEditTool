package com.sunhapper.x.spedit.gif.span

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri

import com.sunhapper.x.spedit.gif.drawable.InvalidateDrawable

/**
 * Created by sunhapper on 2019/1/28 .
 */
open class GifIsoheightImageSpan : IsoheightImageSpan, RefreshSpan {
    constructor(d: Drawable) : super(d)

    constructor(d: Drawable, verticalAlignment: Int) : super(d, verticalAlignment) {}

    constructor(d: Drawable, source: String) : super(d, source)

    constructor(d: Drawable,
                source: String, verticalAlignment: Int) : super(d, source, verticalAlignment)

    constructor(context: Context, uri: Uri) : super(context, uri)

    constructor(context: Context, uri: Uri, verticalAlignment: Int) : super(context, uri, verticalAlignment)

    constructor(context: Context, resourceId: Int) : super(context, resourceId)

    constructor(context: Context, resourceId: Int,
                verticalAlignment: Int) : super(context, resourceId, verticalAlignment)

    constructor(b: Bitmap) : super(b)

    constructor(b: Bitmap, verticalAlignment: Int) : super(b, verticalAlignment)

    constructor(context: Context,
                b: Bitmap) : super(context, b)

    constructor(context: Context,
                b: Bitmap, verticalAlignment: Int) : super(context, b, verticalAlignment)

    override fun getInvalidateDrawable(): InvalidateDrawable? {
        val d = getResizedDrawable()
        return if (d is InvalidateDrawable) {
            d
        } else {
            null
        }
    }
}
