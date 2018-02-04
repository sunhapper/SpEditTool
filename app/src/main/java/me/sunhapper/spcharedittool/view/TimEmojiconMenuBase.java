package me.sunhapper.spcharedittool.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class TimEmojiconMenuBase extends LinearLayout {

  protected EaseEmojiconMenuListener listener;

  public TimEmojiconMenuBase(Context context) {
    super(context);
  }

  @SuppressLint("NewApi")
  public TimEmojiconMenuBase(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public TimEmojiconMenuBase(Context context, AttributeSet attrs) {
    super(context, attrs);
  }


  /**
   * 设置回调监听
   */
  public void setEmojiconMenuListener(EaseEmojiconMenuListener listener) {
    this.listener = listener;
  }

  public interface EaseEmojiconMenuListener {

    /**
     * 表情被点击
     */
    void onExpressionClicked(TimEmojicon emojicon);

    /**
     * 删除按钮被点击
     */
    void onDeleteImageClicked();
  }
}
