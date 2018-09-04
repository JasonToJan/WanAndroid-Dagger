package jan.jason.wanandroid.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * @Description: 图片加载器
 * @Author: jasonjan
 * @Date: 2018/9/3 18:28
 */
public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object o, ImageView imageView) {
        Glide.with(context).load(o).into(imageView);
    }
}
