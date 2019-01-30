package me.sunhapper.spcharedittool.data;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.sunhapper.x.spedit.mention.span.BreakableSpan;

/**
 * Created by sunhapper on 2019/1/30 .
 */
public class Topic implements BreakableSpan, DataSpan {
    private String text = "topic";
    private Object styleSpan;

    public String getDisplayText() {
        return "#" + text + "#";
    }

    public Spannable getSpanableString() {
        styleSpan = new ForegroundColorSpan(Color.BLUE);
        SpannableString spannableString = new SpannableString(getDisplayText());
        spannableString.setSpan(styleSpan, 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(this, 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        return stringBuilder.append(" ").append(spannableString).append(" ");
    }


    @Override
    public boolean isBreak(Spannable text) {
        int spanStart = text.getSpanStart(this);
        int spanEnd = text.getSpanEnd(this);
        boolean isBreak = spanStart >= 0 && spanEnd >= 0 && !text.subSequence(spanStart, spanEnd).toString().equals(
                getDisplayText());
        if (isBreak && styleSpan != null) {
            text.removeSpan(styleSpan);
            styleSpan = null;
        }
        return isBreak;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "text='" + text + '\'' +
                '}';
    }
}
