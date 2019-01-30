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
public class Topic implements BreakableSpan {
    private String text = "topic";

    public String getDisplayText() {
        return "#" + text + "#";
    }

    public Spannable getSpanableString() {
        SpannableString spannableString = new SpannableString(getDisplayText());
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 0, spannableString.length(),
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
        return spanStart >= 0 && spanEnd >= 0 && text.subSequence(spanStart, spanEnd) != getDisplayText();
    }

    @Override
    public String toString() {
        return "Topic{" +
                "text='" + text + '\'' +
                '}';
    }
}
