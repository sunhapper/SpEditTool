package com.sunhapper.x.spedit.view

import android.text.Editable
import android.text.Selection
import android.text.Spanned
import android.view.KeyEvent
import com.sunhapper.x.spedit.mention.span.IntegratedBgSpan
import com.sunhapper.x.spedit.mention.span.IntegratedSpan

/**
 * Created by sunhapper on 2019/1/25 .
 */
class DefaultKeyEventProxy : KeyEventProxy {
    private val dpadKeyEvent = DpadKeyEvent()
    override fun onKeyEvent(keyEvent: KeyEvent, text: Editable): Boolean {
        if (dpadKeyEvent.onKeyEvent(keyEvent, text)) {
            return true
        }
        if (keyEvent.keyCode == KeyEvent.KEYCODE_DEL && keyEvent.action == KeyEvent.ACTION_DOWN) {
            val selectionStart = Selection.getSelectionStart(text)
            val selectionEnd = Selection.getSelectionEnd(text)
            if (selectionEnd != selectionStart) {
                return false
            }
            val integratedSpans = text.getSpans(selectionStart, selectionEnd, IntegratedSpan::class.java)
            integratedSpans?.firstOrNull { text.getSpanEnd(it) == selectionStart }
                    ?.apply {
                        val spanStart = text.getSpanStart(this)
                        val spanEnd = text.getSpanEnd(this)
                        if (this is IntegratedBgSpan) {
                            if (isShow) {
                                text.replace(spanStart, spanEnd, "")
                            } else {
                                isShow = true
                                text.setSpan(createStoredBgSpan(), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            }
                        } else {
                            text.replace(spanStart, spanEnd, "")
                        }
                        return true
                    }
        }
        return false
    }
}
