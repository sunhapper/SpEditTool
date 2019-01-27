package me.sunhapper.spcharedittool.drawable;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sunhapper.x.spedit.gif.drawable.InvalidateDrawable;
import com.sunhapper.x.spedit.gif.listener.RefreshListener;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifOptions;
import pl.droidsonroids.gif.InputSource;

/**
 * Created by sunha on 2018/1/23 0023.
 */

public class GifPlaceHolderDrawable extends GifDrawable implements Drawable.Callback, InvalidateDrawable {

    private Drawable mDrawable;
    private List<RefreshListener> mRefreshListeners = new ArrayList<>();
    private Callback mCallback = new CallBack();

    public GifPlaceHolderDrawable(@NonNull Resources res, int id)
            throws Resources.NotFoundException, IOException {
        super(res, id);
    }

    public GifPlaceHolderDrawable(@NonNull AssetManager assets, @NonNull String assetName)
            throws IOException {
        super(assets, assetName);
    }

    public GifPlaceHolderDrawable(@NonNull String filePath) throws IOException {
        super(filePath);
    }

    public GifPlaceHolderDrawable(@NonNull File file) throws IOException {
        super(file);
    }

    public GifPlaceHolderDrawable(@NonNull InputStream stream) throws IOException {
        super(stream);
    }

    public GifPlaceHolderDrawable(@NonNull AssetFileDescriptor afd) throws IOException {
        super(afd);
    }

    public GifPlaceHolderDrawable(@NonNull FileDescriptor fd) throws IOException {
        super(fd);
    }

    public GifPlaceHolderDrawable(@NonNull byte[] bytes) throws IOException {
        super(bytes);
    }

    public GifPlaceHolderDrawable(@NonNull ByteBuffer buffer) throws IOException {
        super(buffer);
    }

    public GifPlaceHolderDrawable(@Nullable ContentResolver resolver,
            @NonNull Uri uri) throws IOException {
        super(resolver, uri);
    }

    protected GifPlaceHolderDrawable(@NonNull InputSource inputSource,
            @Nullable GifDrawable oldDrawable, @Nullable ScheduledThreadPoolExecutor executor,
            boolean isRenderingTriggeredOnDraw, @NonNull GifOptions options) throws IOException {
        super(inputSource, oldDrawable, executor, isRenderingTriggeredOnDraw, options);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mDrawable != null) {
            mDrawable.draw(canvas);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        if (mDrawable != null) {
            mDrawable.setAlpha(alpha);
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (mDrawable != null) {
            mDrawable.setColorFilter(cf);
        }
    }

    @Override
    public int getOpacity() {
        if (mDrawable != null) {
            return mDrawable.getOpacity();
        }
        return PixelFormat.UNKNOWN;
    }

    public void setDrawable(Drawable drawable) {
        if (this.mDrawable != null) {
            this.mDrawable.setCallback(null);
        }
        drawable.setCallback(mCallback);
        this.mDrawable = drawable;
        if (getCallback() != null) {
            getCallback().invalidateDrawable(this);
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
    }

    @Override
    public void invalidateDrawable(Drawable who) {
        if (getCallback() != null) {
            getCallback().invalidateDrawable(this);
        }
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        if (getCallback() != null) {
            getCallback().scheduleDrawable(who, what, when);
        }
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        if (getCallback() != null) {
            getCallback().unscheduleDrawable(who, what);
        }
    }

    @Override
    public void setBounds(@NonNull Rect bounds) {
        super.setBounds(bounds);
        if (mDrawable != null) {
            mDrawable.setBounds(bounds);
        }
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        if (mDrawable != null) {
            mDrawable.setBounds(left, top, right, bottom);
        }
    }


    public Drawable getDrawable() {
        return mDrawable;
    }

    @Override
    public void addRefreshListener(RefreshListener callback) {
        mRefreshListeners.add(callback);
    }

    @Override
    public void removeRefreshListener(RefreshListener callback) {
        mRefreshListeners.remove(callback);
    }

    @Override
    public int intervalTime() {
        return 60;
    }

    class CallBack implements Callback {

        @Override
        public void invalidateDrawable(@NonNull Drawable who) {
            for (RefreshListener listener : mRefreshListeners) {
                listener.onRefresh();
            }
        }

        @Override
        public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {

        }

        @Override
        public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {

        }
    }
}