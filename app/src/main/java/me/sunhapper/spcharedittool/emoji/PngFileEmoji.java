package me.sunhapper.spcharedittool.emoji;

import android.graphics.drawable.Drawable;
import java.io.File;

public class PngFileEmoji implements Emoji {

  private boolean isDeleteIcon = false;
  private String emojiText;
  private File pngFile;

  public PngFileEmoji() {
    isDeleteIcon=true;
  }

  public PngFileEmoji(File iconPng, String emojiText) {
    this(iconPng, emojiText, false);
  }

  public PngFileEmoji(File iconPng, String emojiText, boolean isDeleteIcon) {
    this.pngFile = iconPng;
    this.emojiText = emojiText;
    this.isDeleteIcon = isDeleteIcon;
  }


  @Override
  public Drawable getCachedDrawable() {
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
    return isDeleteIcon;
  }


}
