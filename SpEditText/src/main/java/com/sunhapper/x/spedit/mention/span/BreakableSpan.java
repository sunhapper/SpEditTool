package com.sunhapper.x.spedit.mention.span;

/**
 * Created by sunhapper on 2019/1/24 .
 * 标记可以被从中间删除的span
 */
public interface BreakableSpan extends MarkSpan {

    boolean isBreak();
}
