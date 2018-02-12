package me.sunhapper.spcharedittool.glide;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import com.sunhapper.spedittool.drawable.RefreshableDrawable;
import java.util.Set;
import java.util.WeakHashMap;
import me.sunhapper.spcharedittool.Measurable;

/**
 * Created by sunha on 2018/1/23 0023.
 */

public class GlideReusePreDrawable extends GlidePreDrawable implements RefreshableDrawable,
    Measurable {

  private WeakHashMap<Callback, Integer> callbackWeakHashMap;
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
  public void addHost(Drawable.Callback currentCallback) {
    if (callbackWeakHashMap == null) {
      callbackWeakHashMap = new WeakHashMap<>();
      //Glide的GifDrawable的findCallback会一直去找不为Drawable的Callback
      // 所以不能直接implements Drawable.Callback
      setCallback(callBack);
    }
    if (!containsCallback(currentCallback)) {
      callbackWeakHashMap.put(currentCallback, 1);
    } else {
      int count = callbackWeakHashMap.get(currentCallback);
      callbackWeakHashMap.put(currentCallback, ++count);
    }
  }

  @Override
  public void removeHost(Callback currentCallback) {
    if (callbackWeakHashMap == null) {
      return;
    }
    if (containsCallback(currentCallback)) {
      int count = callbackWeakHashMap.get(currentCallback);
      if (count <= 1) {
        callbackWeakHashMap.remove(currentCallback);
      } else {
        callbackWeakHashMap.put(currentCallback, --count);
      }
    }
  }

  private boolean containsCallback(Callback currentCallback) {
    return callbackWeakHashMap != null && callbackWeakHashMap.containsKey(currentCallback);
  }

  class CallBack implements Callback {

    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
      if (callbackWeakHashMap != null) {
        Set<Callback> set = callbackWeakHashMap.keySet();
        for (Callback callback : set) {
          callback.invalidateDrawable(who);

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