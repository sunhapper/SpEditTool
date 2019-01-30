package com.sunhapper.x.spedit.view;

import android.text.Selection;
import android.text.Spannable;
import android.view.KeyEvent;

import com.sunhapper.x.spedit.mention.span.IntegratedSpan;

/**
 * Created by sunhapper on 2019/1/25 .
 */
public class DefaultDeleteKeyEventProxy implements KeyEventProxy {
    @Override
    public boolean onKeyEvent(KeyEvent keyEvent, Spannable text) {
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            int selectionStart = Selection.getSelectionStart(text);
            int selectionEnd = Selection.getSelectionEnd(text);
            IntegratedSpan[] integratedSpans = text.getSpans(selectionStart, selectionEnd, IntegratedSpan.class);
            if (integratedSpans != null && integratedSpans.length > 0) {
                IntegratedSpan span = integratedSpans[0];
                int spanStart = text.getSpanStart(span);
                int spanEnd = text.getSpanEnd(span);
                if (spanEnd == selectionStart) {
                    Selection.setSelection(text, spanStart, spanEnd);
                    if (needSelectionFlag()) {
                        return selectionEnd == selectionStart;
                    }
                }
            }
        }
        return false;
    }

    public boolean needSelectionFlag() {
        return true;
    }
}
