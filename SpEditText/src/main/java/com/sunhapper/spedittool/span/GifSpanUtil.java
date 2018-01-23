package com.sunhapper.spedittool.span;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.sunhapper.spedittool.span.GifSpan.GifSpanWatcher;

/**
 * Created by sunhapper on 2018/1/21.
 */

public class GifSpanUtil {

  private static final String TAG = "TextGifUtil";

  public static void setText(final TextView textView, final CharSequence text) {
    setText(textView, text, BufferType.NORMAL);
  }

  public static void setText(final TextView textView, final CharSequence text,
      final BufferType type) {
    CharSequence oldCharSequence = "";
    try {
      oldCharSequence = textView.getText();
    } catch (ClassCastException e) {
      e.printStackTrace();
    }

    if (oldCharSequence instanceof Spannable) {

      GifSpan[] gifSpans = ((Spannable) oldCharSequence)
          .getSpans(0, oldCharSequence.length(), GifSpan.class);
      for (GifSpan span : gifSpans) {
        ((Spannable) oldCharSequence).removeSpan(span);
      }
      GifSpanWatcher[] watchers = ((Spannable) text)
          .getSpans(0, text.length(), GifSpanWatcher.class);
      for (GifSpanWatcher watcher : watchers) {
        ((Spannable) oldCharSequence).removeSpan(watcher);
      }
    }
    if (text instanceof Spannable) {
      GifSpan[] gifSpans = ((Spannable) text).getSpans(0, text.length(), GifSpan.class);
      for (GifSpan gifSpan : gifSpans) {
        final Drawable drawable = gifSpan.getGifDrawable();
        if (drawable != null) {
          drawable.setCallback(new Callback() {
            @Override
            public void invalidateDrawable(@NonNull Drawable who) {
              GifSpan imageSpan = getGifSpanByDrawable(textView, drawable);
              if (imageSpan != null) {
                CharSequence text = textView.getText();
                if (!TextUtils.isEmpty(text)) {
                  if (text instanceof Spannable) {
                    Spannable spannable = (Spannable) text;
                    int start = spannable.getSpanStart(imageSpan);
                    int end = spannable.getSpanEnd(imageSpan);
                    int flags = spannable.getSpanFlags(imageSpan);
                    imageSpan.isChanging = true;
                    spannable.removeSpan(imageSpan);
                    spannable.setSpan(imageSpan, start, end, flags);
                    imageSpan.isChanging = false;
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
    }
    textView.setText(text, type);
    CharSequence charSequence = textView.getText();
    if (charSequence instanceof Spannable) {
      Spannable spannableAfter = (Spannable) charSequence;
      spannableAfter.setSpan(GifSpan.getGifSpanWatcher(), 0, text.length(),
          Spanned.SPAN_INCLUSIVE_INCLUSIVE | Spanned.SPAN_PRIORITY);

    }
  }

  private static GifSpan getGifSpanByDrawable(TextView textView, Drawable drawable) {
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


}
