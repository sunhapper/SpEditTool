package com.sunhapper.spedittool.util;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.TextView;
import com.sunhapper.spedittool.span.GifAlignCenterSpan;
import com.sunhapper.spedittool.span.GifSpan;
import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by sunhapper on 2018/1/21.
 */

public class TextGifUtil {

  private static final String TAG = "TextGifUtil";
  public static void setText(final TextView textView, final CharSequence text) {
//   CharSequence oldCharSequence= textView.getText();
//    if (oldCharSequence instanceof Editable) {
//
//      GifAlignCenterSpan[] gifAlignCenterSpans = ((Editable) oldCharSequence)
//          .getSpans(0, oldCharSequence.length(), GifAlignCenterSpan.class);
//
//
//    }
    if (text instanceof Spanned) {
      GifSpan[] gifSpans = ((Spanned) text).getSpans(0, text.length(), GifSpan.class);
      for (GifSpan gifSpan : gifSpans) {
        final Drawable drawable = gifSpan.getGifDrawable();
        if (drawable != null) {
          drawable.setCallback(new Callback() {
            @Override
            public void invalidateDrawable(@NonNull Drawable who) {
              Log.i(TAG, "invalidateDrawable: ");
              GifSpan imageSpan = getGifSpanByDrawable(textView, drawable);
              if (imageSpan != null) {
                CharSequence text = textView.getText();
                if (!TextUtils.isEmpty(text)) {
                  if (text instanceof Editable) {
                    Editable editable = (Editable) text;
                    int start = editable.getSpanStart(imageSpan);
                    int end = editable.getSpanEnd(imageSpan);
                    int flags = editable.getSpanFlags(imageSpan);

                    editable.removeSpan(imageSpan);
                    editable.setSpan(imageSpan, start, end, flags);
                  }
                }

              }
            }

            @Override
            public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {

            }

            @Override
            public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {

            }
          });
        }
      }
      textView.setText(text);
    }
  }

  public static GifSpan getGifSpanByDrawable(TextView textView, Drawable drawable) {
    GifSpan gifSpan = null;
    CharSequence text = textView.getText();
    if (!TextUtils.isEmpty(text)) {
      if (text instanceof Spanned) {
        Spanned spanned = (Spanned) text;
        GifSpan[] spans = spanned.getSpans(0, text.length(), GifSpan.class);
        if (spans != null && spans.length > 0) {
          for (GifSpan span : spans) {
            if (drawable == span.getGifDrawable()) {
              gifSpan = span;
              break;
            }
          }
        }
      }
    }

    return gifSpan;

  }


  public static Spannable getGifText(CharSequence text, GifDrawable gifDrawable) {
    Spannable spannable = new SpannableString(text);
    ImageSpan imageSpan = new GifAlignCenterSpan(gifDrawable);
    spannable.setSpan(imageSpan,
        0, text.length(),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    return spannable;
  }

}
