package com.sunhapper.spedittool.span;

import android.graphics.drawable.Drawable;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.style.ImageSpan;

/**
 * Created by sunhapper on 2018/1/21.
 */

public abstract class GifSpan extends ImageSpan {

  boolean isChanging = false;
  private static final String TAG = "GifSpan";


  public GifSpan(Drawable d) {
    super(d);
  }


  public Drawable getGifDrawable() {
    return getDrawable();
  }

  public static GifSpanWatcher getGifSpanWatcher() {
    return gifSpanWatcher;
  }

  private static GifSpanWatcher gifSpanWatcher = new GifSpanWatcher();

  public static class GifSpanWatcher implements SpanWatcher {

    @Override
    public void onSpanAdded(Spannable text, Object what, int start, int end) {

    }

    @Override
    public void onSpanRemoved(Spannable text, Object what, int start, int end) {
      if (what instanceof GifSpan) {
        GifSpan gifSpan = (GifSpan) what;
        if (!gifSpan.isChanging) {
          Drawable drawable = gifSpan.getGifDrawable();
          if (drawable != null) {
            drawable.setCallback(null);
          }
        }
      }
    }

    @Override
    public void onSpanChanged(Spannable text, Object what, int ostart, int oend, int nstart,
        int nend) {
    }
  }
}
