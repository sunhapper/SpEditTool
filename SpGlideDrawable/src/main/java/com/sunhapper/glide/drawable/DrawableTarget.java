package com.sunhapper.glide.drawable;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * Created by sunha on 2018/1/23 0023.
 */

public class DrawableTarget extends SimpleTarget<Drawable> {

  private static final String TAG = "GifTarget";
  private GlideProxyDrawable mGlideProxyDrawable;

  @Override
  public void onResourceReady(@NonNull Drawable resource,
      @Nullable Transition<? super Drawable> transition) {
    Log.i(TAG, "onResourceReady: " + resource);
    mGlideProxyDrawable.setDrawable(resource);
    if (resource instanceof GifDrawable) {
      ((GifDrawable) resource).setLoopCount(GifDrawable.LOOP_FOREVER);
      ((GifDrawable) resource).start();
    }
    mGlideProxyDrawable.invalidateSelf();
  }

  public DrawableTarget(GlideProxyDrawable mGlideProxyDrawable) {
    this.mGlideProxyDrawable = mGlideProxyDrawable;
  }


  @Override
  public void onLoadCleared(@Nullable Drawable placeholder) {
    Log.i(TAG, "onLoadCleared: " + placeholder);
    if (mGlideProxyDrawable.getDrawable() != null) {
      return;
    }
    mGlideProxyDrawable.setDrawable(placeholder);
    if (placeholder instanceof GifDrawable) {
      ((GifDrawable) placeholder).setLoopCount(GifDrawable.LOOP_FOREVER);
      ((GifDrawable) placeholder).start();
    }
    mGlideProxyDrawable.invalidateSelf();

  }

  @Override
  public void onLoadStarted(@Nullable Drawable placeholder) {
    Log.i(TAG, "onLoadCleared: " + placeholder);
    mGlideProxyDrawable.setDrawable(placeholder);
    if (placeholder instanceof GifDrawable) {
      ((GifDrawable) placeholder).setLoopCount(GifDrawable.LOOP_FOREVER);
      ((GifDrawable) placeholder).start();
    }
    mGlideProxyDrawable.invalidateSelf();
  }

  @Override
  public void onLoadFailed(@Nullable Drawable errorDrawable) {
    Log.i(TAG, "onLoadFailed: " + errorDrawable);
    mGlideProxyDrawable.setDrawable(errorDrawable);
    if (errorDrawable instanceof GifDrawable) {
      ((GifDrawable) errorDrawable).setLoopCount(GifDrawable.LOOP_FOREVER);
      ((GifDrawable) errorDrawable).start();
    }
    mGlideProxyDrawable.invalidateSelf();
  }

}
