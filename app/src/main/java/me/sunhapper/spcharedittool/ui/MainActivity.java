package me.sunhapper.spcharedittool.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.sunhapper.gifdrawable.drawable.TextGifDrawable;
import com.sunhapper.glide.drawable.DrawableTarget;
import com.sunhapper.x.spedit.SpUtilKt;
import com.sunhapper.x.spedit.gif.drawable.ProxyDrawable;
import com.sunhapper.x.spedit.view.SpXEditText;

import java.io.IOException;

import me.sunhapper.spcharedittool.GlideApp;
import me.sunhapper.spcharedittool.R;
import me.sunhapper.spcharedittool.data.DataSpan;
import me.sunhapper.spcharedittool.data.MentionUser;
import me.sunhapper.spcharedittool.data.Topic;
import me.sunhapper.spcharedittool.emoji.DefaultGifEmoji;
import me.sunhapper.spcharedittool.emoji.DeleteEmoji;
import me.sunhapper.spcharedittool.emoji.Emoji;
import me.sunhapper.spcharedittool.emoji.EmojiManager;
import me.sunhapper.spcharedittool.emoji.EmojiManager.OnUnzipSuccessListener;
import me.sunhapper.spcharedittool.emoji.EmojiconMenu;
import me.sunhapper.spcharedittool.emoji.EmojiconMenuBase.EmojiconMenuListener;
import pl.droidsonroids.gif.GifDrawable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SpXEditText spEditText;
    private EmojiconMenu emojiInputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spEditText = findViewById(R.id.spEdt);
        emojiInputView = findViewById(R.id.emojiInputView);
        spEditText.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
    }

    private void insertEmoji(Emoji emoji) {
        //使用EmojiManager中缓存的drawable
        Drawable gifDrawable = EmojiManager.getInstance()
                .getDrawableByEmoji(this, emoji);
        Spannable spannable = SpUtilKt.createGifDrawableSpan(gifDrawable, emoji.getEmojiText());
        SpUtilKt.insertSpannableString(spEditText.getText(), spannable);
    }


    public void insertTopic(View view) {
        replace(new Topic().getSpanableString());
    }


    public void insertMention(View view) {
        replace(new MentionUser().getSpanableString());
    }

    public void getData(View view) {
        DataSpan[] dataSpans = spEditText.getText().getSpans(0, spEditText.length(), DataSpan.class);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("完整字符串：").append(spEditText.getText())
                .append("\n").append("特殊字符串：\n");
        for (DataSpan dataSpan : dataSpans) {
            stringBuilder.append(dataSpan.toString())
                    .append("\n");
        }
        Log.i(TAG, "getData: " + stringBuilder);
        Toast.makeText(this, stringBuilder, Toast.LENGTH_SHORT).show();
    }


    public void insertAllGif(View view) {
        EmojiManager.getInstance().getDefaultEmojiData(new OnUnzipSuccessListener() {
            @Override
            public void onUnzipSuccess(DefaultGifEmoji[] defaultGifEmojis) {
                for (DefaultGifEmoji defaultGifEmoji : defaultGifEmojis) {
                    insertEmoji(defaultGifEmoji);
                }
            }
        });
    }

    public void setGlideGif(View view) {
        try {
            CharSequence charSequence = createGlideText();
            SpUtilKt.insertSpannableString(spEditText.getText(), charSequence);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private CharSequence createGlideText() throws IOException {
        GifDrawable gifDrawable = new TextGifDrawable(getResources(), R.drawable.a);
        ProxyDrawable proxyDrawable = new ProxyDrawable();
        GlideApp.with(this)
                .load(
                        "http://5b0988e595225.cdn.sohucs.com/images/20170919/1ce5d4c52c24432e9304ef942b764d37.gif")
                .placeholder(gifDrawable)
                .into(new DrawableTarget(proxyDrawable));
        return SpUtilKt.createResizeGifDrawableSpan(proxyDrawable, "[c]");
    }

    public void openGifRecycler(View view) {
        Intent intent = new Intent(this, RecyclerDemoActivity.class);
        startActivity(intent);

    }

    public void openGlideRecycler(View view) {
        Intent intent = new Intent(this, RecyclerGlideDemoActivity.class);
        startActivity(intent);
    }

    public void regText(View view) {
        String regText = "[大兵]  [奋斗]";
        Spannable spannable = EmojiManager.getInstance().getSpannableByPattern(this, regText);
        SpUtilKt.insertSpannableString(spEditText.getText(), spannable);
    }

    private void replace(CharSequence charSequence) {
        Editable editable = spEditText.getText();
        SpUtilKt.insertSpannableString(editable, charSequence);
    }


}
