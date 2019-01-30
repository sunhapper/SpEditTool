package com.sunhapper.x.spedit.gif.watcher;

import android.text.SpanWatcher;
import android.text.Spannable;
import android.view.View;

import com.sunhapper.x.spedit.gif.drawable.InvalidateDrawable;
import com.sunhapper.x.spedit.gif.listener.RefreshListener;
import com.sunhapper.x.spedit.gif.span.RefreshSpan;

/**
 * Created by sunhapper on 2019/1/25 .
 */
public class GifWatcher implements SpanWatcher, RefreshListener {
    private long mLastTime;
    private static final int REFRESH_INTERVAL = 60;
    private View mView;

    public GifWatcher(View view) {
        mView = view;
    }

    @Override
    public void onSpanAdded(Spannable text, Object what, int start, int end) {
        if (what instanceof RefreshSpan) {
            InvalidateDrawable drawable = ((RefreshSpan) what).getInvalidateDrawable();
            if (drawable != null) {
                drawable.addRefreshListener(this);
            }
        }
    }

    @Override
    public void onSpanRemoved(Spannable text, Object what, int start, int end) {
        if (what instanceof RefreshSpan) {
            InvalidateDrawable drawable = ((RefreshSpan) what).getInvalidateDrawable();
            if (drawable != null) {
                drawable.removeRefreshListener(this);
            }
        }
    }

    @Override
    public void onSpanChanged(Spannable text, Object what, int ostart, int oend, int nstart, int nend) {

    }

    @Override
    public void onRefresh() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastTime > REFRESH_INTERVAL) {
            mLastTime = currentTime;
            mView.invalidate();
        }
    }
}
