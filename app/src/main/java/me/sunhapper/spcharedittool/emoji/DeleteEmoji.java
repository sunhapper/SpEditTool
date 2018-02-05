package me.sunhapper.spcharedittool.emoji;

import android.graphics.drawable.Drawable;
import java.io.File;
import me.sunhapper.spcharedittool.R;

public class DeleteEmoji implements Emoji {

  private String emojiText;
  private File pngFile;

  public DeleteEmoji() {

  }


  public DeleteEmoji(File iconPng, String emojiText) {
    this.pngFile = iconPng;
    this.emojiText = emojiText;
  }


  @Override
  public Drawable getDrawable() {
    return null;
  }

  public CharSequence getEmojiText() {
    return emojiText;
  }

  @Override
  public Object getRes() {
    return pngFile;
  }

  @Override
  public int getResType() {
    return 0;
  }

  @Override
  public boolean isDeleteIcon() {
    return true;
  }

  @Override
  public int getDefaultResId() {
    return R.drawable.common_emoj_delete_expression;
  }


}
