package com.sunhapper.x.spedit.view;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Editable.Factory;
import android.text.NoCopySpan;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import java.util.List;

/**
 * Created by sunhapper on 2019/1/25 .
 */
public class SpXFactory extends Factory {
    private List<NoCopySpan> mNoCopySpans;

    public SpXFactory(List<NoCopySpan> watchers) {
        mNoCopySpans = watchers;
    }

    public Editable newEditable(@NonNull CharSequence source) {
        SpannableStringBuilder spannableStringBuilder = SpannableStringBuilder.valueOf(source);
        for (NoCopySpan span : mNoCopySpans) {
            spannableStringBuilder.setSpan(span, 0, source.length(),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE | Spanned.SPAN_PRIORITY);
        }
        return spannableStringBuilder;
    }
}
