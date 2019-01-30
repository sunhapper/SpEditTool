package com.sunhapper.x.spedit.gif.drawable;

import com.sunhapper.x.spedit.gif.listener.RefreshListener;

/**
 * Created by sunhapper on 2019/1/25 .
 */
public interface InvalidateDrawable {
    void addRefreshListener(RefreshListener callback);

    void removeRefreshListener(RefreshListener callback);
}
