package me.sunhapper.spcharedittool.util;

import android.content.Context;

/**
 * Created by sunhapper on 2018/2/4.
 */

public class Utils {
  public static int dp2px(Context var0, float var1) {
    float var2 = var0.getResources().getDisplayMetrics().density;
    return (int) (var1 * var2 + 0.5F);
  }

  public static int px2dip(Context var0, float var1) {
    float var2 = var0.getResources().getDisplayMetrics().density;
    return (int) (var1 / var2 + 0.5F);
  }

  public static int sp2px(Context var0, float var1) {
    float var2 = var0.getResources().getDisplayMetrics().scaledDensity;
    return (int) (var1 * var2 + 0.5F);
  }

  public static int px2sp(Context var0, float var1) {
    float var2 = var0.getResources().getDisplayMetrics().scaledDensity;
    return (int) (var1 / var2 + 0.5F);
  }


}
