package me.sunhapper.spcharedittool.emoji;

import android.content.Context;
import android.graphics.drawable.Drawable;
import java.io.File;
import java.io.IOException;
import me.sunhapper.spcharedittool.R;
import me.sunhapper.spcharedittool.drawable.RefreshGifDrawable;

public class DefaultGifEmoji implements Emoji {

  private String emojiText;
  private File emojiconFile;

  public DefaultGifEmoji(File emojiconFile, String emojiText) {
    this.emojiconFile = emojiconFile;
    this.emojiText = emojiText;
  }


  @Override
  public Drawable getDrawable(Context context) {
    try {
      return new RefreshGifDrawable(emojiconFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return context.getResources().getDrawable(getDefaultResId());
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

  @Override
  public Object getCacheKey() {
    if (emojiconFile.exists()) {
      return emojiconFile.getAbsolutePath();
    } else {
      return R.drawable.common_emoj_smile_default;
    }
  }


}
