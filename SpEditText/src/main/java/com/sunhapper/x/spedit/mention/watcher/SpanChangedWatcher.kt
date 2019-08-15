package com.sunhapper.x.spedit.mention.watcher

import android.text.Selection
import android.text.SpanWatcher
import android.text.Spannable
import com.sunhapper.x.spedit.mention.span.BreakableSpan
import com.sunhapper.x.spedit.mention.span.IntegratedBgSpan
import com.sunhapper.x.spedit.mention.span.IntegratedSpan
import kotlin.math.abs

/**
 * Created by sunhapper on 2019/1/24 .
 */
class SpanChangedWatcher : SpanWatcher {
    override fun onSpanAdded(text: Spannable, what: Any, start: Int, end: Int) {

    }

    override fun onSpanRemoved(text: Spannable, what: Any, start: Int, end: Int) {

    }

    override fun onSpanChanged(text: Spannable, what: Any?, ostart: Int, oend: Int, nstart: Int, nend: Int) {
        if (what === Selection.SELECTION_END && oend != nstart) {
            val spans = text.getSpans(nstart, nend, IntegratedSpan::class.java)
            if (spans != null && spans.isNotEmpty()) {
                val integratedSpan = spans[0]
                val spanStart = text.getSpanStart(integratedSpan)
                val spanEnd = text.getSpanEnd(integratedSpan)
                val index = if (abs(nstart - spanEnd) > abs(nstart - spanStart)) spanStart else spanEnd
                Selection.setSelection(text, Selection.getSelectionStart(text), index)
            }
        }
        if (what === Selection.SELECTION_START && oend != nstart) {
            val spans = text.getSpans(nstart, nend, IntegratedSpan::class.java)
            if (spans != null && spans.isNotEmpty()) {
                val integratedSpan = spans[0]
                val spanStart = text.getSpanStart(integratedSpan)
                val spanEnd = text.getSpanEnd(integratedSpan)
                val index = if (abs(nstart - spanEnd) > abs(nstart - spanStart)) spanStart else spanEnd
                Selection.setSelection(text, index, Selection.getSelectionEnd(text))
            }
        }
        if (what is BreakableSpan && what.isBreak(text)) {
            text.removeSpan(what)
        }
        if (what === Selection.SELECTION_START && oend != nstart && nstart == nend) {
            val spans = text.getSpans(0, text.length, IntegratedBgSpan::class.java)
            for (span in spans) {
                span.removeBg(text)
            }
        }
    }
}
