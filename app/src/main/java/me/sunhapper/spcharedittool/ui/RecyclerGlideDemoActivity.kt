package me.sunhapper.spcharedittool.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sunhapper.glide.drawable.DrawableTarget
import com.sunhapper.x.spedit.createResizeGifDrawableSpan
import com.sunhapper.x.spedit.gif.drawable.ProxyDrawable
import kotlinx.android.synthetic.main.activity_recycler_demo.*
import me.sunhapper.spcharedittool.GlideApp
import me.sunhapper.spcharedittool.R
import pl.droidsonroids.gif.GifDrawable
import java.io.IOException
import java.util.*

/**
 * Created by sunha on 2018/2/7 0007.
 */

class RecyclerGlideDemoActivity : AppCompatActivity() {
    private val charSequences = ArrayList<CharSequence>()
    private val adapter = Adapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_demo)
        gif_recyclerView.layoutManager = LinearLayoutManager(this)
        gif_recyclerView.adapter = adapter
        for (i in 0..99) {
            val gifDrawable: GifDrawable
            try {
                gifDrawable = GifDrawable(resources, R.drawable.a)
                val proxyDrawable = ProxyDrawable()
                GlideApp.with(this)
                        .load(
                                "http://5b0988e595225.cdn.sohucs.com/images/20170919/1ce5d4c52c24432e9304ef942b764d37" + ".gif")
                        .placeholder(gifDrawable)
                        .into(DrawableTarget(proxyDrawable))
                val spannableStringBuilder = SpannableStringBuilder()
                val spannable = createResizeGifDrawableSpan(proxyDrawable, "[c]")
                spannableStringBuilder.append(spannable).append(i.toString())
                charSequences.add(SpannableString(spannableStringBuilder))
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        adapter.notifyDataSetChanged()

    }

    internal inner class Adapter : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_recycler_glide_demo, parent, false)
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

        private const val TAG = "RecyclerDemoActivity"
    }

}
