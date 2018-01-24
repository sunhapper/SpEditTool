package com.sunhapper.spedittool.span;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.sunhapper.spedittool.R;

/**
 * Created by sunhapper on 2018/1/21.
 */

public class GifSpanUtil {

  private static final String TAG = "TextGifUtil";

  public static void setText(final TextView textView, final CharSequence text) {
    setText(textView, text, BufferType.NORMAL);
  }

  public static void setText(final TextView textView, final CharSequence nText,
      final BufferType type) {
//    CharSequence oldCharSequence = "";
//    try {
//      oldCharSequence = textView.getText();
//    } catch (ClassCastException e) {
//      e.printStackTrace();
//    }
//    if (oldCharSequence instanceof Editable) {
//      ((Editable) oldCharSequence).clearSpans();
////      GifSpan[] gifSpans = ((Spannable) oldCharSequence)
////          .getSpans(0, oldCharSequence.length(), GifSpan.class);
////      for (GifSpan span : gifSpans) {
////        ((Spannable) oldCharSequence).removeSpan(span);
////      }
////      GifSpanWatcher[] watchers = ((Spannable) text)
////          .getSpans(0, text.length(), GifSpanWatcher.class);
////      for (GifSpanWatcher watcher : watchers) {
////        ((Spannable) oldCharSequence).removeSpan(watcher);
////      }
//    }

    textView.setText(nText, type);
    CharSequence text=textView.getText();
    if (text instanceof Spannable) {
      GifSpan[] gifSpans = ((Spannable) text).getSpans(0, text.length(), GifSpan.class);
      Object oldCallback = textView
          .getTag(R.id.drawable_callback_tag);
      if (oldCallback != null && oldCallback instanceof CallbackForTextView) {
        ((CallbackForTextView) oldCallback).disable();
      }
      Callback callback = new CallbackForTextView(textView);
      //让textview持有callback,防止callback被回收
      textView.setTag(R.id.drawable_callback_tag, callback);
      for (GifSpan gifSpan : gifSpans) {
        Drawable drawable = gifSpan.getGifDrawable();
        if (drawable != null) {
          drawable.setCallback(callback);
        }
      }
      ((Spannable) text).setSpan(GifSpan.getGifSpanWatcher(), 0, text.length(),
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


  public static class CallbackForTextView implements Callback {

    private boolean enable = true;
    private long lastInvalidateTime;
    private TextView textView;
//    private Set<Drawable> drawableTemps = new HashSet<>();

    public CallbackForTextView(TextView textView) {
      this.textView = textView;
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
//      drawableTemps.add(who);
      if (!enable) {
        return;
      }
      if (System.currentTimeMillis()-lastInvalidateTime>40){
        lastInvalidateTime=System.currentTimeMillis();
        textView.invalidate();
      }

//      GifSpan imageSpan = getGifSpanByDrawable(textView, who);
//      if (imageSpan != null) {
//        Rect rect = who.getBounds();
//        who.setBounds(rect);
//      }
//      if (imageSpan != null) {
//        CharSequence text = textView.getText();
//        Log.i(TAG, "invalidateDrawable: 0");
//        if (!TextUtils.isEmpty(text)) {
//          if (text instanceof Spannable) {
//            Spannable spannable = (Spannable) text;
//            Log.i(TAG, "invalidateDrawable: 1");
//            int start = spannable.getSpanStart(imageSpan);
//            int end = spannable.getSpanEnd(imageSpan);
//            int flags = spannable.getSpanFlags(imageSpan);
//            Log.i(TAG, "invalidateDrawable: 2");
//            imageSpan.isChanging = true;
////            spannable.removeSpan(imageSpan);
//            spannable.setSpan(imageSpan, start, end, flags);
//            imageSpan.isChanging = false;
//          }
//        }
//        Log.i(TAG, "invalidateDrawable: 4");
//      }

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


}
