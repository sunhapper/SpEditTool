package com.sunhapper.glide.drawable

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.sunhapper.x.spedit.gif.drawable.ProxyDrawable

/**
 * Created by sunhapper on 2019-07-11 .
 */
class DrawableTarget(private val mProxyDrawable: ProxyDrawable) : Target<Drawable> {
    private var request: Request? = null

    override fun onResourceReady(resource: Drawable,
                                 transition: Transition<in Drawable>?) {
        mProxyDrawable.drawable = resource
        if (resource is GifDrawable) {
            resource.setLoopCount(GifDrawable.LOOP_FOREVER)
            resource.start()
        }
        mProxyDrawable.invalidateSelf()
    }


    override fun onLoadCleared(placeholder: Drawable?) {
        if (mProxyDrawable.drawable != null) {
            return
        }
        mProxyDrawable.drawable = placeholder
        if (placeholder is GifDrawable) {
            placeholder.setLoopCount(GifDrawable.LOOP_FOREVER)
            placeholder.start()
        }
        mProxyDrawable.invalidateSelf()
    }

    override fun onLoadStarted(placeholder: Drawable?) {
        mProxyDrawable.drawable = placeholder
        if (placeholder is GifDrawable) {
            placeholder.setLoopCount(GifDrawable.LOOP_FOREVER)
            placeholder.start()
        }
        mProxyDrawable.invalidateSelf()

    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        mProxyDrawable.drawable = errorDrawable
        if (errorDrawable is GifDrawable) {
            errorDrawable.setLoopCount(GifDrawable.LOOP_FOREVER)
            errorDrawable.start()
        }
        mProxyDrawable.invalidateSelf()
    }

    override fun setRequest(request: Request?) {
        this.request = request
    }

    override fun getRequest(): Request? {
        return request
    }

    override fun getSize(cb: SizeReadyCallback) {
        cb.onSizeReady(Int.MIN_VALUE, Int.MIN_VALUE)
    }

    override fun onStop() {
    }

    override fun removeCallback(cb: SizeReadyCallback) {
    }

    override fun onStart() {
    }

    override fun onDestroy() {
    }

}