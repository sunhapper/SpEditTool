package com.sunhapper.x.spedit.mention.span;

import android.text.Spannable;

/**
 * Created by sunhapper on 2019/1/24 .
 * 标记可以从中间删除的span
 * 与IntegratedSpan互斥
 */
public interface BreakableSpan {

    boolean isBreak(Spannable spannable);
}
