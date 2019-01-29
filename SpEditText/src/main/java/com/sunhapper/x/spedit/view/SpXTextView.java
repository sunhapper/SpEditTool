package com.sunhapper.x.spedit.view;

import android.content.Context;
import android.text.NoCopySpan;
import android.util.AttributeSet;

import com.sunhapper.x.spedit.gif.watcher.GifWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunhapper on 2019/1/25 .
 */
public class SpXTextView extends android.support.v7.widget.AppCompatTextView {

    public SpXTextView(Context context) {
        super(context);
    }

    public SpXTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpXTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        List<NoCopySpan> watchers = new ArrayList<>();
        watchers.add(new GifWatcher(this));
        setSpannableFactory(new SpXSpannableFactory(watchers));
    }

    public void setText(CharSequence text, BufferType type) {
        super.setText(text, BufferType.SPANNABLE);
    }

}
