package com.sunhapper.spedittool.drawable;

import android.graphics.drawable.Drawable;

/**
 * Created by sunhapper on 2018/1/28.
 */

public interface RefreshableDrawable {

  boolean canRefresh();

  int getInterval();

  void addHost(Drawable.Callback callback);

  void removeHost(Drawable.Callback callback);

}
