package me.sunhapper.spcharedittool

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.module.AppGlideModule


/**
 * Create time: 2016/8/18.
 */
@com.bumptech.glide.annotation.GlideModule
class MyGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        // Do nothing.
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        //registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

}
