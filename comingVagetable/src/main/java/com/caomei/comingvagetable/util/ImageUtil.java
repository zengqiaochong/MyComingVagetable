package com.caomei.comingvagetable.util;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.caomei.comingvagetable.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageUtil {

    public static DisplayImageOptions roundImageOptions = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .displayer(new RoundedBitmapDisplayer(100))
            .bitmapConfig(Bitmap.Config.RGB_565).build();
    public static DisplayImageOptions roundCornerImageOptions = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .displayer(new RoundedBitmapDisplayer(3))
            .bitmapConfig(Bitmap.Config.RGB_565).build();
    public static DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
//			.displayer(new FadeInBitmapDisplayer(100))
            .bitmapConfig(Bitmap.Config.RGB_565).build();
    /**
     * 带默认图片的图片显示配置
     */
    public static DisplayImageOptions imageVegeOptions = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.none)
            .showImageOnFail(R.drawable.none)
            .showImageOnLoading(R.drawable.none)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .displayer(new FadeInBitmapDisplayer(300))
            .bitmapConfig(Bitmap.Config.RGB_565).build();
    //drawable资源图片显示配置
    public static DisplayImageOptions drawableOptions = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheInMemory(false)
            .cacheOnDisk(false)
            .considerExifParams(true)
//			.displayer(new FadeInBitmapDisplayer(100))
            .bitmapConfig(Bitmap.Config.RGB_565).build();

    public static void showImageFromUrl(String url,ImageView imageView,DisplayImageOptions mOption,ImageLoadingListener listener){
        ImageLoader.getInstance().displayImage(url,imageView,mOption,listener);
    }

    public static void showImageFromUrl(String url,ImageView imageView,DisplayImageOptions mOption){
        ImageLoader.getInstance().displayImage(url,imageView,mOption);
    }
    public static void showImageFromUrl(String url,ImageView imageView){
        ImageLoader.getInstance().displayImage(url,imageView,imageOptions);
    }
    /**
     * 从内存卡中异步加载本地图片
     *
     * @param uri
     * @param imageView
     */
    public static void showImageFromSDCard(String uri, ImageView imageView) {
        // String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
        ImageLoader.getInstance().displayImage("file://" + uri, imageView,imageOptions);
    }
    public static void catchImageFromUrl(String url,DisplayImageOptions options,ImageLoadingListener listener){
        ImageLoader.getInstance().loadImage(url, options, listener);
    }
    /**
     * 回收imageView的图片资源
     * @param imageView
     */
    public static void RecycleImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }
    public static DisplayImageOptions dressSuggestionImageOptions = new DisplayImageOptions.Builder()
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT).cacheInMemory(true)
            .cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
}
