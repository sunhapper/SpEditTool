package me.sunhapper.spcharedittool.glide;

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

public class GlideReusePreDrawable extends GlidePreDrawable implements RefreshableDrawable,
    Measurable {

  private List<TextView> hosts;
  private CallBack callBack = new CallBack();

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
    if (hosts == null) {
      hosts = new ArrayList<>();
      //Glide的GifDrawable的findCallback会一直去找不为Drawable的Callback
      // 所以不能直接implements Drawable.Callback
      setCallback(callBack);
    }
    if (!hosts.contains(tv)) {
      hosts.add(tv);
    }
  }

  @Override
  public void removeHost(TextView tv) {
    if (hosts != null && hosts.contains(tv)) {
      hosts.remove(tv);
    }
  }

  class CallBack implements Callback {

    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
      if (hosts != null) {
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