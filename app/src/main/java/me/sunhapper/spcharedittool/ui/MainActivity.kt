package me.sunhapper.spcharedittool.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.sunhapper.glide.drawable.DrawableTarget
import com.sunhapper.x.spedit.createGifDrawableSpan
import com.sunhapper.x.spedit.createResizeGifDrawableSpan
import com.sunhapper.x.spedit.gif.drawable.ProxyDrawable
import com.sunhapper.x.spedit.insertSpannableString
import kotlinx.android.synthetic.main.activity_main.*
import me.sunhapper.spcharedittool.GlideApp
import me.sunhapper.spcharedittool.R
import me.sunhapper.spcharedittool.data.DataSpan
import me.sunhapper.spcharedittool.data.MentionUser
import me.sunhapper.spcharedittool.data.Topic
import me.sunhapper.spcharedittool.emoji.DefaultGifEmoji
import me.sunhapper.spcharedittool.emoji.DeleteEmoji
import me.sunhapper.spcharedittool.emoji.Emoji
import me.sunhapper.spcharedittool.emoji.EmojiManager
import me.sunhapper.spcharedittool.emoji.EmojiManager.OnUnzipSuccessListener
import pl.droidsonroids.gif.GifDrawable
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        spEdt.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        emojiInputView.listener = { emoji ->
            if (emoji is DeleteEmoji) {
                if (!TextUtils.isEmpty(spEdt.text)) {
                    val event = KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0,
                            KeyEvent.KEYCODE_ENDCALL)
                    spEdt.dispatchKeyEvent(event)
                }
            } else if (emoji is DefaultGifEmoji) {
                insertEmoji(emoji)
            }
        }
    }

    private fun insertEmoji(emoji: Emoji) {
        //使用EmojiManager中缓存的drawable
        val gifDrawable = EmojiManager.getDrawableByEmoji(this, emoji)
        val spannable = createGifDrawableSpan(gifDrawable, emoji.emojiText)
        insertSpannableString(spEdt.text, spannable)
    }


    fun insertTopic(view: View) {
        replace(Topic().spannableString)
    }


    fun insertMention(view: View) {
        replace(MentionUser().spannableString)
    }

    fun getData(view: View) {
        val dataSpans = spEdt.text.getSpans(0, spEdt.length(), DataSpan::class.java)
        val stringBuilder = StringBuilder()
        stringBuilder.append("完整字符串：").append(spEdt.text)
                .append("\n").append("特殊字符串：\n")
        for (dataSpan in dataSpans) {
            stringBuilder.append(dataSpan.toString())
                    .append("\n")
        }
        Log.i(TAG, "getData: $stringBuilder")
        Toast.makeText(this, stringBuilder, Toast.LENGTH_SHORT).show()
    }


    fun insertAllGif(view: View) {
        EmojiManager.getDefaultEmojiData(object : OnUnzipSuccessListener {
            override fun onUnzipSuccess(defaultGifEmojis: List<DefaultGifEmoji>) {
                for (defaultGifEmoji in defaultGifEmojis) {
                    insertEmoji(defaultGifEmoji)
                }
            }
        })
    }

    fun setGlideGif(view: View) {
        try {
            val charSequence = createGlideText()
            insertSpannableString(spEdt.text, charSequence)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Throws(IOException::class)
    private fun createGlideText(): CharSequence {
        val gifDrawable = GifDrawable(resources, R.drawable.a)
        val proxyDrawable = ProxyDrawable()
        GlideApp.with(this)
                .load(
                        "http://5b0988e595225.cdn.sohucs.com/images/20170919/1ce5d4c52c24432e9304ef942b764d37.gif")
                .placeholder(gifDrawable)
                .into(DrawableTarget(proxyDrawable))
        return createResizeGifDrawableSpan(proxyDrawable, "[c]")
    }

    fun openGifRecycler(view: View) {
        val intent = Intent(this, RecyclerDemoActivity::class.java)
        startActivity(intent)

    }

    fun openGlideRecycler(view: View) {
        val intent = Intent(this, RecyclerGlideDemoActivity::class.java)
        startActivity(intent)
    }

    fun regText(view: View) {
        val regText = "[大兵]  [奋斗]"
        val spannable = EmojiManager.getSpannableByPattern(this, regText)
        insertSpannableString(spEdt.text, spannable)
    }

    private fun replace(charSequence: CharSequence) {
        val editable = spEdt.text
        insertSpannableString(editable, charSequence)
    }

    companion object {

        private val TAG = "MainActivity"
    }


}
