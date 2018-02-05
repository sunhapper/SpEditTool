package me.sunhapper.spcharedittool.emoji;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class EmojiconMenuBase extends LinearLayout {

  protected EmojiconMenuListener listener;

  public EmojiconMenuBase(Context context) {
    super(context);
  }

  @SuppressLint("NewApi")
  public EmojiconMenuBase(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public EmojiconMenuBase(Context context, AttributeSet attrs) {
    super(context, attrs);
  }


  /**
   * 设置回调监听
   */
  public void setEmojiconMenuListener(EmojiconMenuListener listener) {
    this.listener = listener;
  }

  public interface EmojiconMenuListener {

    void onExpressionClicked(Emoji emoji);
  }
}
