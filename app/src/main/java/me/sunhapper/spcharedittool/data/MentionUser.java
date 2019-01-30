package me.sunhapper.spcharedittool.data;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.sunhapper.x.spedit.mention.span.IntegratedSpan;

/**
 * Created by sunhapper on 2019/1/30 .
 */
public class MentionUser implements IntegratedSpan {
    public String name;
    public long id;

    public MentionUser() {
        name = "sunhapper";
        id = System.currentTimeMillis();
    }

    public Spannable getSpanableString() {
        SpannableString spannableString = new SpannableString(getDisplayText());
        spannableString.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, spannableString.length(),
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
}
