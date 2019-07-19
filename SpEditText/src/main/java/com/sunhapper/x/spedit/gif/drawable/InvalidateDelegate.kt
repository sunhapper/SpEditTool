package com.sunhapper.x.spedit.gif.drawable

import android.graphics.drawable.Drawable
import android.util.Log
import com.sunhapper.x.spedit.gif.listener.RefreshListener

/**
 * Created by sunhapper on 2019-07-19 .
 */
class InvalidateDelegate : InvalidateDrawable, Drawable.Callback {
    companion object {
        private val TAG = "InvalidateDelegate"
    }

    override var mRefreshListeners: MutableCollection<RefreshListener> = mutableListOf()

    override fun addRefreshListener(callback: RefreshListener) {
        Log.i(TAG, "addRefreshListener: $callback")
        mRefreshListeners.add(callback)
    }

    override fun removeRefreshListener(callback: RefreshListener) {
        mRefreshListeners.remove(callback)
    }

    override fun refresh() {
        Log.i(TAG, "refresh: ")
        mRefreshListeners = mRefreshListeners.filter {
            it.onRefresh()
        }.toMutableList()
    }


    override fun unscheduleDrawable(who: Drawable, what: Runnable) {

    }

    override fun invalidateDrawable(who: Drawable) {
        refresh()
    }

    override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {

    }

}