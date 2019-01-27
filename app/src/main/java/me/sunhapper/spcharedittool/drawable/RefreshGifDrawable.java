package me.sunhapper.spcharedittool.drawable;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
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
 * Created by sunhapper on 2018/1/28.
 */

public class RefreshGifDrawable extends GifDrawable implements InvalidateDrawable {

    private List<RefreshListener> mRefreshListeners = new ArrayList<>();

    public RefreshGifDrawable(@NonNull Resources res,
            int id) throws NotFoundException, IOException {
        super(res, id);
    }

    public RefreshGifDrawable(@NonNull AssetManager assets,
            @NonNull String assetName) throws IOException {
        super(assets, assetName);
    }

    public RefreshGifDrawable(@NonNull String filePath) throws IOException {
        super(filePath);
    }

    public RefreshGifDrawable(@NonNull File file) throws IOException {
        super(file);
    }

    public RefreshGifDrawable(@NonNull InputStream stream) throws IOException {
        super(stream);
    }

    public RefreshGifDrawable(@NonNull AssetFileDescriptor afd) throws IOException {
        super(afd);
    }

    public RefreshGifDrawable(@NonNull FileDescriptor fd) throws IOException {
        super(fd);
    }

    public RefreshGifDrawable(@NonNull byte[] bytes) throws IOException {
        super(bytes);
    }

    public RefreshGifDrawable(@NonNull ByteBuffer buffer) throws IOException {
        super(buffer);
    }

    public RefreshGifDrawable(
            @Nullable ContentResolver resolver,
            @NonNull Uri uri) throws IOException {
        super(resolver, uri);
    }

    protected RefreshGifDrawable(@NonNull InputSource inputSource,
            @Nullable GifDrawable oldDrawable,
            @Nullable ScheduledThreadPoolExecutor executor,
            boolean isRenderingTriggeredOnDraw, @NonNull GifOptions options)
            throws IOException {
        super(inputSource, oldDrawable, executor, isRenderingTriggeredOnDraw, options);
    }

    {
        setCallback(new CallBack());
    }




    @Override
    public void addRefreshListener(RefreshListener refreshListener) {
        mRefreshListeners.add(refreshListener);
    }

    @Override
    public void removeRefreshListener(RefreshListener refreshListener) {
        mRefreshListeners.remove(refreshListener);
    }

    @Override
    public int intervalTime() {
        return getDuration() / getNumberOfFrames();
    }

    private class CallBack implements Callback {

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
