package com.sunhapper.gifdrawable.drawable

import android.content.ContentResolver
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import com.sunhapper.x.spedit.gif.drawable.InvalidateDrawable
import com.sunhapper.x.spedit.gif.listener.RefreshListener
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifOptions
import pl.droidsonroids.gif.InputSource
import java.io.File
import java.io.FileDescriptor
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.util.concurrent.ScheduledThreadPoolExecutor

/**
 * Created by sunha on 2018/1/23 0023.
 */
@Deprecated(message = "use ProxyDrawable to load GifDrawable")
class TextGifDrawable : GifDrawable, InvalidateDrawable {


    override var mRefreshListeners: MutableCollection<RefreshListener> = mutableListOf()

    @Throws(Resources.NotFoundException::class, IOException::class)
    constructor(res: Resources, id: Int) : super(res, id)

    @Throws(IOException::class)
    constructor(assets: AssetManager, assetName: String) : super(assets, assetName)

    @Throws(IOException::class)
    constructor(filePath: String) : super(filePath)

    @Throws(IOException::class)
    constructor(file: File) : super(file)

    @Throws(IOException::class)
    constructor(stream: InputStream) : super(stream)

    @Throws(IOException::class)
    constructor(afd: AssetFileDescriptor) : super(afd)

    @Throws(IOException::class)
    constructor(fd: FileDescriptor) : super(fd)

    @Throws(IOException::class)
    constructor(bytes: ByteArray) : super(bytes)

    @Throws(IOException::class)
    constructor(buffer: ByteBuffer) : super(buffer)

    @Throws(IOException::class)
    constructor(resolver: ContentResolver?,
                uri: Uri) : super(resolver, uri)

    @Throws(IOException::class)
    protected constructor(inputSource: InputSource,
                          oldDrawable: GifDrawable?,
                          executor: ScheduledThreadPoolExecutor?,
                          isRenderingTriggeredOnDraw: Boolean, options: GifOptions) :
            super(inputSource, oldDrawable, executor, isRenderingTriggeredOnDraw, options)

    init {
        callback = CallBack()
    }


    override fun addRefreshListener(refreshListener: RefreshListener) {
        mRefreshListeners.add(refreshListener)
    }

    override fun removeRefreshListener(refreshListener: RefreshListener) {
        mRefreshListeners.remove(refreshListener)
    }


    private inner class CallBack : Callback {

        override fun invalidateDrawable(who: Drawable) {
            refresh()
        }

        override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {

        }

        override fun unscheduleDrawable(who: Drawable, what: Runnable) {

        }
    }

    override fun refresh() {
        mRefreshListeners = mRefreshListeners.filter {
            it.onRefresh()
        }.toMutableList()
    }
}