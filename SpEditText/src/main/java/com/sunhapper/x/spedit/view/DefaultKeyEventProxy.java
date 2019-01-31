package com.sunhapper.x.spedit.view;

import android.text.Editable;
import android.text.Selection;
import android.view.KeyEvent;

import com.sunhapper.x.spedit.mention.span.IntegratedSpan;

/**
 * Created by sunhapper on 2019/1/25 .
 */
public class DefaultKeyEventProxy implements KeyEventProxy {
    @Override
    public boolean onKeyEvent(KeyEvent keyEvent, Editable text) {
        //处理删除事件
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            int selectionStart = Selection.getSelectionStart(text);
            int selectionEnd = Selection.getSelectionEnd(text);
            if (selectionEnd != selectionStart) {
                return false;
            }
            IntegratedSpan[] integratedSpans = text.getSpans(selectionStart, selectionEnd, IntegratedSpan.class);
            if (integratedSpans != null && integratedSpans.length > 0) {
                //部分设备上的span是无序的，所以需要遍历一遍找可以删除的span
                for (IntegratedSpan span : integratedSpans) {
                    int spanStart = text.getSpanStart(span);
                    int spanEnd = text.getSpanEnd(span);
                    if (spanEnd == selectionStart) {
                        text.delete(spanStart, spanEnd);
                        return true;
                    }
                }
            }
        }

        //处理光标左移事件
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT
                && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

            int selectionStart = Selection.getSelectionStart(text);
            int selectionEnd = Selection.getSelectionEnd(text);
            IntegratedSpan[] integratedSpans = text.getSpans(selectionEnd, selectionEnd, IntegratedSpan.class);
            if (integratedSpans != null && integratedSpans.length > 0) {
                for (IntegratedSpan span : integratedSpans) {
                    int spanStart = text.getSpanStart(span);
                    int spanEnd = text.getSpanEnd(span);
                    //selectionEnd表示主动移动的光标
                    if (spanEnd == selectionEnd) {
                        Selection.setSelection(text, selectionStart, spanStart);
                        return true;
                    }
                }
            }
        }
        //处理光标右移事件
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT
                && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            int selectionStart = Selection.getSelectionStart(text);
            int selectionEnd = Selection.getSelectionEnd(text);
            IntegratedSpan[] integratedSpans = text.getSpans(selectionEnd, selectionEnd, IntegratedSpan.class);
            if (integratedSpans != null && integratedSpans.length > 0) {
                for (IntegratedSpan span : integratedSpans) {
                    int spanStart = text.getSpanStart(span);
                    int spanEnd = text.getSpanEnd(span);
                    if (spanStart == selectionEnd) {
                        Selection.setSelection(text, selectionStart, spanEnd);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
