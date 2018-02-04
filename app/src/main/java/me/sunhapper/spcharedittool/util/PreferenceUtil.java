package me.sunhapper.spcharedittool.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sunhapper on 2018/2/3.
 */

public class PreferenceUtil {
  public static final String TAG ="sp_data";
  public static final String EMOJI_INIT ="emoji_init";
  public static boolean getEmojiInitResult(Context context) {
    SharedPreferences pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    return pref.getBoolean(EMOJI_INIT, false);
  }

  public static void setEmojiInitResult(Context context,boolean  initResult) {
    SharedPreferences pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    pref.edit().putBoolean(EMOJI_INIT, initResult).apply();
  }
}
