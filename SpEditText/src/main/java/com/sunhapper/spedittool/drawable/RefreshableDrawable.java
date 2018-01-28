package com.sunhapper.spedittool.drawable;

import android.widget.TextView;

/**
 * Created by sunhapper on 2018/1/28.
 */

public interface RefreshableDrawable {
  boolean canRefresh();
  int getInterval();
  void addHost(TextView tv);
  void removeHost(TextView tv);
}
