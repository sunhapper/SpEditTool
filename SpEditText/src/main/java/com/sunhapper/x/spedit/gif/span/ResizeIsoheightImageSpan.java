package com.sunhapper.x.spedit.gif.span;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.sunhapper.x.spedit.gif.drawable.ResizeDrawable;


public class ResizeIsoheightImageSpan extends GifIsoheightImageSpan implements RefreshSpan {


    public ResizeIsoheightImageSpan(Drawable d) {
        super(d);
    }

    public ResizeIsoheightImageSpan(@NonNull Drawable d, int verticalAlignment) {
        super(d, verticalAlignment);
    }

    public ResizeIsoheightImageSpan(@NonNull Drawable d, @NonNull String source) {
        super(d, source);
    }

    public ResizeIsoheightImageSpan(@NonNull Drawable d, @NonNull String source, int verticalAlignment) {
        super(d, source, verticalAlignment);
    }

    public ResizeIsoheightImageSpan(Context context, Uri uri) {
        super(context, uri);
    }

    public ResizeIsoheightImageSpan(@NonNull Context context, @NonNull Uri uri, int verticalAlignment) {
        super(context, uri, verticalAlignment);
    }

    public ResizeIsoheightImageSpan(@NonNull Context context, int resourceId) {
        super(context, resourceId);
    }

    public ResizeIsoheightImageSpan(@NonNull Context context, int resourceId, int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
    }

    public ResizeIsoheightImageSpan(@NonNull Bitmap b) {
        super(b);
    }

    public ResizeIsoheightImageSpan(@NonNull Bitmap b, int verticalAlignment) {
        super(b, verticalAlignment);
    }

    public ResizeIsoheightImageSpan(@NonNull Context context, @NonNull Bitmap b) {
        super(context, b);
    }

    public ResizeIsoheightImageSpan(@NonNull Context context, @NonNull Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
    }

    @Override
    protected Drawable getResizedDrawable() {
        Drawable d = getDrawable();
        if (drawableHeight == 0) {
            return d;
        }
        if (d instanceof ResizeDrawable) {
            ResizeDrawable resizeDrawable = (ResizeDrawable) d;
            if (resizeDrawable.needResize() || !resized) {
                resized = true;
                d.setBounds(new Rect(0, 0,
                        (int) (1f * drawableHeight * resizeDrawable.getWidth() / resizeDrawable.getHeight()),
                        drawableHeight));
            }
        } else if (!resized) {
            resized = true;
            d.setBounds(new Rect(0, 0,
                    (int) (1f * drawableHeight * d.getIntrinsicWidth() / d.getIntrinsicWidth()),
                    drawableHeight));
        }
        return d;
    }

}
