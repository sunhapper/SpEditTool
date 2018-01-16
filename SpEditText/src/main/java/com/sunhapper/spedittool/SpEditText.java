package com.sunhapper.spedittool;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

public class SpEditText extends AppCompatEditText {

  private static String TAG = "SpEditText";
  private KeyReactListener mKeyReactListener;
  private char[] reactKeys;

  public SpEditText(Context context) {
    super(context);
    init();
  }

  public SpEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    if (isInEditMode()) {
      return;
    }
    init();
  }


  public void setReactKeys(String reactKeys) {
    this.reactKeys = reactKeys.toCharArray();
  }

  /**
   * 设置文本变化监听
   * 设置删除事件监听
   */
  private void init() {
    addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        for (Character character : reactKeys) {
          if (count == 1 && !TextUtils.isEmpty(charSequence)) {
            char mentionChar = charSequence.toString().charAt(start);
            if (character.equals(mentionChar) && mKeyReactListener != null) {
              mKeyReactListener.onKeyReact(character.toString());
              return;
            }
          }
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
//        resolveDeleteSpecialStr();
      }
    });

    setOnKeyListener(new OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
          int selectionStart = getSelectionStart();
          int selectionEnd = getSelectionEnd();
          SpData[] spDatas = getSpDatas();
          // 遍历判断光标的位置
          for (int i = 0; i < spDatas.length; i++) {
            SpData spData = spDatas[i];
            int rangeStart = spData.start;
            if (selectionStart == spData.end) {
              getEditableText().delete(rangeStart, selectionEnd);
              return true;
            }

          }
        }

        return false;
      }
    });
  }


  public SpData[] getSpDatas() {
    Editable editable = getText();
    SpData[] spanneds = editable.getSpans(0, getText().length(), SpData.class);
    if (spanneds != null && spanneds.length > 0) {
      for (SpData spData : spanneds) {
        int start = editable.getSpanStart(spData);
        int end = editable.getSpanEnd(spData);
        spData.setEnd(end);
        spData.setStart(start);
      }
      sortSpans(editable, spanneds, 0, spanneds.length - 1);
      return spanneds;
    } else {
      return new SpData[]{};
    }


  }

  private void sortSpans(Editable editable, SpData[] spanneds, int left, int right) {
    if (left >= right) {
      return;
    }
    int i = left;
    int j = right;
    SpData keySpan = spanneds[left];
    int key = editable.getSpanStart(spanneds[left]);
    while (i < j) {
      while (i < j && key <= editable.getSpanStart(spanneds[j])) {
        j--;
      }
      spanneds[i] = spanneds[j];
      while (i < j && key >= editable.getSpanStart(spanneds[i])) {
        i++;
      }

      spanneds[j] = spanneds[i];
    }

    spanneds[i] = keySpan;
    sortSpans(editable, spanneds, left, i - 1);
    sortSpans(editable, spanneds, i + 1, right);

  }

  /**
   * 监听光标位置,对插入的字符一起删除
   */
  @Override
  protected void onSelectionChanged(int selStart, int selEnd) {
    super.onSelectionChanged(selStart, selEnd);

    SpData[] spDatas = getSpDatas();
    for (int i = 0; i < spDatas.length; i++) {
      SpData spData = spDatas[i];
      int startPostion = spData.start;
      int endPostion = spData.end;
      if (changeSelection(selStart, selEnd, startPostion, endPostion, false)) {
        return;
      }
    }
  }

  /**
   * 不让光标进入特殊字符串内部
   */
  private boolean changeSelection(int selStart, int selEnd, int startPostion, int endPostion,
      boolean toEnd) {
    Log.i(TAG, "selStart: "+selStart+"   selEnd:"+selEnd+"   startPostion:"+startPostion+"   endPostion:"+endPostion);
    boolean hasChange = false;
    if (selStart == selEnd) {
      if (startPostion != -1 && startPostion < selStart && selStart < endPostion) {
        if (toEnd) {
          setSelection(endPostion);
        } else {
          setSelection(startPostion);
        }
        hasChange = true;
      }
    } else {
      if (startPostion != -1 && startPostion < selStart && selStart < endPostion) {
        if (toEnd) {
          setSelection(endPostion, selEnd);
        } else {
          setSelection(startPostion, selEnd);
        }

        hasChange = true;
      }
      if (endPostion != -1 && startPostion < selEnd && selEnd < endPostion) {
        setSelection(selStart, endPostion);
        hasChange = true;
      }
    }
    return hasChange;
  }


  public void insertSpecialStr(String showContent, boolean rollBack, Object customData,
      Object customSpan) {
    if (TextUtils.isEmpty(showContent)) {
      return;
    }
    //将特殊字符插入到EditText 中显示
    int index = getSelectionStart();
    Editable editable = getText();
    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(editable);
    SpData spData = new SpData();
    spData.setShowContent(showContent);
    spData.setCustomData(customData);
    SpannableString spannableString = new SpannableString(showContent);
    spannableString
        .setSpan(spData, 0, spannableString.length(),
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
    if (customSpan != null) {
      spannableString
          .setSpan(customSpan, 0, spannableString.length(),
              SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    if (rollBack) {
      spannableStringBuilder.delete(index - 1, index);
      index--;
    }
    spannableStringBuilder.insert(index, spannableString);
    setText(spannableStringBuilder);
    setSelection(index + spannableString.length());
  }


  public interface KeyReactListener {

    void onKeyReact(String key);
  }


  public void setmKeyReactListener(KeyReactListener mKeyReactListener) {
    this.mKeyReactListener = mKeyReactListener;
  }

  public class SpData {

    private String showContent;
    private Object customData;
    private int start;
    private int end;


    public int getStart() {
      return start;
    }

    private void setStart(int start) {
      this.start = start;
    }

    public int getEnd() {
      return end;
    }

    private void setEnd(int end) {
      this.end = end;
    }

    public Object getCustomData() {
      return customData;
    }

    private void setCustomData(Object customData) {
      this.customData = customData;
    }

    public String getShowContent() {
      return showContent;
    }

    private void setShowContent(String showContent) {
      this.showContent = showContent;
    }

    @Override
    public String toString() {
      return "SpData{" +
          "showContent='" + showContent + '\'' +
          ", customData=" + customData +
          ", start=" + start +
          ", end=" + end +
          '}';
    }
  }


}