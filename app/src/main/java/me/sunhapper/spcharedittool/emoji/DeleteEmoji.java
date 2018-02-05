package me.sunhapper.spcharedittool.emoji;

import android.content.Context;
import android.graphics.drawable.Drawable;
import me.sunhapper.spcharedittool.R;

public class DeleteEmoji implements Emoji {


  public DeleteEmoji() {

  }


  @Override
  public Drawable getDrawable(Context context) {
    return context.getResources().getDrawable(getDefaultResId());
  }

  public CharSequence getEmojiText() {
    return "";
  }

  @Override
  public Object getRes() {
    return R.drawable.common_emoj_delete_expression;
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

  @Override
  public Object getCacheKey() {
    return R.drawable.common_emoj_delete_expression;
  }


}
