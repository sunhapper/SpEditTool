package me.sunhapper.spcharedittool.data

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import com.sunhapper.x.spedit.mention.span.BreakableSpan

/**
 * Created by sunhapper on 2019/1/30 .
 */
class Topic : BreakableSpan, DataSpan {
    private val text = "topic"
    private var styleSpan: Any? = null
    private val displayText: String
        get() = "#$text#"

    val spannableString: Spannable
        get() {
            styleSpan = ForegroundColorSpan(Color.BLUE)
            val spannableString = SpannableString(displayText)
            spannableString.setSpan(styleSpan, 0, spannableString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(this, 0, spannableString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            val stringBuilder = SpannableStringBuilder()
            return stringBuilder.append(" ").append(spannableString).append(" ")
        }

    /**
     * @return true the BreakableSpan will be removed
     * you can remove custom style span when content broken
     */
    override fun isBreak(spannable: Spannable): Boolean {
        val spanStart = spannable.getSpanStart(this)
        val spanEnd = spannable.getSpanEnd(this)
        val isBreak = spanStart >= 0 && spanEnd >= 0 && spannable.subSequence(spanStart, spanEnd).toString() != displayText
        if (isBreak && styleSpan != null) {
            spannable.removeSpan(styleSpan)
            styleSpan = null
        }
        return isBreak
    }

    override fun toString(): String {
        return "Topic{text= $text }"
    }
}
