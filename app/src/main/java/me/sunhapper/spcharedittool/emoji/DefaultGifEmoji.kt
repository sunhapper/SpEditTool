package me.sunhapper.spcharedittool.emoji

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.sunhapper.x.spedit.gif.drawable.ProxyDrawable
import me.sunhapper.spcharedittool.R
import pl.droidsonroids.gif.GifDrawable
import java.io.File
import java.io.IOException

class DefaultGifEmoji(private val emojiconFile: File, override val emojiText: String) : Emoji {


    override val res: Any
        get() = emojiconFile


    override val isDeleteIcon: Boolean
        get() = false

    override val defaultResId: Int
        get() = R.drawable.common_emoj_smile_default

    override val cacheKey: Any
        get() = if (emojiconFile.exists()) {
            emojiconFile.absolutePath
        } else {
            R.drawable.common_emoj_smile_default
        }


    override fun getDrawable(context: Context): Drawable {
        try {
            return ProxyDrawable(GifDrawable(emojiconFile))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return ContextCompat.getDrawable(context, defaultResId)!!
    }


}
