package jan.jason.wanandroid.utils;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

/**
 * @Description: 定义GlideApp
 * @Author: jasonjan
 * @Date: 2018/9/4 17:37
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule{

    /**
     * 配置Glide参数
     * @param context
     * @param builder
     */
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565));
        int memoryCacheSizeBytes = 1024 * 1024 * 20;
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
        int bitmapPoolSizeBytes = 1024 * 1024 * 30;
        builder.setBitmapPool(new LruBitmapPool(bitmapPoolSizeBytes));
        int diskCacheSizeBytes = 1024 * 1024 * 100;
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));
    }
}
