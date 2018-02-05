package me.sunhapper.spcharedittool.emoji;

import android.graphics.drawable.Drawable;
import java.io.File;
import me.sunhapper.spcharedittool.R;

public class DefaultGifEmoji implements Emoji {

  private String emojiText;
  private File emojiconFile;

  public DefaultGifEmoji(File emojiconFile, String emojiText) {
    this.emojiconFile = emojiconFile;
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
    return emojiconFile;
  }

  @Override
  public int getResType() {
    return 0;
  }

  @Override
  public boolean isDeleteIcon() {
    return false;
  }

  @Override
  public int getDefaultResId() {
    return R.drawable.common_emoj_smile_default;
  }


}
