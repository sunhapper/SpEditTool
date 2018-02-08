package me.sunhapper.spcharedittool.span;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.ImageSpan;
import me.sunhapper.spcharedittool.Measurable;

public class VerticalCenterSpan extends ImageSpan {

  private boolean resized = false;


  public VerticalCenterSpan(Drawable d) {
    super(d, ALIGN_BASELINE);
  }


  public VerticalCenterSpan(Context context, Uri uri) {
    super(context, uri);
  }

  @Override
  public int getSize(Paint paint, CharSequence text,
      int start, int end, FontMetricsInt fm) {
    Drawable drawable = getResizedDrawable();
    Rect bounds = drawable.getBounds();
    return bounds.right;
  }


  private Drawable getResizedDrawable() {
    Drawable d = getDrawable();
    if (d instanceof Measurable) {
      Measurable measurableDrawable = (Measurable) d;
      if (measurableDrawable.needResize() || !resized) {
        resized = true;
        d.setBounds(new Rect(0, 0, measurableDrawable.getWidth(), measurableDrawable.getHeight()));
      }
    } else if (!resized) {
      resized = true;
      d.setBounds(new Rect(0, 0, d.getIntrinsicWidth(), d.getIntrinsicWidth()));
    }
    return d;
  }

  @Override
  public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y,
      int bottom, Paint paint) {
    Drawable d = getResizedDrawable();
    canvas.save();
    int transY;
    FontMetricsInt fm = paint.getFontMetricsInt();
    transY = y + (fm.descent + fm.ascent) / 2 - d.getBounds().bottom / 2;
    canvas.translate(x, transY);
    d.draw(canvas);
    canvas.restore();
  }
}
