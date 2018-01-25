package me.sunhapper.spcharedittool.glide;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import me.sunhapper.spcharedittool.Measurable;

/**
 * Created by sunha on 2018/1/23 0023.
 */

public class PreDrawable extends Drawable implements Drawable.Callback, Measurable {

  private static final String TAG = "PreGifDrawable";
  private Drawable mDrawable;
  private boolean needResize;

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
    drawable.setCallback(this);
    this.mDrawable = drawable;
    needResize = true;
  }

  @Override
  protected void onBoundsChange(Rect bounds) {
    super.onBoundsChange(bounds);
    needResize = false;
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

  @Override
  public boolean needResize() {
    return mDrawable != null && needResize;
  }

  public Drawable getDrawable() {
    return mDrawable;
  }
}