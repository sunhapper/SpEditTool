# 介绍

输入@xxx #话题#等特殊字符，实现整体删除，文字高亮等功能的自定义EditText

## 截图

<img src="artworks/fullSp.gif" width = "240" height = "400" alt="ScreenShot"  /><img src="artworks/emojiSp.gif" width = "240" height = "400" alt="ScreenShot"  />

# 使用说明  

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

```
dependencies {
	        compile 'com.github.sunhapper:SpEditTool:0.1.5'
	}
```

## xml

```
<!--react_keys属性指定要响应的字符列表,也可以在java代码中用setReactKeys设置-->
<com.sunhapper.spedittool.view.SpEditText
    android:id="@+id/spEdt"
    app:react_keys="#*%@"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

## java  

* 设置要响应的字符列表及设置回调
```
//参数为String，会被解析成char[]
spEditText.setReactKeys("@#%*");
spEditText.setKeyReactListener(new KeyReactListener() {
      @Override
      public void onKeyReact(String key) {
        //key被响应的字符,长度为1
        //todo 处理自己的逻辑
      }
    });
```

* 插入特殊字符串
```
spEditText.insertSpecialStr(" @sunhapper ", true, 0, new ForegroundColorSpan(Color.RED));
```

```
  /**
   * 插入特殊字符串
   *
   * @param showContent 特殊字符串显示在文本框中的内容
   * @param rollBack 是否往前删除一个字符，因为@的时候可能留了一个字符在输入框里
   * @param customData 特殊字符串的数据结构
   * @param customSpan 特殊字符串的样式
   */
  public void insertSpecialStr(String showContent, boolean rollBack, Object customData,
      Object customSpan)
```

* 获取SpEditText的数据  

```
//获取到的数据是根据起点位置排过序的
SpData[] spDatas = spEditText.getSpDatas(); 
```

```
  public class SpData {

    /**
     * EditText中显示的内容
     */
    private String showContent;
    /**
     * 插入特殊字符串时传入的代表自定义数据结构的对象
     */
    private Object customData;
    /**
     * 特殊字符串的在完整字符串中的起点
     */
    private int start;
    /**
     * 特殊字符串的在完整字符串中的终点
     */
    private int end;
  }

```

# 彩蛋

`GifTextUtil.setText(TextView textView,CharSequence text)`
[一行代码让TextView中ImageSpan支持Gif](http://www.jianshu.com/p/3ae513115c17)

> 欢迎star，提PR、issue