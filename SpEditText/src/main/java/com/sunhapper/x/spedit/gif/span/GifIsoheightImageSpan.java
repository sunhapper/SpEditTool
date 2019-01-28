package com.sunhapper.x.spedit.gif.span;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.sunhapper.x.spedit.gif.drawable.InvalidateDrawable;

/**
 * Created by sunhapper on 2019/1/28 .
 */
public class GifIsoheightImageSpan extends IsoheightImageSpan implements RefreshSpan{
    public GifIsoheightImageSpan(Drawable d) {
        super(d);
    }

    public GifIsoheightImageSpan(Context context, Uri uri) {
        super(context, uri);
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
