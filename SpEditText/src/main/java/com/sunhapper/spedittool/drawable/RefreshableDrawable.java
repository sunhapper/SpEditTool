package com.sunhapper.spedittool.drawable;

import android.graphics.drawable.Drawable;

/**
 * Created by sunhapper on 2018/1/28.
 */

public interface RefreshableDrawable {

  boolean canRefresh();

  int getInterval();

  void addCallback(Drawable.Callback callback);

  void removeCallback(Drawable.Callback callback);

}
