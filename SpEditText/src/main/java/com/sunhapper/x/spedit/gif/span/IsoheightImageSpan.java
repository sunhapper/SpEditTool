package com.sunhapper.x.spedit.gif.span;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.style.ImageSpan;

import com.sunhapper.x.spedit.mention.span.IntegratedSpan;

/**
 * 和文字等高的ImageSpan
 * 在选择模式下，光标的index可能在Span对应的文字内部，标记为IntegratedSpan可以防止光标
 * 在内部，在replace其他文字时可以正常删除该span
 */
public class IsoheightImageSpan extends ImageSpan implements IntegratedSpan {
    protected boolean resized = false;
    private static final char[] ELLIPSIS_NORMAL = {'\u2026'}; // this is "..."
    private static final char[] ELLIPSIS_TWO_DOTS = {'\u2025'}; // this is ".."
    protected int drawableHeight = 0;

    public IsoheightImageSpan(Drawable d) {
        super(d);
    }

    public IsoheightImageSpan(@NonNull Drawable d, int verticalAlignment) {
        super(d, verticalAlignment);
    }

    public IsoheightImageSpan(@NonNull Drawable d,
            @NonNull String source) {
        super(d, source);
    }

    public IsoheightImageSpan(@NonNull Drawable d,
            @NonNull String source, int verticalAlignment) {
        super(d, source, verticalAlignment);
    }

    public IsoheightImageSpan(Context context, Uri uri) {
        super(context, uri);
    }

    public IsoheightImageSpan(@NonNull Context context,
            @NonNull Uri uri, int verticalAlignment) {
        super(context, uri, verticalAlignment);
    }

    public IsoheightImageSpan(@NonNull Context context, int resourceId) {
        super(context, resourceId);
    }

    public IsoheightImageSpan(@NonNull Context context, int resourceId, int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
    }

    public IsoheightImageSpan(@NonNull Bitmap b) {
        super(b);
    }

    public IsoheightImageSpan(@NonNull Bitmap b, int verticalAlignment) {
        super(b, verticalAlignment);
    }

    public IsoheightImageSpan(@NonNull Context context,
            @NonNull Bitmap b) {
        super(context, b);
    }

    public IsoheightImageSpan(@NonNull Context context,
            @NonNull Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
    }

    @Override
    public int getSize(Paint paint, CharSequence text,
            int start, int end, FontMetricsInt fm) {
        if (fm != null && paint.getFontMetricsInt() != null) {
            //paint.getFontMetricsInt() 和 参数中FontMetricsInt不是同一个对象，数据不同
            // FontMetricsInt会变化，paint.getFontMetricsInt()会和TextView的文字大小保持一致
            FontMetricsInt paintFontMetricsInt = paint.getFontMetricsInt();
            drawableHeight = paintFontMetricsInt.descent - paintFontMetricsInt.ascent;
        }
        Drawable drawable = getResizedDrawable();
        Rect bounds = drawable.getBounds();
        return bounds.right;
    }

    protected Drawable getResizedDrawable() {

        Drawable d = getDrawable();
        if (drawableHeight == 0) {
            return d;
        }
        if (!resized) {
            resized = true;
            d.setBounds(new Rect(0, 0,
                    (int) (1f * drawableHeight * d.getIntrinsicWidth() / d.getIntrinsicHeight()),
                    drawableHeight));
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
            Drawable d = getResizedDrawable();
            canvas.save();
            int transY;
            FontMetricsInt fm = paint.getFontMetricsInt();
            transY = y + fm.ascent;
            canvas.translate(x, transY);
            d.draw(canvas);
            canvas.restore();
        }
    }

}
