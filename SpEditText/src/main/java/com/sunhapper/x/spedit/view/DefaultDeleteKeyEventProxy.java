package com.sunhapper.x.spedit.view;

import android.text.Selection;
import android.text.Spannable;
import android.view.KeyEvent;

import com.sunhapper.x.spedit.mention.span.MarkSpan;

/**
 * Created by sunhapper on 2019/1/25 .
 */
public class DefaultDeleteKeyEventProxy implements KeyEventProxy {
    @Override
    public boolean onKeyEvent(KeyEvent keyEvent, Spannable text) {
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            int selectionStart = Selection.getSelectionStart(text);
            int selectionEnd = Selection.getSelectionEnd(text);
            MarkSpan[] markSpans = text.getSpans(selectionStart, selectionEnd, MarkSpan.class);
            if (markSpans != null && markSpans.length > 0) {
                MarkSpan span = markSpans[0];
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
