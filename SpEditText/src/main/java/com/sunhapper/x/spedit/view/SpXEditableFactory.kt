package com.sunhapper.x.spedit.view

import android.annotation.SuppressLint
import android.text.Editable
import android.text.Editable.Factory
import android.text.NoCopySpan
import android.text.SpannableStringBuilder
import android.text.Spanned

/**
 * Created by sunhapper on 2019/1/25 .
 */
class SpXEditableFactory @SuppressLint("PrivateApi")
constructor(private val mNoCopySpans: List<NoCopySpan>) : Factory() {
    companion object {
        private var sWatcherClass: Class<*>? = null
    }

    init {
        try {
            val className = "android.text.DynamicLayout\$ChangeWatcher"
            sWatcherClass = this.javaClass.classLoader.loadClass(className)
        } catch (var2: Throwable) {
            var2.printStackTrace()
        }

    }

    override fun newEditable(source: CharSequence): Editable {
        val spannableStringBuilder = if (sWatcherClass != null)
            EmojiSpannableStringBuilder.create(
                    sWatcherClass!!, source)
        else
            SpannableStringBuilder.valueOf(source)
        for (span in mNoCopySpans) {
            spannableStringBuilder.setSpan(span, 0, source.length,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE or Spanned.SPAN_PRIORITY)
        }
        return spannableStringBuilder
    }


}
