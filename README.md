# Description

* `SpEditText`--An EditText for mentioning user and showing topic with highlight and overall deleting
* `GifTextUtil`--A tool for show gif on graph-text mixed TextView/EditText efficiently

## ScreenShot

<img src="artworks/fullSp.gif" width = "240" height = "400" alt="ScreenShot"  /><img src="artworks/emojiSp.gif" width = "240" height = "400" alt="ScreenShot"  />

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
	        compile 'com.github.sunhapper:SpEditTool:{last version}'
	}
```

## xml

```
<com.sunhapper.spedittool.view.SpEditText
    android:id="@+id/spEdt"
    app:react_keys="#*%@"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

## java

### SpEditText

* react the character input
```
spEditText.setReactKeys("@#%*");
spEditText.setKeyReactListener(new KeyReactListener() {
      @Override
      public void onKeyReact(String key) {

      }
    });
```

* insert special content
```
spEditText.insertSpecialStr(" @sunhapper ", true, 0, new ForegroundColorSpan(Color.RED));
```

```
  /**
   *
   * @param showContent special text content
   * @param rollBack true is need to delete on char forward
   * @param customData special text`s extra data
   * @param customSpan special text`s style
   */
  public void insertSpecialStr(String showContent, boolean rollBack, Object customData,
      Object customSpan)
```

* get data from SpEditText

```
SpData[] spDatas = spEditText.getSpDatas(); 
```

```
  public class SpData {

    /**
     * special text content
     */
    private String showContent;
    /**
     * special text`s extra data
     */
    private Object customData;
    /**
     * special text`s start on EditText
     */
    private int start;
    /**
     * special text`s end on EditText
     */
    private int end;
  }

```

### GifTextUtil

define a custom gifDrawable extend Drawable and implements RefreshableDrawable
```
public interface RefreshableDrawable {

  boolean canRefresh();

  int getInterval();

  void addCallback(Drawable.Callback callback);

  void removeCallback(Drawable.Callback callback);

}
```

bind spannable with gifDrawable to textView
```
GifTextUtil.setTextWithReuseDrawable(textView, charSequence, false);

```

## proguard

```
-keep class com.sunhapper.spedittool.**{*;}
```

more detail info and guidance，please see the demo

[中文文档](./README_CN.md)

> welcome to star，PR and issue