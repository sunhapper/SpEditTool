/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.sunhapper.spcharedittool.emoji;

import android.content.Context;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.TextView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.sunhapper.spcharedittool.span.GifAlignCenterSpan;
import me.sunhapper.spcharedittool.util.FileUtil;
import pl.droidsonroids.gif.GifDrawable;

public class EmojiManager {

  private final String TAG = "EmojManager";
  public static final String DELETE_KEY = "em_delete_delete_expression";

  private final Map<Pattern, Object> emoticons = new HashMap<>();
  private HashMap<String, GifDrawable> gifDrawableHashMap = new HashMap<>();

  private volatile static EmojiManager instance;


  private static String[] emojiList = new String[]{
      "[微笑]", "[撇嘴]", "[色]", "[发呆]", "[得意]", "[流泪]", "[害羞]",
      "[闭嘴]", "[睡]", "[大哭]", "[尴尬]", "[发怒]", "[调皮]", "[龇牙]",
      "[惊讶]", "[难过]", "[酷]", "[冷汗]", "[抓狂]", "[吐]",

      "[偷笑]", "[可爱]", "[白眼]", "[傲慢]", "[饥饿]", "[困]", "[惊恐]",
      "[流汗]", "[憨笑]", "[大兵]", "[奋斗]", "[咒骂]", "[疑问]", "[嘘…]",
      "[晕]", "[折磨]", "[衰]", "[骷髅]", "[敲打]", "[再见]",

      "[擦汗]", "[抠鼻]", "[鼓掌]", "[糗大了]", "[坏笑]", "[左哼哼]", "[右哼哼]",
      "[哈欠]", "[鄙视]", "[委屈]", "[快哭了]", "[阴险]", "[亲亲]", "[吓]",
      "[可怜]", "[菜刀]", "[西瓜]", "[啤酒]", "[篮球]", "[乒乓球]",

      "[咖啡]", "[饭]", "[猪头]", "[玫瑰]", "[凋谢]", "[示爱]", "[爱心]",
      "[心碎]", "[蛋糕]", "[闪电]", "[炸弹]", "[刀]", "[足球]", "[瓢虫]",
      "[便便]", "[月亮]", "[太阳]", "[礼物]", "[拥抱]", "[强]",

      "[弱]", "[握手]", "[胜利]", "[抱拳]", "[勾引]", "[拳头]", "[差劲]",
      "[爱你]", "[NO]", "[OK]", "[爱情]", "[飞吻]", "[跳跳]", "[发抖]",
      "[怄火]", "[转圈]", "[磕头]", "[回头]", "[跳绳]", "[挥手]"
  };
  private static List<File> emojiPngs = new ArrayList<>();
  private static List<File> emojiGifs = new ArrayList<>();


  private EmojiManager() {
  }

  public static EmojiManager getInstance() {
    if (instance == null) {
      synchronized (EmojiManager.class) {
        if (instance == null) {
          instance = new EmojiManager();
        }
      }
    }
    return instance;
  }

  public void init(Context context) {

    for (int i = 1; i < 6; i++) {
      for (int j = 0; j < 3; j++) {
        for (int k = 0; k < 7; k++) {
          if (j == 2 && k == 6) {
            continue;
          }
          File pngFile;
          File gifFile;

          pngFile = new File(FileUtil.getEmojiDir(context),
              "/emoji/e" + i + "/" + k + "_" + j + ".png");
          gifFile = new File(FileUtil.getEmojiDir(context),
              "/emoji/e" + i + "/" + k + "_" + j + ".gif");
          emojiPngs.add(pngFile);
          emojiGifs.add(gifFile);
        }
      }
    }
    for (int i = 0; i < emojiList.length; i++) {
      addPattern(emojiList[i], new IconSet(emojiGifs.get(i), emojiPngs.get(i)));
    }
  }


  private void addPattern(String emojiText, Object res) {
    emoticons.put(Pattern.compile(Pattern.quote(emojiText)), res);
  }


  /**
   * replace existing spannable with smiles
   */
  public boolean addSmiles(Context context, Spannable spannable) {
    boolean hasChanges = false;
    for (Entry<Pattern, Object> entry : emoticons.entrySet()) {
      Matcher matcher = entry.getKey().matcher(spannable);
      while (matcher.find()) {
        boolean set = true;
        for (ImageSpan span : spannable.getSpans(matcher.start(),
            matcher.end(), ImageSpan.class)) {
          if (spannable.getSpanStart(span) >= matcher.start()
              && spannable.getSpanEnd(span) <= matcher.end()) {
            spannable.removeSpan(span);
          } else {
            set = false;
            break;
          }
        }
        if (set) {
          hasChanges = true;
          Object value = entry.getValue();
          if (value instanceof String && !((String) value).startsWith("http")) {
            File file = new File((String) value);
            if (!file.exists() || file.isDirectory()) {
              return false;
            }
            spannable.setSpan(new ImageSpan(context, Uri.fromFile(file)),
                matcher.start(), matcher.end(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
          } else if (value instanceof IconSet) {
            IconSet iconSet = (IconSet) value;
            if (iconSet.iconPng.exists()) {
              ImageSpan imageSpan = new GifAlignCenterSpan(context,
                  Uri.fromFile(((IconSet) value).iconPng));
              spannable.setSpan(imageSpan,
                  matcher.start(), matcher.end(),
                  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
              return false;
            }
          } else {
            spannable.setSpan(new ImageSpan(context, (Integer) value),
                matcher.start(), matcher.end(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
          }
        }
      }
    }

    return hasChanges;
  }


  public Spannable getGifSmiles(CharSequence text, TextView textView) {
    Spannable spannable = new SpannableString(text);
    for (Entry<Pattern, Object> entry : emoticons.entrySet()) {
      Matcher matcher = entry.getKey().matcher(spannable);
      while (matcher.find()) {
        boolean set = true;
        for (ImageSpan span : spannable.getSpans(matcher.start(),
            matcher.end(), ImageSpan.class)) {
          if (spannable.getSpanStart(span) >= matcher.start()
              && spannable.getSpanEnd(span) <= matcher.end()) {
            spannable.removeSpan(span);
          } else {
            set = false;
            break;
          }
        }
        if (set) {
          Object value = entry.getValue();
          if (value instanceof IconSet) {
//                        Drawable gifDrawable = ImageTextUtil.getUrlDrawable(((IconSet) value).iconGif.getAbsolutePath(), textView);
//                        Drawable gifDrawable = imageGetter.getDrawable(((IconSet) value).iconGif.getAbsolutePath());
            try {
              ImageSpan imageSpan;
              GifDrawable gifDrawable1;
              if (gifDrawableHashMap.containsKey(((IconSet) value).iconGif.getAbsolutePath())) {
                gifDrawable1 = gifDrawableHashMap.get(((IconSet) value).iconGif.getAbsolutePath());
                imageSpan = new GifAlignCenterSpan(gifDrawable1);

              } else {
                gifDrawable1 = new GifDrawable(((IconSet) value).iconGif);
//                            gifDrawable1.start();
                imageSpan = new GifAlignCenterSpan(gifDrawable1);
                gifDrawableHashMap.put(((IconSet) value).iconGif.getAbsolutePath(), gifDrawable1);
              }
              spannable.setSpan(imageSpan,
                  matcher.start(), matcher.end(),
                  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (IOException e) {
              e.printStackTrace();
            }


          }
        }
      }
    }
    return spannable;
  }


  public  Emojicon[] createData(Context context) {
    init(context);
    Emojicon[] datas = new Emojicon[emojiList.length];
    for (int i = 0; i < emojiList.length; i++) {
      datas[i] = new Emojicon(emojiPngs.get(i), emojiGifs.get(i), emojiList[i]);
    }
    return datas;
  }

  public Spannable getPngSmiledText(Context context, CharSequence text) {
    Spannable spannable = new SpannableString(text);
//        addHttp(context, spannable);
    addSmiles(context, spannable);
    return spannable;
  }


}
