package com.sunhapper.x.spedit.view;

import android.content.Context;
import android.text.NoCopySpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

import com.sunhapper.x.spedit.gif.watcher.GifWatcher;
import com.sunhapper.x.spedit.mention.watcher.SpanChangedWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunhapper on 2019/1/25 .
 */
public class SpXEditText extends android.support.v7.widget.AppCompatEditText {
    private KeyEventProxy mKeyEventProxy = new DefaultKeyEventProxy();

    public SpXEditText(Context context) {
        super(context);
    }

    public SpXEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpXEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        List<NoCopySpan> watchers = new ArrayList<>();
        watchers.add(new SpanChangedWatcher());
        watchers.add(new GifWatcher(this));
        setEditableFactory(new SpXFactory(watchers));
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return (mKeyEventProxy != null && mKeyEventProxy.onKeyEvent(event, getText()));
            }
        });
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new CustomInputConnectionWrapper(super.onCreateInputConnection(outAttrs), true);
    }


    /**
     * 解决google输入法删除不走OnKeyListener()回调问题
     */
    private class CustomInputConnectionWrapper extends InputConnectionWrapper {


        /**
         * Initializes a wrapper.
         *
         * <p><b>Caveat:</b> Although the system can accept {@code (InputConnection) null} in some
         * places, you cannot emulate such a behavior by non-null {@link InputConnectionWrapper} that
         * has {@code null} in {@code target}.</p>
         *
         * @param target  the {@link InputConnection} to be proxied.
         * @param mutable set {@code true} to protect this object from being reconfigured to target
         *                another {@link InputConnection}.  Note that this is ignored while the target is {@code
         *                null}.
         */
        public CustomInputConnectionWrapper(InputConnection target, boolean mutable) {
            super(target, mutable);
        }


        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,
                        KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            return (mKeyEventProxy != null && mKeyEventProxy.onKeyEvent(event, getText()))
                    || super.sendKeyEvent(event);
        }
    }

    public void setKeyEventProxy(KeyEventProxy keyEventProxy) {
        mKeyEventProxy = keyEventProxy;
    }

}
