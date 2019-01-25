package com.sunhapper.x.spedit.mention.watcher;

import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;

import com.sunhapper.x.spedit.mention.span.BreakableSpan;
import com.sunhapper.x.spedit.mention.span.IntegratedSpan;

/**
 * Created by sunhapper on 2019/1/24 .
 */
public class SpanChangedWatcher implements SpanWatcher {
    @Override
    public void onSpanAdded(Spannable text, Object what, int start, int end) {

    }

    @Override
    public void onSpanRemoved(Spannable text, Object what, int start, int end) {

    }

    @Override
    public void onSpanChanged(Spannable text, Object what, int ostart, int oend, int nstart, int nend) {
        if (what == Selection.SELECTION_END && oend != nstart) {
            IntegratedSpan[] spans = text.getSpans(nstart, nend, IntegratedSpan.class);
            if (spans != null && spans.length > 0) {
                IntegratedSpan integratedSpan = spans[0];
                int spanStart = text.getSpanStart(integratedSpan);
                int spanEnd = text.getSpanEnd(integratedSpan);
                int index = (Math.abs(nstart - spanEnd) > Math.abs(nstart - spanStart)) ? spanStart : spanEnd;
                Selection.setSelection(text, Selection.getSelectionStart(text), index);
            }
        }
        if (what == Selection.SELECTION_START && oend != nstart) {
            IntegratedSpan[] spans = text.getSpans(nstart, nend, IntegratedSpan.class);
            if (spans != null && spans.length > 0) {
                IntegratedSpan integratedSpan = spans[0];
                int spanStart = text.getSpanStart(integratedSpan);
                int spanEnd = text.getSpanEnd(integratedSpan);
                int index = (Math.abs(nstart - spanEnd) > Math.abs(nstart - spanStart)) ? spanStart : spanEnd;
                Selection.setSelection(text, index, Selection.getSelectionEnd(text));
            }
        }
        if (what instanceof BreakableSpan && ((BreakableSpan) what).isBreak()) {
            text.removeSpan(what);
        }
    }
}
