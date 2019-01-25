package com.sunhapper.x.spedit.view;

import android.view.KeyEvent;

/**
 * Created by sunhapper on 2019/1/25 .
 */
public class DefaultKeyEventProxy implements KeyEventProxy  {
    @Override
    public boolean onKeyEvent(KeyEvent keyEvent, CharSequence text) {
        return false;
    }
}
