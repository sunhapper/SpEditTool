
开发聊天功能，需要在群聊中实现@xxx功能，网上没有找到现成的东西可以直接拿来用的，那就自己撸一个好了

> 项目地址[https://github.com/sunhapper/SpEditTool](https://github.com/sunhapper/SpEditTool)

## demo截图
<img src="artworks/fullSp.gif" width = "240" height = "400" alt="ScreenShot"  />

## 功能分析

* 可以插入@xxx这样的特殊字符串
* 需要有高亮等效果
* 特殊字符串作为一个整体，要一起删除，光标不能进入特殊字符串内部
* 特殊字符串应当对应一个自定义的数据结构保存@的对象的id，名字等信息

## 实现思路

### 继承EditText

本来不想使用继承这样侵入的方式去实现，但是需要监听光标的变化，而sdk并没有提供设置光标监听的方法。

### 记录特殊字符串的位置和代表的信息

这个是实现功能的关键点，总结了下网上的方案
> [MentionEditText](https://github.com/luckyandyzhang/MentionEditText)

这个库中使用了正则表达式去匹配字符串中的特殊字符串，而且必须严格的@开头空格结尾，这种方式对于特殊字符串中间带@或者空格的的情况无法处理，对只想把@视为普通字符的情况也无法处理

> [RichEditor](https://github.com/JustYJQ/RichEditor)

这个库自己维护了一个List，记录了特殊字符串的内容，在删除或者光标变化时遍历这个List判断光标是否处在特殊字符串的位置
最初自己咋一看觉得可以满足需求，在List的元素中加一个字段就可以记录@xxx的数据结构了，但是简单用了之后发现一个很严重的问题：像@11 @1这样前面是相同内容的字符串处理的时候遍历算出的位置是不对的，而且很容易触发setSelection的递归调用导致StackOverflow

> [SpEditTool](https://github.com/sunhapper/SpEditTool)

自己写的库，容我自卖自夸一下
这里利用了Spannable的setSpan方法为对应的特殊字符串设置一个Object作为标记，好处有这么两点

*  这个标记的位置是由EditText中的Editable对象来维护的，插入字符，删除特殊字符串位置自动就会变化，虽然偷懒，但是效果不错
*  因为标记和特殊字符串是一一对应的，所以无论文本框的内容如何变化都不用担心匹配出错

主要代码：

```
 /**
   * 插入特殊字符串，提供给外部调用
   * @param showContent 特殊字符串显示在文本框中的内容
   * @param rollBack 是否往前删除一个字符，因为@的时候可能留了一个字符在输入框里
   * @param customData 特殊字符串的数据结构
   * @param customSpan 特殊字符串的样式
   */
  public void insertSpecialStr(String showContent, boolean rollBack, Object customData,
      Object customSpan) {
    if (TextUtils.isEmpty(showContent)) {
      return;
    }
    int index = getSelectionStart();
    Editable editable = getText();
    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(editable);
    //SpData中保存了显示内容和对应数据结构
    SpData spData = new SpData();
    spData.setShowContent(showContent);
    spData.setCustomData(customData);
    SpannableString spannableString = new SpannableString(showContent);
    spannableString
        .setSpan(spData, 0, spannableString.length(),
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
    //设置自定义样式
    if (customSpan != null) {
      spannableString
          .setSpan(customSpan, 0, spannableString.length(),
              SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    //是否回删一个字符
    if (rollBack) {
      spannableStringBuilder.delete(index - 1, index);
      index--;
    }
    spannableStringBuilder.insert(index, spannableString);
    setText(spannableStringBuilder);
    //将光标置到插入内容末尾
    setSelection(index + spannableString.length());
  }
```




### 获取插入的特殊字符串

使用Spanned接口的getSpans方法

```
 public SpData[] getSpDatas() {
    Editable editable = getText();
    SpData[] spanneds = editable.getSpans(0, getText().length(), SpData.class);
    if (spanneds != null && spanneds.length > 0) {
      for (SpData spData : spanneds) {
        int start = editable.getSpanStart(spData);
        int end = editable.getSpanEnd(spData);
        //设置当前特殊字符串的起止位置
        spData.setEnd(end);
        spData.setStart(start);
      }
      sortSpans(editable, spanneds, 0, spanneds.length - 1);//获取到的数据可能是没排过序的，所以快排排个序再返回
      return spanneds;
    } else {
      return new SpData[]{};
    }
  }
```

### 监听光标改变

覆盖onSelectionChanged方法

```
/**
   * 监听光标位置,对插入的特殊字符一起删除
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
```

### 监听删除事件

使用EditText的setOnKeyListener，监听删除事件，如果碰到特殊字符串整体删除

```
 setOnKeyListener(new OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
        return onDeleteEvent();
        }

        return false;
      }
    });
```

```
 private boolean onDeleteEvent() {
    int selectionStart = getSelectionStart();
    int selectionEnd = getSelectionEnd();
    if (selectionEnd!=selectionStart){
      return false;
    }
    SpData[] spDatas = getSpDatas();
    for (int i = 0; i < spDatas.length; i++) {
      SpData spData = spDatas[i];
      int rangeStart = spData.start;
      if (selectionStart == spData.end) {
        getEditableText().delete(rangeStart, selectionEnd);
        return true;
      }

    }
    return false;
  }
```

### 响应文本框中@的输入

EditText可以添加一个TextWatcher监听文本的变化（并不是必要的，可以自己在外部处理）

```
addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        //reactKeys是需要响应的字符列表，不仅仅可以响应@
        for (Character character : reactKeys) {
          if (count == 1 && !TextUtils.isEmpty(charSequence)) {
            char mentionChar = charSequence.toString().charAt(start);
            if (character.equals(mentionChar) && mKeyReactListener != null) {
             handKeyReactEvent(character);//在EditText内部，所以用回调的方式通知外部有特殊的字符被输入
              return;
            }
          }
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    })；
```
```
  private void handKeyReactEvent(final Character character) {
    post(new Runnable() {
      @Override
      public void run() {
        mKeyReactListener.onKeyReact(character.toString());
      }
    });
  }
```

#### Tips:post(Runnable runnabe)

在`onTextChanged`中使用`post(Runnable runnabe)`去调用外部回调,是因为在onTextChanged执行时,最初插入的@等字符的`onSelectionChanged`回调还没走

假设输入了`@`,不使用`post(Runnable runnabe)`,直接调用`onKeyReact`,在回调中插入`@sunhapper`字符串并设置光标位置,onSelectionChanged调用顺序为`onSelectionChanged(10,10)`-->`onSelectionChanged(1,1)`导致光标位置位于插入字符串前面而不是后面,不符合预期

使用post(Runnable runnabe)可以让当前线程的代码执行完再去调用`onKeyReact`,onSelectionChanged调用顺序为`onSelectionChanged(1,1)`-->`onSelectionChanged(10,10)`,光标位置符合预期

## 总结

* 继承EditText
* 利用setSpan方法将自定义的数据结构和样式和插入的文本绑定
* 利用getSpans方法获取插入的数据
* 监听光标变化，主动改变光标位置，防止光标进入特殊字符串内部
* 监听删除事件，对特殊字符串整体删除

完成以上几步，一个支持插入@ #话题#等各种要高亮要整体删除的EditText就完成了

> 欢迎大家使用已有的轮子<br>
> 项目地址[https://github.com/sunhapper/SpEditTool](https://github.com/sunhapper/SpEditTool)<br>
> 欢迎star，提PR、issue