# Description

一个高效可扩展，在TextView/EditText中输入和显示gif和@mention等图文混排内容的库


## ScreenShot

<img src="artworks/fullSp.gif" width = "240" height = "400" alt="ScreenShot"  /><img src="artworks/emojiSp.gif" width = "240" height = "400" alt="ScreenShot"  />

## feature

- [x] 整体删除的特殊内容
- [x] 部分删除的特殊内容
- [x] 自定义样式 
- [x] gif图文混排
- [x] 使用AndroidGifDrawable加载gif
- [x] 使用Glide加载gif

## todo 

- [ ] 使用Fresco加载gif
- [ ] 更多的ImageSpan样式

## change log  

* 1.0.1/1.0.2
    * 修复bug 

* 1.0.0 
    * @mention功能使用更优雅的实现方式 （特别感谢 [iYaoy](https://github.com/iYaoy/easy_at)的思路）
    * 更清晰的包结构
    * 支持Glide/AndroidGifDrawable的库

# Usage

## Gradle  

* root build.gradle

```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
* dependency

[![](https://jitpack.io/v/sunhapper/SpEditTool.svg)](https://jitpack.io/#sunhapper/SpEditTool)
```
dependencies {
	        implementation 'com.github.sunhapper:SpEditTool:SpEditText:{last version}'
	        //help to create gif drawable use Glide
	        implementation 'com.github.sunhapper:SpEditTool:SpGlideDrawable:{last version}'
	        //help to create gif drawable use AndroidGifDrawable
	        implementation 'com.github.sunhapper:SpEditTool:SpGifDrawable:{last version}'
	}
```

## xml

EditText
* CustomInputConnectionWrapper 正确接收keyEvent，使用谷歌输入法时setOnKeyListener设置的回调不起作用
* SpanChangedWatcher 处理@mention的内容
* GifWatcher 处理gif的刷新
```
<com.sunhapper.x.spedit.view.SpXEditText
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```
TextView
```
<com.sunhapper.x.spedit.view.SpXTextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

## java

* 创建 @mention Spannable
```
//IntegratedSpan 完整性不会破坏，删除时整个内容全部删除
public class MentionUser implements IntegratedSpan {
    public String name;
    public long id;

    public Spannable getSpanableString() {
        SpannableString spannableString = new SpannableString(getDisplayText());
        spannableString.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(this, 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        return stringBuilder.append(spannableString).append(" ");
    }
}
```


* 当BreakableSpan完整性被破坏时删除样式Span
```
//BreakableSpan 完整性可以被破坏
public class Topic implements BreakableSpan {

    ...
    /**
     * @return true the BreakableSpan will be removed
     * you can remove custom style span when content broken
     */
    @Override
    public boolean isBreak(Spannable text) {
        int spanStart = text.getSpanStart(this);
        int spanEnd = text.getSpanEnd(this);
        boolean isBreak = spanStart >= 0 && spanEnd >= 0 && !text.subSequence(spanStart, spanEnd).toString().equals(
                getDisplayText());
        if (isBreak && styleSpan != null) {
            text.removeSpan(styleSpan);
            styleSpan = null;
        }
        return isBreak;
    }
}
```

* 使用AndroidGifDrawable创建包含gif的Spannable对象
```
Drawable drawable = new TextGifDrawable(emojiconFile);
Spannable spannable = SpUtil.createGifDrawableSpan(gifDrawable,"text");
```

* 使用Glide创建包含gif的Spannable对象
```
//placeholder drawable
GifDrawable gifDrawable = new TextGifDrawable(getResources(), R.drawable.a);
ProxyDrawable proxyDrawable = new ProxyDrawable();
GlideApp.with(this)
        .load(gifurl)
        .placeholder(gifDrawable)
        .into(new DrawableTarget(proxyDrawable));
return SpUtil.createResizeGifDrawableSpan(proxyDrawable, "text");
```

* 自定义实现  
    * 你可以创建自己的 `SpanWatcher` 和 `Spannable.Factory/Editable.Factory `
    * 使用`setSpannableFactory/setEditableFactory`实现功能而不用`SpXTextView` 或者 `SpXEditText`

## proguard

```
-keep class com.sunhapper.x.spedit.**{*;}
```

更多详细信息请看demo或者本人简书[https://www.jianshu.com/u/e173cf3c1f1c](https://www.jianshu.com/u/e173cf3c1f1c)


> 欢迎star，提PR、issue