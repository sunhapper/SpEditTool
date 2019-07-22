package me.sunhapper.spcharedittool.emoji

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import me.sunhapper.spcharedittool.R


class DeleteEmoji : Emoji {

    override val emojiText: CharSequence
        get() = ""

    override val res: Any
        get() = R.drawable.common_emoj_delete_expression


    override val isDeleteIcon: Boolean
        get() = true

    override val defaultResId: Int
        get() = R.drawable.common_emoj_delete_expression

    override val cacheKey: Any
        get() = R.drawable.common_emoj_delete_expression


    override fun getDrawable(context: Context): Drawable {
        return ContextCompat.getDrawable(context, defaultResId)!!
    }


}
