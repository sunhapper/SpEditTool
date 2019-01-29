package com.sunhapper.x.spedit.view;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    @Nullable
    private static Class<?> sWatcherClass;


    @SuppressLint("PrivateApi")
    public SpXFactory(List<NoCopySpan> watchers) {
        mNoCopySpans = watchers;
        try {
            String className = "android.text.DynamicLayout$ChangeWatcher";
            sWatcherClass = this.getClass().getClassLoader().loadClass(className);
        } catch (Throwable var2) {
            ;
        }
    }

    public Editable newEditable(@NonNull CharSequence source) {
        SpannableStringBuilder spannableStringBuilder = sWatcherClass != null ? EmojiSpannableStringBuilder.create(
                sWatcherClass, source)
                : SpannableStringBuilder.valueOf(source);
        for (NoCopySpan span : mNoCopySpans) {
            spannableStringBuilder.setSpan(span, 0, source.length(),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE | Spanned.SPAN_PRIORITY);
        }
        return spannableStringBuilder;
    }
}
