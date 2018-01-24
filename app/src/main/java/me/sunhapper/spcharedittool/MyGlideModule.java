package me.sunhapper.spcharedittool;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.module.AppGlideModule;


/**
 * Create time: 2016/8/18.
 */
@com.bumptech.glide.annotation.GlideModule
public final class MyGlideModule extends AppGlideModule {

  @Override
  public void applyOptions(Context context, GlideBuilder builder) {
    // Do nothing.
  }

  @Override
  public void registerComponents(Context context, Glide glide, Registry registry) {
    //registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
  }

  @Override
  public boolean isManifestParsingEnabled() {
    return false;
  }

}
