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

public class GifDrawableTarget extends SimpleTarget<GifDrawable> {

  private static final String TAG = "GifTarget";
  private GlidePreDrawable glidePreDrawable;

  @Override
  public void onResourceReady(@NonNull GifDrawable resource,
      @Nullable Transition<? super GifDrawable> transition) {
    Log.i(TAG, "onResourceReady: " + resource);
    glidePreDrawable.setDrawable(resource);
    resource.setLoopCount(GifDrawable.LOOP_FOREVER);
    resource.start();
    glidePreDrawable.invalidateSelf();
  }

  public GifDrawableTarget(GlidePreDrawable glidePreDrawable) {
    this.glidePreDrawable = glidePreDrawable;
  }


  @Override
  public void onLoadCleared(@Nullable Drawable placeholder) {
    Log.i(TAG, "onLoadCleared: " + placeholder);
    if (glidePreDrawable.getDrawable() != null) {
      return;
    }
    glidePreDrawable.setDrawable(placeholder);
    if (placeholder instanceof GifDrawable) {
      ((GifDrawable) placeholder).setLoopCount(GifDrawable.LOOP_FOREVER);
      ((GifDrawable) placeholder).start();
    }
    glidePreDrawable.invalidateSelf();

  }

  @Override
  public void onLoadStarted(@Nullable Drawable placeholder) {
    Log.i(TAG, "onLoadCleared: " + placeholder);
    glidePreDrawable.setDrawable(placeholder);
    if (placeholder instanceof GifDrawable) {
      ((GifDrawable) placeholder).setLoopCount(GifDrawable.LOOP_FOREVER);
      ((GifDrawable) placeholder).start();
    }
    glidePreDrawable.invalidateSelf();
  }

  @Override
  public void onLoadFailed(@Nullable Drawable errorDrawable) {
    Log.i(TAG, "onLoadFailed: " + errorDrawable);
    glidePreDrawable.setDrawable(errorDrawable);
    if (errorDrawable instanceof GifDrawable) {
      ((GifDrawable) errorDrawable).setLoopCount(GifDrawable.LOOP_FOREVER);
      ((GifDrawable) errorDrawable).start();
    }
    glidePreDrawable.invalidateSelf();
  }

}
