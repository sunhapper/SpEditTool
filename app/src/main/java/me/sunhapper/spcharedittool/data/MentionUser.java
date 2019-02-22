package me.sunhapper.spcharedittool.data;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.sunhapper.x.spedit.mention.span.BreakableSpan;
import com.sunhapper.x.spedit.mention.span.IntegratedSpan;

/**
 * Created by sunhapper on 2019/1/30 .
 * 使用三星输入法IntegratedSpan完整性不能保证，所以加上BreakableSpan使得@mention完整性被破坏时删除对应span
 */
public class MentionUser implements IntegratedSpan, BreakableSpan, DataSpan {
    public String name;
    public long id;
    private Object styleSpan;

    public MentionUser() {
        name = "sunhapper";
        id = System.currentTimeMillis();
    }

    public Spannable getSpanableString() {
        styleSpan = new ForegroundColorSpan(Color.MAGENTA);
        SpannableString spannableString = new SpannableString(getDisplayText());
        spannableString.setSpan(styleSpan, 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(this, 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        return stringBuilder.append(spannableString).append(" ");
    }

    private CharSequence getDisplayText() {
        return "@" + name;
    }


    @Override
    public String toString() {
        return "MentionUser{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
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
}
