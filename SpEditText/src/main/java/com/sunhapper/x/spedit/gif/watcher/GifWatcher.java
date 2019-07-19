package com.sunhapper.x.spedit.gif.watcher;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.view.View;

import com.sunhapper.x.spedit.gif.drawable.InvalidateDrawable;
import com.sunhapper.x.spedit.gif.listener.RefreshListener;
import com.sunhapper.x.spedit.gif.span.RefreshSpan;

import java.lang.ref.WeakReference;

/**
 * Created by sunhapper on 2019/1/25 .
 */
public class GifWatcher implements SpanWatcher, RefreshListener {
    private long mLastTime;
    private static final int REFRESH_INTERVAL = 60;
    private WeakReference<View> mViewWeakReference;

    public GifWatcher(View view) {
        mViewWeakReference = new WeakReference<>(view);
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
    public boolean onRefresh() {
        View view = mViewWeakReference.get();
        if (view == null) {
            return false;
        }
        Context context = view.getContext();
        if (context instanceof Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (((Activity) context).isDestroyed()) {
                    mViewWeakReference.clear();
                    return false;
                }
            }
            if (((Activity) context).isFinishing()) {
                mViewWeakReference.clear();
                return false;
            }
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastTime > REFRESH_INTERVAL) {
            mLastTime = currentTime;
            view.invalidate();
        }
        return true;
    }
}
