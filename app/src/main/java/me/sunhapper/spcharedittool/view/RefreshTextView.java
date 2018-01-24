package me.sunhapper.spcharedittool.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by sunha on 2017/4/12 0012.
 */

public class RefreshTextView extends AppCompatTextView {

  public RefreshTextView(Context context) {
    super(context);
    invalidateSelf();
  }

  private void invalidateSelf() {
    postInvalidate();
    postDelayed(new Runnable() {
      @Override
      public void run() {
        invalidateSelf();
      }
    }, 100);
  }

  public RefreshTextView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    invalidateSelf();
  }

  public RefreshTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    invalidateSelf();
  }


}
