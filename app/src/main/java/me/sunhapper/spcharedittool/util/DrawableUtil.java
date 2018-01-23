package me.sunhapper.spcharedittool.util;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import me.sunhapper.spcharedittool.span.GifAlignCenterSpan;

/**
 * Created by sunha on 2018/1/23 0023.
 */

public class DrawableUtil {

  public static Spannable getDrawableText(CharSequence text, Drawable gifDrawable) {
    Spannable spannable = new SpannableString(text);
    ImageSpan imageSpan = new GifAlignCenterSpan(gifDrawable);
    spannable.setSpan(imageSpan,
        0, text.length(),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    return spannable;
  }
}
