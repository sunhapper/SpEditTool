package com.sunhapper.spedittool.span;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.style.ImageSpan;
import android.util.Log;

/**
 * Created by sunhapper on 2018/1/21.
 */

public abstract class GifSpan extends ImageSpan {
   boolean isChanging=false;
  private static final String TAG = "GifSpan";

  public GifSpan(Context context, Bitmap b) {
    super(context, b);
  }

  public GifSpan(Context context, Bitmap b, int verticalAlignment) {
    super(context, b, verticalAlignment);
  }

  public GifSpan(Drawable d) {
    super(d);
  }

  public GifSpan(Drawable d, int verticalAlignment) {
    super(d, verticalAlignment);
  }

  public GifSpan(Drawable d, String source) {
    super(d, source);
  }

  public GifSpan(Drawable d, String source, int verticalAlignment) {
    super(d, source, verticalAlignment);
  }

  public GifSpan(Context context, Uri uri) {
    super(context, uri);
  }

  public GifSpan(Context context, Uri uri, int verticalAlignment) {
    super(context, uri, verticalAlignment);
  }

  public GifSpan(Context context, int resourceId) {
    super(context, resourceId);
  }

  public GifSpan(Context context, int resourceId, int verticalAlignment) {
    super(context, resourceId, verticalAlignment);
  }

  public abstract Drawable getGifDrawable();

  public static GifSpanWatcher getGifSpanWatcher() {
    return gifSpanWatcher;
  }

  private static GifSpanWatcher gifSpanWatcher = new GifSpanWatcher();

  public static class GifSpanWatcher implements SpanWatcher {

    @Override
    public void onSpanAdded(Spannable text, Object what, int start, int end) {
      Log.i(TAG, "onSpanAdded: " + text + "  what:" + what);
    }

    @Override
    public void onSpanRemoved(Spannable text, Object what, int start, int end) {
      Log.i(TAG, "onSpanRemoved: " + text + "  what:" + what);
    }

    @Override
    public void onSpanChanged(Spannable text, Object what, int ostart, int oend, int nstart,
        int nend) {
      Log.i(TAG, "onSpanChanged: " + text + "  what:" + what);
    }
  }
}
