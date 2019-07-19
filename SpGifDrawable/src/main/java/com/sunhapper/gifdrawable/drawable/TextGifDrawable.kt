package com.sunhapper.gifdrawable.drawable

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.sunhapper.x.spedit.gif.drawable.InvalidateDelegate
import com.sunhapper.x.spedit.gif.drawable.InvalidateDrawable
import pl.droidsonroids.gif.GifDrawable
import java.io.File

/**
 * Created by sunha on 2018/1/23 0023.
 */

//@JvmOverloads 为函数默认值重载多个方法供java调用 必须加constructor
class TextGifDrawable
@JvmOverloads constructor(private val drawable: Drawable,
                          private val invalidateDelegate: InvalidateDelegate = InvalidateDelegate())
    : Drawable(), InvalidateDrawable by invalidateDelegate, Drawable.Callback by invalidateDelegate {
    init {
        drawable.callback = invalidateDelegate
    }

    override fun draw(canvas: Canvas) {
        drawable.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {
        drawable.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        drawable.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return drawable.opacity
    }

    override fun setBounds(bounds: Rect) {
        super.setBounds(bounds)
        drawable.bounds = bounds
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        drawable.setBounds(left, top, right, bottom)
    }


}

fun TextGifDrawable.create(file: File): TextGifDrawable = TextGifDrawable(GifDrawable(file))
fun TextGifDrawable.create(res: Resources, id: Int): TextGifDrawable = TextGifDrawable(GifDrawable(res, id))