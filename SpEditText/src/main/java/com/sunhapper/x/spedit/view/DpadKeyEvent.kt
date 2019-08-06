package com.sunhapper.x.spedit.view

import android.text.Editable
import android.text.Selection
import android.view.KeyEvent
import com.sunhapper.x.spedit.mention.span.IntegratedSpan

/**
 * Created by sunhapper on 2019-07-19 .
 */
class DpadKeyEvent : KeyEventProxy {
    override fun onKeyEvent(keyEvent: KeyEvent, text: Editable): Boolean {
        //处理光标左移事件
        if (keyEvent.keyCode == KeyEvent.KEYCODE_DPAD_LEFT && keyEvent.action == KeyEvent.ACTION_DOWN) {

            val selectionStart = Selection.getSelectionStart(text)
            val selectionEnd = Selection.getSelectionEnd(text)
            val integratedSpans = text.getSpans(selectionEnd, selectionEnd, IntegratedSpan::class.java)
            if (integratedSpans != null && integratedSpans.isNotEmpty()) {
                for (span in integratedSpans) {
                    val spanStart = text.getSpanStart(span)
                    val spanEnd = text.getSpanEnd(span)
                    //selectionEnd表示主动移动的光标
                    if (spanEnd == selectionEnd) {
                        Selection.setSelection(text, selectionStart, spanStart)
                        return true
                    }
                }
            }
        }
        //处理光标右移事件
        if (keyEvent.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && keyEvent.action == KeyEvent.ACTION_DOWN) {
            val selectionStart = Selection.getSelectionStart(text)
            val selectionEnd = Selection.getSelectionEnd(text)
            val integratedSpans = text.getSpans(selectionEnd, selectionEnd, IntegratedSpan::class.java)
            if (integratedSpans != null && integratedSpans.isNotEmpty()) {
                for (span in integratedSpans) {
                    val spanStart = text.getSpanStart(span)
                    val spanEnd = text.getSpanEnd(span)
                    if (spanStart == selectionEnd) {
                        Selection.setSelection(text, selectionStart, spanEnd)
                        return true
                    }
                }
            }
        }
        return false
    }

}