package me.sunhapper.spcharedittool.glide;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import me.sunhapper.spcharedittool.Measurable;

/**
 * Created by sunha on 2018/1/23 0023.
 */

public class PreGifDrawable extends Drawable implements Drawable.Callback, Measurable {

  private GifDrawable mDrawable;

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

  public void setDrawable(GifDrawable drawable) {
    if (this.mDrawable != null) {
      this.mDrawable.setCallback(null);
    }
    drawable.setCallback(this);
    this.mDrawable = drawable;
  }

  @Override
  public void invalidateDrawable(Drawable who) {
    if (getCallback() != null) {
      getCallback().invalidateDrawable(who);
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
//    if (mDrawable != null) {
//      mDrawable.setBounds(bounds);
//    }
  }

  @Override
  public int getWidth() {
    if (mDrawable != null) {
      return mDrawable.getIntrinsicWidth();
    }
    return 0;
  }

  @Override
  public int getHeight() {
    if (mDrawable != null) {
      return mDrawable.getIntrinsicHeight();
    }
    return 0;
  }

  @Override
  public boolean canMeasure() {
    return mDrawable != null;
  }
}