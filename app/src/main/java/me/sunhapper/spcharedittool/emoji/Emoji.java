package me.sunhapper.spcharedittool.emoji;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by sunhapper on 2018/2/4.
 */

public interface Emoji {

  Drawable getDrawable(Context context);

  CharSequence getEmojiText();

  Object getRes();

  int getResType();

  boolean isDeleteIcon();

  int getDefaultResId();

  Object getCacheKey();

}
