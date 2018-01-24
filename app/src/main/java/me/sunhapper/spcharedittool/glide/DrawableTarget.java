package me.sunhapper.spcharedittool.glide;

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
  private PreDrawable preDrawable;

  @Override
  public void onResourceReady(@NonNull Drawable resource,
      @Nullable Transition<? super Drawable> transition) {
    Log.i(TAG, "onResourceReady: " + resource);
    preDrawable.setDrawable(resource);
    if (resource instanceof GifDrawable) {
      ((GifDrawable) resource).setLoopCount(GifDrawable.LOOP_FOREVER);
      ((GifDrawable) resource).start();
    }
    preDrawable.invalidateSelf();
  }

  public DrawableTarget(PreDrawable preDrawable) {
    this.preDrawable = preDrawable;
  }


  @Override
  public void onLoadCleared(@Nullable Drawable placeholder) {
    Log.i(TAG, "onLoadCleared: " + placeholder);
    if (preDrawable.getDrawable() != null) {
      return;
    }
    preDrawable.setDrawable(placeholder);
    if (placeholder instanceof GifDrawable) {
      ((GifDrawable) placeholder).setLoopCount(GifDrawable.LOOP_FOREVER);
      ((GifDrawable) placeholder).start();
    }
    preDrawable.invalidateSelf();

  }

  @Override
  public void onLoadStarted(@Nullable Drawable placeholder) {
    Log.i(TAG, "onLoadCleared: " + placeholder);
    preDrawable.setDrawable(placeholder);
    if (placeholder instanceof GifDrawable) {
      ((GifDrawable) placeholder).setLoopCount(GifDrawable.LOOP_FOREVER);
      ((GifDrawable) placeholder).start();
    }
    preDrawable.invalidateSelf();
  }

  @Override
  public void onLoadFailed(@Nullable Drawable errorDrawable) {
    Log.i(TAG, "onLoadFailed: " + errorDrawable);
    preDrawable.setDrawable(errorDrawable);
    if (errorDrawable instanceof GifDrawable) {
      ((GifDrawable) errorDrawable).setLoopCount(GifDrawable.LOOP_FOREVER);
      ((GifDrawable) errorDrawable).start();
    }
    preDrawable.invalidateSelf();
  }

}
