package com.sunhapper.spedittool.span;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.sunhapper.spedittool.R;
import com.sunhapper.spedittool.drawable.RefreshableDrawable;

/**
 * Created by sunhapper on 2018/1/21.
 */

public class GifTextUtil {

  public static void setText(final TextView textView, final CharSequence text) {
    //需要SPANNABLE，保证textView中取出来的是SpannableString
    setText(textView, text, BufferType.SPANNABLE);
  }


  public static void setTextWithReuseDrawable(final TextView textView, final CharSequence text) {
    //需要SPANNABLE，保证textView中取出来的是SpannableString
    setTextWithReuseDrawable(textView, text, BufferType.SPANNABLE);
  }

  public static void setTextWithReuseDrawable(final TextView textView, final CharSequence nText,
      final BufferType type) {
    CharSequence oldText="";
    try {
      //EditText第一次获取到的是个空字符串，会强转成Editable，出现ClassCastException
      oldText= textView.getText();
    }catch (ClassCastException e){
      e.fillInStackTrace();
    }
    if (oldText instanceof Spannable){
      ImageSpan[] gifSpans = ((Spannable) oldText).getSpans(0, oldText.length(), ImageSpan.class);
      for (ImageSpan gifSpan : gifSpans) {
        Drawable drawable = gifSpan.getDrawable();
        if (drawable != null&&drawable instanceof RefreshableDrawable) {
          ((RefreshableDrawable) drawable).removeHost(textView);
        }
      }
    }

    textView.setText(nText, type);
    CharSequence text = textView.getText();

    if (text instanceof Spannable) {
      RefreshableDrawable temp=null;
      int tempInterval=0;
      ImageSpan[] gifSpans = ((Spannable) text).getSpans(0, text.length(), ImageSpan.class);
      for (ImageSpan gifSpan : gifSpans) {
        Drawable drawable = gifSpan.getDrawable();
        if (drawable != null&&drawable instanceof RefreshableDrawable) {
          if (((RefreshableDrawable) drawable).canRefresh()){
            if (tempInterval<((RefreshableDrawable) drawable).getInterval()){
              temp= (RefreshableDrawable) drawable;
            }
          }
        }
      }
      if (temp!=null){
        temp.addHost(textView);
      }
    }
    textView.invalidate();

  }


  public static void setText(final TextView textView, final CharSequence nText,
      final BufferType type) {
    //type 默认SPANNABLE，保证textView中取出来的是Spannable类型
    textView.setText(nText, type);
    CharSequence text = textView.getText();
    if (text instanceof Spannable) {
      ImageSpan[] gifSpans = ((Spannable) text).getSpans(0, text.length(), ImageSpan.class);
      //将之前的Callback disable
      Object oldCallback = textView
          .getTag(R.id.drawable_callback_tag);
      if (oldCallback != null && oldCallback instanceof CallbackForTextView) {
        ((CallbackForTextView) oldCallback).disable();
      }
      Callback callback = new CallbackForTextView(textView);
      //callback在drawable中是弱引用
      //让textview持有callback,防止callback被回收
      textView.setTag(R.id.drawable_callback_tag, callback);
      for (ImageSpan gifSpan : gifSpans) {
        Drawable drawable = gifSpan.getDrawable();
        if (drawable != null) {
          drawable.setCallback(callback);
        }
      }
      //gifSpanWatcher是SpanWatcher,继承自NoCopySpan
      //只有setText之后设置SpanWatcher才能成功
      ((Spannable) text).setSpan(gifSpanWatcher, 0, text.length(),
          Spanned.SPAN_INCLUSIVE_INCLUSIVE | Spanned.SPAN_PRIORITY);

    }
    textView.invalidate();
  }


  public static class CallbackForTextView implements Callback {

    private boolean enable = true;
    private long lastInvalidateTime;
    private TextView textView;

    public CallbackForTextView(TextView textView) {
      this.textView = textView;
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
      if (!enable) {
        return;
      }
      if (System.currentTimeMillis() - lastInvalidateTime > 40) {
        lastInvalidateTime = System.currentTimeMillis();
        textView.invalidate();
      }
    }

    @Override
    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {

    }

    @Override
    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {

    }

    public void disable() {
      this.enable = false;
    }
  }

  private static GifSpanWatcher gifSpanWatcher = new GifSpanWatcher();
}
