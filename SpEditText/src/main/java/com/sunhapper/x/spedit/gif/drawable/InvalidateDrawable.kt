package com.sunhapper.x.spedit.gif.drawable

import com.sunhapper.x.spedit.gif.listener.RefreshListener

/**
 * Created by sunhapper on 2019/1/25 .
 */
interface InvalidateDrawable {
    fun addRefreshListener(callback: RefreshListener)

    fun removeRefreshListener(callback: RefreshListener)
}
