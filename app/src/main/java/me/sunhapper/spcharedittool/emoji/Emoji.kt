package me.sunhapper.spcharedittool.emoji

import android.content.Context
import android.graphics.drawable.Drawable

/**
 * Created by sunhapper on 2018/2/4.
 */

interface Emoji {

    val emojiText: CharSequence

    val res: Any


    val isDeleteIcon: Boolean

    val defaultResId: Int

    val cacheKey: Any

    fun getDrawable(context: Context): Drawable

}
