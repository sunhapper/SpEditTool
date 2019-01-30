package com.sunhapper.x.spedit.mention.span;

import android.text.Spannable;

/**
 * Created by sunhapper on 2019/1/24 .
 * 标记可以从中间删除的span
 */
public interface BreakableSpan extends DataSpan {

    boolean isBreak(Spannable spannable);
}
