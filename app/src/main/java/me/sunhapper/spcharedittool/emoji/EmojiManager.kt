/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.sunhapper.spcharedittool.emoji

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Handler.Callback
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sunhapper.x.spedit.gif.span.GifIsoheightImageSpan
import me.sunhapper.spcharedittool.util.FileUtil
import me.sunhapper.spcharedittool.util.PreferenceUtil
import java.io.File
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

object EmojiManager {

    private val UNZIP_SUCCESS = 11111
    private var emojiGifs = arrayListOf<File>()


    private val emojiList = arrayOf("[微笑]", "[撇嘴]", "[色]", "[发呆]", "[得意]", "[流泪]", "[害羞]", "[闭嘴]", "[睡]", "[大哭]", "[尴尬]", "[发怒]", "[调皮]", "[龇牙]", "[惊讶]", "[难过]", "[酷]", "[冷汗]", "[抓狂]", "[吐]",

            "[偷笑]", "[可爱]", "[白眼]", "[傲慢]", "[饥饿]", "[困]", "[惊恐]", "[流汗]", "[憨笑]", "[大兵]", "[奋斗]", "[咒骂]", "[疑问]", "[嘘…]", "[晕]", "[折磨]", "[衰]", "[骷髅]", "[敲打]", "[再见]",

            "[擦汗]", "[抠鼻]", "[鼓掌]", "[糗大了]", "[坏笑]", "[左哼哼]", "[右哼哼]", "[哈欠]", "[鄙视]", "[委屈]", "[快哭了]", "[阴险]", "[亲亲]", "[吓]", "[可怜]", "[菜刀]", "[西瓜]", "[啤酒]", "[篮球]", "[乒乓球]",

            "[咖啡]", "[饭]", "[猪头]", "[玫瑰]", "[凋谢]", "[示爱]", "[爱心]", "[心碎]", "[蛋糕]", "[闪电]", "[炸弹]", "[刀]", "[足球]", "[瓢虫]", "[便便]", "[月亮]", "[太阳]", "[礼物]", "[拥抱]", "[强]",

            "[弱]", "[握手]", "[胜利]", "[抱拳]", "[勾引]", "[拳头]", "[差劲]", "[爱你]", "[NO]", "[OK]", "[爱情]", "[飞吻]", "[跳跳]", "[发抖]", "[怄火]", "[转圈]", "[磕头]", "[回头]", "[跳绳]", "[挥手]")


    private val emoticons = HashMap<Pattern, Emoji>()
    private val drawableCacheMap = HashMap<Any, Drawable>()
    private var defaultGifEmojis = arrayListOf<DefaultGifEmoji>()

    private var defaultEmojiInited = false
    private val handler: Handler = Handler(Callback { msg ->
        if (msg.what == UNZIP_SUCCESS) {
            onUnzipSuccess()
            return@Callback true
        }
        false
    })
    private val onUnzipSuccessListeners = ArrayList<OnUnzipSuccessListener>()


    fun getDefaultEmojiData(unzipSuccessListener: OnUnzipSuccessListener) {
        if (defaultEmojiInited) {
            unzipSuccessListener.onUnzipSuccess(defaultGifEmojis)
        } else {
            onUnzipSuccessListeners.add(unzipSuccessListener)
        }
    }

    fun init(context: Context) {
        val unziped = PreferenceUtil.getEmojiInitResult(context)
        if (unziped) {
            initDefault(context)
        } else {
            Thread(Runnable {
                val unzipResult = FileUtil
                        .unzipFromAssets(context, FileUtil.getEmojiDir(context), "emoji")
                PreferenceUtil.setEmojiInitResult(context, unzipResult)
                if (unzipResult) {
                    initDefault(context)
                }
            }).start()
        }
    }

    private fun initDefault(context: Context) {
        for (i in 1..5) {
            for (j in 0..2) {
                for (k in 0..6) {
                    if (j == 2 && k == 6) {
                        continue
                    }
                    val gifFile: File
                    gifFile = File(FileUtil.getEmojiDir(context),
                            "/emoji/e" + i + "/" + k + "_" + j + ".gif")
                    emojiGifs.add(gifFile)
                }
            }
        }
        emojiList.forEachIndexed { index, s ->
            emoticons[Pattern.compile(Pattern.quote(s))] = DefaultGifEmoji(emojiGifs[index], s)
            defaultGifEmojis.add(DefaultGifEmoji(emojiGifs[index], s))
        }
        defaultEmojiInited = true
        handler.sendEmptyMessage(UNZIP_SUCCESS)
    }

    fun getSpannableByEmoji(context: Context, emoji: Emoji): Spannable {
        val spannableString = SpannableString(emoji.emojiText)
        val imageSpan: ImageSpan
        val drawable: Drawable
        if (drawableCacheMap.containsKey(emoji.cacheKey)) {
            drawable = drawableCacheMap[emoji.cacheKey]!!
            imageSpan = GifIsoheightImageSpan(drawable)
        } else {

            drawable = emoji.getDrawable(context)
            imageSpan = GifIsoheightImageSpan(drawable)
            drawableCacheMap[emoji.cacheKey] = drawable

        }
        spannableString
                .setSpan(imageSpan, 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    fun getDrawableByEmoji(context: Context, emoji: Emoji): Drawable {
        val drawable: Drawable
        if (drawableCacheMap.containsKey(emoji.cacheKey)) {
            drawable = drawableCacheMap[emoji.cacheKey]!!
        } else {
            drawable = emoji.getDrawable(context)
            drawableCacheMap[emoji.cacheKey] = drawable
        }
        return drawable
    }


    fun getSpannableByPattern(context: Context, text: String): Spannable {
        val spannableString = SpannableString(text)
        for ((key, emoji) in emoticons) {
            val matcher = key.matcher(text)
            while (matcher.find()) {
                var imageSpan: ImageSpan? = null

                if (emoji is DefaultGifEmoji) {
                    imageSpan = getImageSpanByEmoji(context, emoji)
                }
                if (imageSpan != null) {
                    spannableString.setSpan(imageSpan,
                            matcher.start(), matcher.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }
        return spannableString
    }

    private fun getImageSpanByEmoji(context: Context, emoji: Emoji): ImageSpan {
        val imageSpan: ImageSpan
        val gifDrawable: Drawable
        if (drawableCacheMap.containsKey(emoji.cacheKey)) {
            gifDrawable = drawableCacheMap[emoji.cacheKey]!!
            imageSpan = GifIsoheightImageSpan(gifDrawable)
        } else {
            gifDrawable = emoji.getDrawable(context)
            imageSpan = GifIsoheightImageSpan(gifDrawable)
            drawableCacheMap[emoji.cacheKey] = gifDrawable

        }
        return imageSpan
    }


    private fun onUnzipSuccess() {
        for (onUnzipSuccessListener in onUnzipSuccessListeners) {
            onUnzipSuccessListener.onUnzipSuccess(defaultGifEmojis)
        }
        onUnzipSuccessListeners.clear()
    }

    fun displayImage(imageView: ImageView, emoji: Emoji) {
        Glide.with(imageView)
                .load(emoji.res)
                .apply(RequestOptions().placeholder(emoji.defaultResId))
                .into(imageView)
    }


    interface OnUnzipSuccessListener {

        fun onUnzipSuccess(defaultGifEmojis: List<DefaultGifEmoji>)
    }

}
