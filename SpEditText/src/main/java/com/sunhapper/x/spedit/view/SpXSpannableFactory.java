package com.sunhapper.x.spedit.view;

import android.support.annotation.NonNull;
import android.text.NoCopySpan;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import java.util.List;

/**
 * Created by sunhapper on 2019/1/25 .
 */
public class SpXSpannableFactory extends Spannable.Factory {
    private List<NoCopySpan> mNoCopySpans;
    public SpXSpannableFactory(List<NoCopySpan> watchers) {
        mNoCopySpans = watchers;
    }

    public Spannable newSpannable(@NonNull CharSequence source) {
        SpannableStringBuilder spannableStringBuilder = new  SpannableStringBuilder();
        for (NoCopySpan span : mNoCopySpans) {
            spannableStringBuilder.setSpan(span, 0,0,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE | Spanned.SPAN_PRIORITY);
        }
        spannableStringBuilder.append(source);
        return spannableStringBuilder;
    }
}
