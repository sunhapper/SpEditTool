package com.sunhapper.spedittool.util;

import android.graphics.drawable.Drawable;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.style.ImageSpan;

/**
 * Created by sunha on 2018/1/25 0025.
 */

class UnreuseSpanWatcher implements SpanWatcher {


  private static final String TAG = "GifSpanWatcher";

  @Override
  public void onSpanAdded(Spannable text, Object what, int start, int end) {

  }

  @Override
  public void onSpanRemoved(Spannable text, Object what, int start, int end) {
    if (what instanceof ImageSpan) {
      ImageSpan imageSpan = (ImageSpan) what;
      Drawable drawable = imageSpan.getDrawable();
      if (drawable != null) {
        drawable.setCallback(null);
      }
    }
  }

  @Override
  public void onSpanChanged(Spannable text, Object what, int ostart, int oend, int nstart,
      int nend) {
  }
}