package com.sunhapper.x.spedit.view

import android.text.Editable
import android.view.KeyEvent

/**
 * Created by sunhapper on 2019/1/25 .
 */
interface KeyEventProxy {

    fun onKeyEvent(keyEvent: KeyEvent, text: Editable): Boolean
}
