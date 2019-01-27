package com.sunhapper.spedittool.util;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.style.ImageSpan;
import com.sunhapper.spedittool.drawable.RefreshableDrawable;

/**
 * Created by sunha on 2018/1/25 0025.
 */
@Deprecated
class GifSpanWatcher implements SpanWatcher {

  private Drawable.Callback callback;

  public GifSpanWatcher(Callback callback) {
    this.callback = callback;
  }

  private static final String TAG = "GifSpanWatcher";

  @Override
  public void onSpanAdded(Spannable text, Object what, int start, int end) {

  }

  @Override
  public void onSpanRemoved(Spannable text, Object what, int start, int end) {
    if (what instanceof ImageSpan) {
      ImageSpan imageSpan = (ImageSpan) what;
      Drawable drawable = imageSpan.getDrawable();
      if (drawable instanceof RefreshableDrawable) {
        ((RefreshableDrawable) drawable).removeCallback(callback);
      } else if (drawable != null) {
        drawable.setCallback(null);
      }
    }
  }

  @Override
  public void onSpanChanged(Spannable text, Object what, int ostart, int oend, int nstart,
      int nend) {
  }
}