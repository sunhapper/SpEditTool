package me.sunhapper.spcharedittool.span;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import com.sunhapper.spedittool.span.GifSpan;
import me.sunhapper.spcharedittool.Measurable;

public class GifAlignCenterSpan extends GifSpan {

  boolean hasResize = false;
  private static final String TAG = "GifAlignCenterSpan";
  private static final char[] ELLIPSIS_NORMAL = {'\u2026'}; // this is "..."
  private static final char[] ELLIPSIS_TWO_DOTS = {'\u2025'}; // this is ".."

  public GifAlignCenterSpan(Context context, Bitmap b) {
    super(context, b);
  }

  public GifAlignCenterSpan(Context context, Bitmap b, int verticalAlignment) {
    super(context, b, verticalAlignment);
  }

  public GifAlignCenterSpan(Drawable d) {
    super(d);
  }

  public GifAlignCenterSpan(Drawable d, int verticalAlignment) {
    super(d, verticalAlignment);
  }

  public GifAlignCenterSpan(Drawable d, String source) {
    super(d, source);
  }

  public GifAlignCenterSpan(Drawable d, String source, int verticalAlignment) {
    super(d, source, verticalAlignment);
  }

  public GifAlignCenterSpan(Context context, Uri uri) {
    super(context, uri);
  }

  public GifAlignCenterSpan(Context context, Uri uri, int verticalAlignment) {
    super(context, uri, verticalAlignment);
  }

  public GifAlignCenterSpan(Context context, int resourceId) {
    super(context, resourceId);
  }

  public GifAlignCenterSpan(Context context, int resourceId, int verticalAlignment) {
    super(context, resourceId, verticalAlignment);
  }


  @Override
  public int getSize(Paint paint, CharSequence text,
      int start, int end, Paint.FontMetricsInt fm) {
    Drawable d = getResizedDrawable(paint);
    if (d == null) {
      return 0;
    }

    Rect rect = d.getBounds();
    if (fm != null) {
      Paint.FontMetricsInt fontMetrics = new Paint.FontMetricsInt();
      paint.getFontMetricsInt(fontMetrics);

      fm.ascent = fontMetrics.ascent;
      fm.descent = fontMetrics.descent;

      fm.top = fontMetrics.top;
      fm.bottom = fontMetrics.bottom;
    }

    return rect.right;
  }

  private Drawable getResizedDrawable(Paint paint) {
    int fontMetricsInt = paint.getFontMetricsInt(null);
    Drawable d = getDrawable();
    if (d != null && !hasResize) {
      if (d instanceof Measurable) {
        Measurable measurableDrawable = (Measurable) d;
        if (measurableDrawable.canMeasure()) {
          Log.i(TAG, "getResizedDrawable: ");
          if (measurableDrawable.getHeight() > 0
              && measurableDrawable.getWidth() > 0) {
            hasResize = true;
            d.setBounds(new Rect(0, 0,
                (int) (1f * fontMetricsInt * measurableDrawable.getWidth() / measurableDrawable
                    .getHeight()),
                fontMetricsInt));
          }
        }
      } else {
        hasResize = true;
        d.setBounds(new Rect(0, 0, fontMetricsInt, fontMetricsInt));
      }
    }
    return d;
  }

  @Override
  public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y,
      int bottom, Paint paint) {
    final String s = text.toString();
    String subS = s.substring(start, end);
    if (ELLIPSIS_NORMAL[0] == subS.charAt(0)
        || ELLIPSIS_TWO_DOTS[0] == subS.charAt(0)) {
      canvas.save();
      canvas.drawText(subS, x, y, paint);
      canvas.restore();
    } else {
      Drawable d = getResizedDrawable(paint);
      canvas.save();
      int transY;
      Paint.FontMetricsInt fontMetrics = new Paint.FontMetricsInt();
      paint.getFontMetricsInt(fontMetrics);
      Rect rect = d.getBounds();

      transY = y + fontMetrics.ascent;

//          Log.v("ImageSpanAlignCenter", "transY=" + transY + " y=" + y + " " + " top=" + top + " bottom=" + bottom
//                    + " rect.bottom=" + rect.bottom + " rect.top=" + rect.top + " " + fontMetrics);
      canvas.translate(x, transY);
      d.draw(canvas);
      canvas.restore();
    }
  }

  @Override
  public Drawable getGifDrawable() {
    return getDrawable();
  }

//  private Drawable getCachedDrawable(Paint paint) {
//    WeakReference<Drawable> wr = mDrawableRef;
//    Drawable d = null;
//
//    if (wr != null) {
//      d = wr.get();
//    }
//
//    if (d == null) {
//      d = getDrawable();
//      if (d != null) {
//        d.setBounds(new Rect(0, 0, paint.getFontMetricsInt(null), paint.getFontMetricsInt(null)));
//        mDrawableRef = new WeakReference<Drawable>(d);
//      }
//
//    }
//
//    return d;
//  }
//
//  private WeakReference<Drawable> mDrawableRef;

}
