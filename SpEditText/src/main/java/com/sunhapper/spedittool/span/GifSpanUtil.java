package com.sunhapper.spedittool.span;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.sunhapper.spedittool.R;

/**
 * Created by sunhapper on 2018/1/21.
 */

public class GifSpanUtil {

  public static void setText(final TextView textView, final CharSequence text) {
    setText(textView, text, BufferType.NORMAL);
  }

  public static void setText(final TextView textView, final CharSequence nText,
      final BufferType type) {
    textView.setText(nText, type);
    CharSequence text = textView.getText();
    if (text instanceof Spannable) {
      ImageSpan[] gifSpans = ((Spannable) text).getSpans(0, text.length(), ImageSpan.class);
      Object oldCallback = textView
          .getTag(R.id.drawable_callback_tag);
      if (oldCallback != null && oldCallback instanceof CallbackForTextView) {
        ((CallbackForTextView) oldCallback).disable();
      }
      Callback callback = new CallbackForTextView(textView);
      //让textview持有callback,防止callback被回收
      textView.setTag(R.id.drawable_callback_tag, callback);
      for (ImageSpan gifSpan : gifSpans) {
        Drawable drawable = gifSpan.getDrawable();
        if (drawable != null) {
          drawable.setCallback(callback);
        }
      }
      ((Spannable) text).setSpan(gifSpanWatcher, 0, text.length(),
          Spanned.SPAN_INCLUSIVE_INCLUSIVE | Spanned.SPAN_PRIORITY);

    }

  }


  public static class CallbackForTextView implements Callback {

    private boolean enable = true;
    private long lastInvalidateTime;
    private TextView textView;

    public CallbackForTextView(TextView textView) {
      this.textView = textView;
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
      if (!enable) {
        return;
      }
      if (System.currentTimeMillis() - lastInvalidateTime > 40) {
        lastInvalidateTime = System.currentTimeMillis();
        textView.invalidate();
      }
    }

    @Override
    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {

    }

    @Override
    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {

    }

    public void disable() {
      this.enable = false;
    }
  }

  private static GifSpanWatcher gifSpanWatcher = new GifSpanWatcher();
}
