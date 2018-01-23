package me.sunhapper.spcharedittool.glide;

import android.graphics.drawable.Animatable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * Created by sunha on 2018/1/23 0023.
 */

public class GifTarget extends SimpleTarget<GifDrawable> {

  private static final String TAG = "GifTarget";
  private PreGifDrawable gifDrawable;

  @Override
  public void onResourceReady(@NonNull GifDrawable resource,
      @Nullable Transition<? super GifDrawable> transition) {
    gifDrawable.setDrawable(resource);
    if (resource instanceof Animatable) {
      resource.setLoopCount(GifDrawable.LOOP_FOREVER);
      resource.start();
    }
    gifDrawable.invalidateSelf();
  }

  public GifTarget(PreGifDrawable gifDrawable) {
    this.gifDrawable = gifDrawable;
  }

}
