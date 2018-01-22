# 介绍
输入@xxx #话题#等特殊字符，实现整体删除，文字高亮等功能的自定义EditText

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
	        compile 'com.github.sunhapper:SpEditTool:0.1.3'
	}
```

## xml

```
  <com.sunhapper.spedittool.view.SpEditText
    android:id="@+id/spEdt"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

## java  

```
//设置要响应的字符，参数为String，会被解析成char[]
spEditText.setReactKeys("@#%*");
//输入要响应的字符串的回调
spEditText.setKeyReactListener(new KeyReactListener() {
      @Override
      public void onKeyReact(String key) {
        switch (key) {
          case "@":
            spEditText
                .insertSpecialStr(" @sunhapper ", true, 0, new ForegroundColorSpan(Color.RED));
            break;
          case "#":
            spEditText.insertSpecialStr(" #这是一个话题# ", true, 1, new ForegroundColorSpan(Color.RED));
            break;
          case "%":
            spEditText.insertSpecialStr(" 100% ", true, 2, new ForegroundColorSpan(Color.RED));
          case "*":
            spEditText.insertSpecialStr(" ******* ", true, 3, new ForegroundColorSpan(Color.RED));
            break;
        }
//插入特殊字符串，参数分别为：字符串的显示内容；是否回删一个字符（被响应的字符会保留在文本框中，调用者需要自己处理是否将其删除）；自定义的数据结构；特殊字符串的显示样式
spEditText.insertSpecialStr(" @sunhapper ", true, 0, new ForegroundColorSpan(Color.RED));
```


