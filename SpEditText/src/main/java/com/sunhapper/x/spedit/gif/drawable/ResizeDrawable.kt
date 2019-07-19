package com.sunhapper.x.spedit.gif.drawable

/**
 * Created by sunhapper on 2019/1/27 .
 */
interface ResizeDrawable {
    val width: Int

    val height: Int

    fun needResize(): Boolean
}
