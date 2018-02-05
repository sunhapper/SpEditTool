package me.sunhapper.spcharedittool.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import com.sunhapper.spedittool.view.SpEditText;
import com.sunhapper.spedittool.view.SpEditText.KeyReactListener;
import com.sunhapper.spedittool.view.SpEditText.SpData;
import java.io.IOException;
import me.sunhapper.spcharedittool.GlideApp;
import me.sunhapper.spcharedittool.R;
import me.sunhapper.spcharedittool.drawable.RefreshGifDrawable;
import me.sunhapper.spcharedittool.emoji.DefaultGifEmoji;
import me.sunhapper.spcharedittool.emoji.DeleteEmoji;
import me.sunhapper.spcharedittool.emoji.Emoji;
import me.sunhapper.spcharedittool.emoji.EmojiManager;
import me.sunhapper.spcharedittool.emoji.EmojiManager.OnUnzipSuccessListener;
import me.sunhapper.spcharedittool.emoji.EmojiconMenu;
import me.sunhapper.spcharedittool.emoji.EmojiconMenuBase.EmojiconMenuListener;
import me.sunhapper.spcharedittool.glide.DrawableTarget;
import me.sunhapper.spcharedittool.glide.PreDrawable;
import me.sunhapper.spcharedittool.span.GifAlignCenterSpan;
import me.sunhapper.spcharedittool.util.DrawableUtil;
import pl.droidsonroids.gif.GifDrawable;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  private SpEditText spEditText;
  private EmojiconMenu emojiInputView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    spEditText = findViewById(R.id.spEdt);
    emojiInputView = findViewById(R.id.emojiInputView);
    spEditText.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//    spEditText.setReactKeys("@#%*");
    emojiInputView.setEmojiconMenuListener(new EmojiconMenuListener() {
      @Override
      public void onExpressionClicked(Emoji emoji) {
        if (emoji instanceof DeleteEmoji) {
          if (!TextUtils.isEmpty(spEditText.getText())) {
            KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0,
                KeyEvent.KEYCODE_ENDCALL);
            spEditText.dispatchKeyEvent(event);
          }
        } else if (emoji instanceof DefaultGifEmoji) {
          insertEmoji(emoji);
        }
      }
    });
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
            break;
          case "*":
            spEditText.insertSpecialStr(" ******* ", true, 3, new ForegroundColorSpan(Color.RED));
            break;
        }


      }
    });
  }

  private void insertEmoji(Emoji emoji) {
    Drawable gifDrawable = EmojiManager.getInstance()
        .getDrawableByEmoji(this, emoji);
    ImageSpan imageSpan = new GifAlignCenterSpan(gifDrawable);
    spEditText.insertSpecialStr(emoji.getEmojiText(), false, emoji.getEmojiText(), imageSpan);
  }

  public void insertSp(View view) {
    spEditText.insertSpecialStr(" 这是插入的字符串 ", false, 4, new ForegroundColorSpan(Color.BLUE));
  }

  public void getData(View view) {
    SpData[] spDatas = spEditText.getSpDatas();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("完整字符串：").append(spEditText.getText())
        .append("\n").append("特殊字符串：\n");
    for (SpData spData : spDatas) {
      stringBuilder.append(spData.toString())
          .append("\n");
    }
    Log.i(TAG, "getData: " + stringBuilder);
    Toast.makeText(this, stringBuilder, Toast.LENGTH_SHORT).show();
  }


  public void insertAllGif(View view) throws IOException {
    EmojiManager.getInstance().getDefaultEmojiData(new OnUnzipSuccessListener() {
      @Override
      public void onUnzipSuccess(DefaultGifEmoji[] defaultGifEmojis) {
        for (DefaultGifEmoji defaultGifEmoji : defaultGifEmojis) {
          insertEmoji(defaultGifEmoji);
        }

//        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
//        for (DefaultGifEmoji defaultGifEmoji : defaultGifEmojis) {
//          Drawable gifDrawable = EmojiManager.getInstance()
//              .getDrawableByEmoji(MainActivity.this, defaultGifEmoji);
//          ImageSpan imageSpan = new GifAlignCenterSpan(gifDrawable);
//          spannableStringBuilder
//              .append(defaultGifEmoji.getEmojiText(), imageSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//        spEditText.insertSpecialStr(spannableStringBuilder, false, spannableStringBuilder, null);
      }
    });
  }

  public void setGif(View view) {
    try {
      GifDrawable gifDrawable = new RefreshGifDrawable(getResources(), R.drawable.a);
      PreDrawable preDrawable = new PreDrawable();
      GlideApp.with(this)
          .load(
              "http://5b0988e595225.cdn.sohucs.com/images/20170919/1ce5d4c52c24432e9304ef942b764d37.gif")
          .placeholder(gifDrawable)
          .into(new DrawableTarget(preDrawable));
      CharSequence charSequence = DrawableUtil.getDrawableText("[c]", preDrawable);
      spEditText.insertSpecialStr(charSequence, false, charSequence, null);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
