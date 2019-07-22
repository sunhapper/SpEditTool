package me.sunhapper.spcharedittool.util

import android.content.Context

/**
 * Created by sunhapper on 2018/2/3.
 */

class PreferenceUtil {
    companion object {
        private val TAG = "sp_data"
        private val EMOJI_INIT = "emoji_init"
        fun getEmojiInitResult(context: Context): Boolean {
            val pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            return pref.getBoolean(EMOJI_INIT, false)
        }

        fun setEmojiInitResult(context: Context, initResult: Boolean) {
            val pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            pref.edit().putBoolean(EMOJI_INIT, initResult).apply()
        }
    }
}


