package me.sunhapper.spcharedittool

import android.app.Application
import me.sunhapper.spcharedittool.emoji.EmojiManager

/**
 * Created by sunha on 2018/2/5 0005.
 */

class SpApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        EmojiManager.init(this)
    }
}
