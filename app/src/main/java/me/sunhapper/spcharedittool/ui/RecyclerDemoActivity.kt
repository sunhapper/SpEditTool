package me.sunhapper.spcharedittool.ui

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sunhapper.x.spedit.gif.span.GifIsoheightImageSpan
import kotlinx.android.synthetic.main.activity_recycler_demo.*
import me.sunhapper.spcharedittool.R
import me.sunhapper.spcharedittool.emoji.DefaultGifEmoji
import me.sunhapper.spcharedittool.emoji.EmojiManager
import me.sunhapper.spcharedittool.emoji.EmojiManager.OnUnzipSuccessListener
import java.util.*

/**
 * Created by sunha on 2018/2/7 0007.
 */

class RecyclerDemoActivity : AppCompatActivity() {
    private val charSequences = ArrayList<CharSequence>()
    private val adapter = Adapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_demo)
        gif_recyclerView.layoutManager = LinearLayoutManager(this)
        gif_recyclerView.adapter = adapter
        EmojiManager.getDefaultEmojiData(object : OnUnzipSuccessListener {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onUnzipSuccess(defaultGifEmojis: List<DefaultGifEmoji>) {
                Log.i(TAG, "onUnzipSuccess: 0")
                val spannableStringBuilder = SpannableStringBuilder()
                for (defaultGifEmoji in defaultGifEmojis) {
                    val gifDrawable = EmojiManager.getDrawableByEmoji(this@RecyclerDemoActivity, defaultGifEmoji)
                    val imageSpan = GifIsoheightImageSpan(gifDrawable)
                    spannableStringBuilder
                            .append(defaultGifEmoji.emojiText, imageSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    charSequences.add(SpannableString(spannableStringBuilder))
                }
                Log.i(TAG, "onUnzipSuccess: ")
                adapter.notifyDataSetChanged()
            }
        })

    }

    internal inner class Adapter : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_recycler_demo, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val charSequence = charSequences[position]
            Log.i(TAG, "onBindViewHolder:start$position")
            if (position == 8) {
                Log.i(TAG, "onBindViewHolder: ")
            }
            holder.textView.text = charSequence
            //      GifTextUtil.setTextWithReuseDrawable(holder.textView, charSequence, false);
            Log.i(TAG, "onBindViewHolder:  end$position")
        }

        override fun getItemCount(): Int {
            return charSequences.size
        }
    }


    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textView: TextView = itemView.findViewById(R.id.textView)

    }

    companion object {

        private val TAG = "RecyclerDemoActivity"
    }

}
