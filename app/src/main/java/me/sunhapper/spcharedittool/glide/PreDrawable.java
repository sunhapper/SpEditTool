package me.sunhapper.spcharedittool.glide;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.TextView;
import com.sunhapper.spedittool.drawable.RefreshableDrawable;
import java.util.ArrayList;
import java.util.List;
import me.sunhapper.spcharedittool.Measurable;

/**
 * Created by sunha on 2018/1/23 0023.
 */

public class PreDrawable extends Drawable implements RefreshableDrawable,Drawable.Callback, Measurable {
  private List< TextView> hosts;
  private static final String TAG = "PreDrawable";
  private Drawable mDrawable;
  private boolean needResize;
  private CallBack callBack=new CallBack();

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
    if (drawable==null){
      return;
    }
    if (this.mDrawable != null) {
      this.mDrawable.setCallback(null);
    }
    drawable.setCallback(this);
    this.mDrawable = drawable;
    needResize = true;
    if (getCallback() != null) {
      getCallback().invalidateDrawable(this);
    }
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

  @Override
  public boolean canRefresh() {
    return true;
  }

  @Override
  public int getInterval() {
    return 60;
  }

  @Override
  public void addHost(TextView tv) {
    if (hosts==null){
      hosts=new ArrayList<>();
      //Glide的GifDrawable的findCallback会一直去找不为Drawable的Callback
      // 所以不能直接implements Drawable.Callback
      setCallback(callBack);
    }
    if (!hosts.contains(tv)){
      hosts.add(tv);
    }
  }

  @Override
  public void removeHost(TextView tv) {
    if (hosts!=null&&hosts.contains(tv)){
      hosts.remove(tv);
    }
  }

  class CallBack implements Drawable.Callback{

    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
      if (hosts!=null){
        for (TextView tv : hosts) {
          tv.invalidate();
        }
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