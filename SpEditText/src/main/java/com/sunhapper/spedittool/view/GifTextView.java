package com.sunhapper.spedittool.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by sunhapper on 2018/1/21.
 */

public class GifTextView extends AppCompatTextView {

  private static final String TAG = "GifTextView";
  public GifTextView(Context context) {
    super(context);
  }

  public GifTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public GifTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public void invalidateDrawable(@NonNull Drawable drawable) {
    super.invalidateDrawable(drawable);
    Log.i(TAG, "invalidateDrawable: ");
  }

  @Override
  public void setText(CharSequence text, BufferType type) {
    super.setText(text, type);
  }


}
