package com.sunhapper.x.spedit.view

import android.content.Context
import android.text.NoCopySpan
import android.util.AttributeSet
import com.sunhapper.x.spedit.gif.watcher.GifWatcher
import java.util.*

/**
 * Created by sunhapper on 2019/1/25 .
 */
class SpXTextView : android.support.v7.widget.AppCompatTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val watchers = ArrayList<NoCopySpan>()
        watchers.add(GifWatcher(this))
        setSpannableFactory(SpXSpannableFactory(watchers))
    }

    override fun setText(text: CharSequence, type: BufferType) {
        super.setText(text, BufferType.SPANNABLE)
    }

}
