package com.sunhapper.x.spedit.gif.span;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.sunhapper.x.spedit.gif.drawable.InvalidateDrawable;

/**
 * Created by sunhapper on 2019/1/28 .
 */
public class GifIsoheightImageSpan extends IsoheightImageSpan implements RefreshSpan{
    public GifIsoheightImageSpan(Drawable d) {
        super(d);
    }

    public GifIsoheightImageSpan(@NonNull Drawable d, int verticalAlignment) {
        super(d, verticalAlignment);
    }

    public GifIsoheightImageSpan(@NonNull Drawable d,
            @NonNull String source) {
        super(d, source);
    }

    public GifIsoheightImageSpan(@NonNull Drawable d,
            @NonNull String source, int verticalAlignment) {
        super(d, source, verticalAlignment);
    }

    public GifIsoheightImageSpan(Context context, Uri uri) {
        super(context, uri);
    }

    public GifIsoheightImageSpan(@NonNull Context context,
            @NonNull Uri uri, int verticalAlignment) {
        super(context, uri, verticalAlignment);
    }

    public GifIsoheightImageSpan(@NonNull Context context, int resourceId) {
        super(context, resourceId);
    }

    public GifIsoheightImageSpan(@NonNull Context context, int resourceId,
            int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
    }

    public GifIsoheightImageSpan(@NonNull Bitmap b) {
        super(b);
    }

    public GifIsoheightImageSpan(@NonNull Bitmap b, int verticalAlignment) {
        super(b, verticalAlignment);
    }

    public GifIsoheightImageSpan(@NonNull Context context,
            @NonNull Bitmap b) {
        super(context, b);
    }

    public GifIsoheightImageSpan(@NonNull Context context,
            @NonNull Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
    }

    @Override
    public InvalidateDrawable getInvalidateDrawable() {
        Drawable d= getResizedDrawable();
        if (d instanceof InvalidateDrawable){
            return (InvalidateDrawable) d;
        }else{
            return null;
        }
    }

}
