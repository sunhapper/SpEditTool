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
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.sunhapper.spcharedittool.span.EqualHeightSpan;
import me.sunhapper.spcharedittool.util.FileUtil;
import me.sunhapper.spcharedittool.util.PreferenceUtil;

public class EmojiManager {

  private static final String TAG = "EmojiManager";
  private static final int UNZIP_SUCCESS = 11111;
  private final Map<Pattern, Emoji> emoticons = new HashMap<>();
  private HashMap<Object, Drawable> drawableCacheMap = new HashMap<>();

  private volatile static EmojiManager instance;
  private static List<File> emojiGifs = new ArrayList<>();
  private DefaultGifEmoji[] defaultGifEmojis;
  private boolean defaultEmojiInited = false;
  private Handler handler;
  private List<OnUnzipSuccessListener> onUnzipSuccessListeners = new ArrayList<>();

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


  private EmojiManager() {
    handler = new Handler(new Callback() {
      @Override
      public boolean handleMessage(Message msg) {
        if (msg.what == UNZIP_SUCCESS) {
          onUnzipSuccess();
          return true;
        }
        return false;
      }
    });
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

  public void getDefaultEmojiData(OnUnzipSuccessListener unzipSuccessListener) {
    if (defaultEmojiInited) {
      unzipSuccessListener.onUnzipSuccess(defaultGifEmojis);
    } else {
      onUnzipSuccessListeners.add(unzipSuccessListener);
    }
  }

  public void init(final Context context) {
    boolean unziped = PreferenceUtil.getEmojiInitResult(context);
    if (unziped) {
      initDefault(context);
    } else {
      new Thread(new Runnable() {
        @Override
        public void run() {
          boolean unzipResult = FileUtil
              .unzipFromAssets(context, FileUtil.getEmojiDir(context), "emoji");
          PreferenceUtil.setEmojiInitResult(context, unzipResult);
          if (unzipResult) {
            initDefault(context);
          }
        }
      }).start();
    }
  }

  private void initDefault(Context context) {
    for (int i = 1; i < 6; i++) {
      for (int j = 0; j < 3; j++) {
        for (int k = 0; k < 7; k++) {
          if (j == 2 && k == 6) {
            continue;
          }
          File gifFile;
          gifFile = new File(FileUtil.getEmojiDir(context),
              "/emoji/e" + i + "/" + k + "_" + j + ".gif");
          emojiGifs.add(gifFile);
        }
      }
    }
    for (int i = 0; i < emojiList.length; i++) {
      emoticons.put(Pattern.compile(Pattern.quote(emojiList[i])),
          new DefaultGifEmoji(emojiGifs.get(i), emojiList[i]));
    }
    defaultGifEmojis = new DefaultGifEmoji[emojiList.length];
    for (int i = 0; i < emojiList.length; i++) {
      defaultGifEmojis[i] = new DefaultGifEmoji(emojiGifs.get(i), emojiList[i]);
    }
    defaultEmojiInited = true;
    handler.sendEmptyMessage(UNZIP_SUCCESS);
  }

  public Spannable getSpannableByEmoji(Context context, Emoji emoji) {
    SpannableString spannableString = new SpannableString(emoji.getEmojiText());
    ImageSpan imageSpan;
    Drawable drawable;
    if (drawableCacheMap.containsKey(emoji.getCacheKey())) {
      drawable = drawableCacheMap
          .get(emoji.getCacheKey());
      imageSpan = new EqualHeightSpan(drawable);
    } else {

      drawable = emoji.getDrawable(context);
      imageSpan = new EqualHeightSpan(drawable);
      drawableCacheMap
          .put(emoji.getCacheKey(), drawable);

    }
    spannableString
        .setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    return spannableString;
  }

  public Drawable getDrawableByEmoji(Context context, Emoji emoji) {
    Drawable drawable;
    if (drawableCacheMap.containsKey(emoji.getCacheKey())) {
      drawable = drawableCacheMap
          .get(emoji.getCacheKey());
    } else {
      drawable = emoji.getDrawable(context);
      drawableCacheMap
          .put(emoji.getCacheKey(), drawable);
    }
    return drawable;
  }


  public Spannable getSpannableByPattern(Context context, String text) {
    SpannableString spannableString = new SpannableString(text);
    for (Entry<Pattern, Emoji> entry : emoticons.entrySet()) {
      Matcher matcher = entry.getKey().matcher(text);
      while (matcher.find()) {
        ImageSpan imageSpan = null;
        Emoji emoji = entry.getValue();

        if (emoji instanceof DefaultGifEmoji) {
          imageSpan = getImageSpanByEmoji(context, emoji);
        }
//        Object value = emoji.getRes();
//        if (value instanceof String && !((String) value).startsWith("http")) {
//          //本地路径
//          File file = new File((String) value);
//          if (file.exists() && !file.isDirectory()) {
//            imageSpan = new ImageSpan(context, Uri.fromFile(file));
//          }
//        } else {
//          imageSpan = new ImageSpan(context, (Integer) value);
//        }
        if (imageSpan != null) {
          spannableString.setSpan(imageSpan,
              matcher.start(), matcher.end(),
              Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
      }
    }
    return spannableString;
  }

  private ImageSpan getImageSpanByEmoji(Context context, Emoji emoji) {
    ImageSpan imageSpan;
    Drawable gifDrawable;
    if (drawableCacheMap.containsKey(emoji.getCacheKey())) {
      gifDrawable = drawableCacheMap.get(emoji.getCacheKey());
      imageSpan = new EqualHeightSpan(gifDrawable);
    } else {
      gifDrawable = emoji.getDrawable(context);
      imageSpan = new EqualHeightSpan(gifDrawable);
      drawableCacheMap
          .put(emoji.getCacheKey(), gifDrawable);

    }
    return imageSpan;
  }


  private void onUnzipSuccess() {
    Log.i(TAG, "onUnzipSuccess: " + Thread.currentThread().getName());
    for (OnUnzipSuccessListener onUnzipSuccessListener : onUnzipSuccessListeners) {
      onUnzipSuccessListener.onUnzipSuccess(defaultGifEmojis);
    }
    onUnzipSuccessListeners.clear();
  }

  public void displayImage(ImageView imageView, Emoji emoji) {
//    Glide.with(imageView)
//        .load(emoji.getRes())
//        .apply(new RequestOptions().placeholder(emoji.getDefaultResId()))
//        .into(imageView);
  }


  public interface OnUnzipSuccessListener {

    void onUnzipSuccess(DefaultGifEmoji[] defaultGifEmojis);
  }
}
