package me.sunhapper.spcharedittool;

import android.app.Application;
import me.sunhapper.spcharedittool.emoji.EmojiManager;

/**
 * Created by sunha on 2018/2/5 0005.
 */

public class SpApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    EmojiManager.getInstance().init(this);
  }
}
