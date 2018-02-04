package me.sunhapper.spcharedittool.emoji;

import android.graphics.drawable.Drawable;

/**
 * Created by sunhapper on 2018/2/4.
 */

public interface Emoji {

  Drawable getCachedDrawable();

  CharSequence getEmojiText();

  Object getRes();

  int getResType();

  boolean isDeleteIcon();

}
