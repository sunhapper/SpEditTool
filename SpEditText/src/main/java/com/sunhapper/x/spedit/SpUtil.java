package com.sunhapper.x.spedit;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.sunhapper.x.spedit.gif.span.GifIsoheightImageSpan;
import com.sunhapper.x.spedit.gif.span.ResizeIsoheightImageSpan;

/**
 * Created by sunhapper on 2019/1/29 .
 */
public final class SpUtil {
    public static Spannable createResizeGifDrawableSpan(Drawable gifDrawable, CharSequence text) {
        Spannable spannable = new SpannableString(text);
        ImageSpan imageSpan = new ResizeIsoheightImageSpan(gifDrawable);
        spannable.setSpan(imageSpan,
                0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static Spannable createGifDrawableSpan(Drawable gifDrawable, CharSequence text) {
        ImageSpan imageSpan = new GifIsoheightImageSpan(gifDrawable);
        Spannable spannable = new SpannableString(text);
        spannable.setSpan(imageSpan,
                0, spannable.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static void insertSpannableString(Editable editable, CharSequence text) {
        editable.replace(Selection.getSelectionStart(editable), Selection.getSelectionEnd(editable), text);
    }
}
