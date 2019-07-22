package com.sunhapper.x.spedit.view

import android.text.*
import android.text.style.ImageSpan
import java.lang.reflect.Array
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by sunha on 2018/2/12 0012.
 */
class EmojiSpannableStringBuilder : SpannableStringBuilder {

    /**
     * DynamicLayout$ChangeWatcher class.
     */
    private val mWatcherClass: Class<*>

    /**
     * All WatcherWrappers.
     */
    private val mWatchers = ArrayList<WatcherWrapper>()


    internal constructor(watcherClass: Class<*>) {
        mWatcherClass = watcherClass
    }


    internal constructor(watcherClass: Class<*>, text: CharSequence) : super(text) {
        mWatcherClass = watcherClass
    }


    internal constructor(watcherClass: Class<*>, text: CharSequence, start: Int,
                         end: Int) : super(text, start, end) {
        mWatcherClass = watcherClass
    }

    /**
     * Checks whether the mObject is instance of the DynamicLayout$ChangeWatcher.
     *
     * @param object mObject to be checked
     * @return true if mObject is instance of the DynamicLayout$ChangeWatcher.
     */
    private fun isWatcher(obj: Any?): Boolean {
        return obj != null && isWatcher(obj.javaClass)
    }

    /**
     * Checks whether the class is DynamicLayout$ChangeWatcher.
     *
     * @param clazz class to be checked
     * @return true if class is DynamicLayout$ChangeWatcher.
     */
    private fun isWatcher(clazz: Class<*>): Boolean {
        return mWatcherClass == clazz
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        return EmojiSpannableStringBuilder(mWatcherClass, this, startIndex, endIndex)
    }

    /**
     * If the span being added is instance of DynamicLayout$ChangeWatcher, wrap the watcher in
     * another internal watcher that will prevent EmojiSpan events to be fired to DynamicLayout. Set
     * this new mObject as the span.
     */
    override fun setSpan(what: Any, start: Int, end: Int, flags: Int) {
        var whatObj = what
        if (isWatcher(whatObj)) {
            val span = WatcherWrapper(whatObj)
            mWatchers.add(span)
            whatObj = span
        }
        super.setSpan(whatObj, start, end, flags)
    }

    /**
     * If previously a DynamicLayout$ChangeWatcher was wrapped in a WatcherWrapper, return the
     * correct Object that the client has set.
     */
    override fun <T : Any?> getSpans(queryStart: Int, queryEnd: Int, kind: Class<T>?): kotlin.Array<T> {
        kind?.run {
            if (isWatcher(kind)) {
                val spans = super.getSpans(queryStart, queryEnd,
                        WatcherWrapper::class.java)
                val result = Array.newInstance(kind, spans.size) as kotlin.Array<T>
                spans.forEachIndexed { index, watcherWrapper ->
                    result[index] = watcherWrapper.mObject as T
                }
                return result
            }
        }
        return super.getSpans(queryStart, queryEnd, kind)
    }


    /**
     * If the client wants to remove the DynamicLayout$ChangeWatcher span, remove the WatcherWrapper
     * instead.
     */
    override fun removeSpan(what: Any) {
        var whatObj = what
        val watcher: WatcherWrapper?
        if (isWatcher(whatObj)) {
            watcher = getWatcherFor(whatObj)
            if (watcher != null) {
                whatObj = watcher
            }
        } else {
            watcher = null
        }

        super.removeSpan(whatObj)

        if (watcher != null) {
            mWatchers.remove(watcher)
        }
    }

    /**
     * Return the correct start for the DynamicLayout$ChangeWatcher span.
     */
    override fun getSpanStart(tag: Any): Int {
        var tagObj = tag
        if (isWatcher(tagObj)) {
            val watcher = getWatcherFor(tagObj)
            if (watcher != null) {
                tagObj = watcher
            }
        }
        return super.getSpanStart(tagObj)
    }

    /**
     * Return the correct end for the DynamicLayout$ChangeWatcher span.
     */
    override fun getSpanEnd(tag: Any): Int {
        var tagObj = tag
        if (isWatcher(tagObj)) {
            val watcher = getWatcherFor(tagObj)
            if (watcher != null) {
                tagObj = watcher
            }
        }
        return super.getSpanEnd(tagObj)
    }

    /**
     * Return the correct flags for the DynamicLayout$ChangeWatcher span.
     */
    override fun getSpanFlags(tag: Any): Int {
        var tagObj = tag
        if (isWatcher(tagObj)) {
            val watcher = getWatcherFor(tagObj)
            if (watcher != null) {
                tagObj = watcher
            }
        }
        return super.getSpanFlags(tagObj)
    }

    /**
     * Return the correct transition for the DynamicLayout$ChangeWatcher span.
     */
    override fun nextSpanTransition(start: Int, limit: Int, type: Class<*>): Int {
        var classType = type
        if (isWatcher(classType)) {
            classType = WatcherWrapper::class.java
        }
        return super.nextSpanTransition(start, limit, classType)
    }

    /**
     * Find the WatcherWrapper for a given DynamicLayout$ChangeWatcher.
     *
     * @param object DynamicLayout$ChangeWatcher mObject
     * @return WatcherWrapper that wraps the mObject.
     */
    private fun getWatcherFor(`object`: Any): WatcherWrapper? {
        for (i in mWatchers.indices) {
            val watcher = mWatchers[i]
            if (watcher.mObject === `object`) {
                return watcher
            }
        }
        return null
    }


    fun beginBatchEdit() {
        blockWatchers()
    }


    fun endBatchEdit() {
        unblockwatchers()
        fireWatchers()
    }

    /**
     * Block all watcher wrapper events.
     */
    private fun blockWatchers() {
        for (i in mWatchers.indices) {
            mWatchers[i].blockCalls()
        }
    }

    /**
     * Unblock all watcher wrapper events.
     */
    private fun unblockwatchers() {
        for (i in mWatchers.indices) {
            mWatchers[i].unblockCalls()
        }
    }

    /**
     * Unblock all watcher wrapper events. Called by editing operations, namely
     * [SpannableStringBuilder.replace].
     */
    private fun fireWatchers() {
        for (i in mWatchers.indices) {
            mWatchers[i].onTextChanged(this, 0, this.length, this.length)
        }
    }

    override fun replace(start: Int, end: Int, tb: CharSequence): SpannableStringBuilder {
        blockWatchers()
        super.replace(start, end, tb)
        unblockwatchers()
        return this
    }

    override fun replace(start: Int, end: Int, tb: CharSequence, tbstart: Int,
                         tbend: Int): SpannableStringBuilder {
        var startIndex = start
        var endIndex = end
        blockWatchers()
        //保证end>=start
        if (endIndex < startIndex) {
            val temp = startIndex
            startIndex = endIndex
            endIndex = temp
        }
        //先删除再插入，解决选择模式下span样式不正常消失的问题
        if (startIndex != endIndex && tbstart != tbend) {
            //注意：选择模式下选中ImageSpan的文字时如"[尴尬]"可能对应的光标位置为start=0 end=1，
            //而正确的预期为start=0 end=4，导致ImageSpan无法正常删除
            //解决方式为将自定义的ImageSpan标记为IntegratedSpan
            super.replace(startIndex, endIndex, "", 0, 0)
            super.insert(startIndex, tb, tbstart, tbend)
        } else {
            super.replace(startIndex, endIndex, tb, tbstart, tbend)
        }
        unblockwatchers()
        return this
    }

    override fun insert(where: Int, tb: CharSequence): SpannableStringBuilder {
        super.insert(where, tb)
        return this
    }

    override fun insert(where: Int, tb: CharSequence, start: Int, end: Int): SpannableStringBuilder {
        super.insert(where, tb, start, end)
        return this
    }

    override fun delete(start: Int, end: Int): SpannableStringBuilder {
        super.delete(start, end)
        return this
    }

    override fun append(text: CharSequence): SpannableStringBuilder {
        super.append(text)
        return this
    }

    override fun append(text: Char): SpannableStringBuilder {
        super.append(text)
        return this
    }

    override fun append(text: CharSequence, start: Int, end: Int): SpannableStringBuilder {
        super.append(text, start, end)
        return this
    }

    override fun append(text: CharSequence, what: Any, flags: Int): SpannableStringBuilder {
        super.append(text, what, flags)
        return this
    }

    /**
     * Wraps a DynamicLayout$ChangeWatcher in order to prevent firing of events to DynamicLayout.
     */
    private class WatcherWrapper internal constructor(val mObject: Any) : TextWatcher, SpanWatcher {
        private val mBlockCalls = AtomicInteger(0)

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            (mObject as TextWatcher).beforeTextChanged(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            (mObject as TextWatcher).onTextChanged(s, start, before, count)
        }

        override fun afterTextChanged(s: Editable) {
            (mObject as TextWatcher).afterTextChanged(s)
        }

        /**
         * Prevent the onSpanAdded calls to DynamicLayout$ChangeWatcher if in a replace operation
         * (mBlockCalls is set) and the span that is added is an EmojiSpan.
         */
        override fun onSpanAdded(text: Spannable, what: Any, start: Int, end: Int) {
            if (mBlockCalls.get() > 0 && isImageSpan(what)) {
                return
            }
            (mObject as SpanWatcher).onSpanAdded(text, what, start, end)
        }

        /**
         * Prevent the onSpanRemoved calls to DynamicLayout$ChangeWatcher if in a replace operation
         * (mBlockCalls is set) and the span that is added is an EmojiSpan.
         */
        override fun onSpanRemoved(text: Spannable, what: Any, start: Int, end: Int) {
            if (mBlockCalls.get() > 0 && isImageSpan(what)) {
                return
            }
            (mObject as SpanWatcher).onSpanRemoved(text, what, start, end)
        }

        /**
         * Prevent the onSpanChanged calls to DynamicLayout$ChangeWatcher if in a replace operation
         * (mBlockCalls is set) and the span that is added is an EmojiSpan.
         */
        override fun onSpanChanged(text: Spannable, what: Any, ostart: Int, oend: Int, nstart: Int,
                                   nend: Int) {
            if (mBlockCalls.get() > 0 && isImageSpan(what)) {
                return
            }
            (mObject as SpanWatcher).onSpanChanged(text, what, ostart, oend, nstart, nend)
        }

        internal fun blockCalls() {
            mBlockCalls.incrementAndGet()
        }

        internal fun unblockCalls() {
            mBlockCalls.decrementAndGet()
        }

        private fun isImageSpan(span: Any): Boolean {
            return span is ImageSpan
        }
    }

    companion object {

        internal fun create(clazz: Class<*>, text: CharSequence): EmojiSpannableStringBuilder {
            return EmojiSpannableStringBuilder(clazz, text)
        }
    }

}
