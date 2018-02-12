package com.sunhapper.spedittool.view;

import android.annotation.SuppressLint;
import android.support.annotation.GuardedBy;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Editable.Factory;

/**
 * Created by sunha on 2018/2/12 0012.
 */

final class ImageEditableFactory extends Factory {

  private static final Object sInstanceLock = new Object();
  @GuardedBy("sInstanceLock")
  private static volatile Factory sInstance;
  @Nullable
  private static Class<?> sWatcherClass;

  @SuppressLint({"PrivateApi"})
  private ImageEditableFactory() {
    try {
      String className = "android.text.DynamicLayout$ChangeWatcher";
      sWatcherClass = this.getClass().getClassLoader().loadClass(className);
    } catch (Throwable var2) {
      ;
    }

  }

  public static Factory getInstance() {
    if (sInstance == null) {
      Object var0 = sInstanceLock;
      synchronized (sInstanceLock) {
        if (sInstance == null) {
          sInstance = new ImageEditableFactory();
        }
      }
    }

    return sInstance;
  }

  public Editable newEditable(@NonNull CharSequence source) {
    return (Editable) (sWatcherClass != null ? SpannableBuilder.create(sWatcherClass, source)
        : super.newEditable(source));
  }
}
