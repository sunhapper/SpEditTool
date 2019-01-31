package com.sunhapper.x.spedit.view;

import android.text.Editable;
import android.text.Selection;
import android.view.KeyEvent;

import com.sunhapper.x.spedit.mention.span.IntegratedSpan;

/**
 * Created by sunhapper on 2019/1/25 .
 */
public class SelectDeleteKeyEventProxy implements KeyEventProxy {
    @Override
    public boolean onKeyEvent(KeyEvent keyEvent, Editable text) {
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            int selectionStart = Selection.getSelectionStart(text);
            int selectionEnd = Selection.getSelectionEnd(text);
            if (selectionEnd != selectionStart) {
                return false;
            }
            IntegratedSpan[] integratedSpans = text.getSpans(selectionStart, selectionEnd, IntegratedSpan.class);
            if (integratedSpans != null && integratedSpans.length > 0) {
                IntegratedSpan span = integratedSpans[0];
                int spanStart = text.getSpanStart(span);
                int spanEnd = text.getSpanEnd(span);
                if (spanEnd == selectionStart) {
                    //使用setSelection会造成点击字符串后部光标移到末尾之后再点删除，经常光标会跑到字符串前面而没有选中效果，原因未知
                    //bug环境 小米note miui9.2 android 6.0.1
                    //手上设备不多暂时只有这一台设备出现此问题。。。
                    Selection.setSelection(text, spanStart, spanEnd);
                    return true;
                }
            }
        }
        return false;
    }
}
