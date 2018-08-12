package com.mark.moveandswipervitem;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

/**
 * Created by mark on 18-8-12.
 */

public class ImageLoader {

    public static void displayImage(String path, ImageView imageView,int w, int h,boolean round){
        if (round){
           displayRoundImage(path,imageView,w,h);
        }else {
            displayImage(path,imageView,w,h);
        }
    }

    public static void displayImage(String path, ImageView imageView,int w, int h){
        if (path != null && imageView != null){
            Bitmap bitmap = LruCacheManager.getInstance().get(path);
            if (bitmap != null){
                imageView.setImageBitmap(bitmap);
            }else {
                bitmap= BitmapUtil.decodeFile(path,w,h);
                if (bitmap != null){
                    imageView.setImageBitmap(bitmap);
                    LruCacheManager.getInstance().put(path,bitmap);
                }
            }
        }
    }

    public static void displayRoundImage(String path, ImageView imageView,int w,int h){
        if (path != null && imageView != null){
            Bitmap bitmap = LruCacheManager.getInstance().get(path);
            if (bitmap != null){
                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(imageView.getResources(),bitmap);
                if (drawable != null){
                    drawable.setCornerRadius(50);
                    imageView.setImageDrawable(drawable);
                }
            }else {
                bitmap= BitmapUtil.decodeFile(path,w,h);
                if (bitmap != null){
                    LruCacheManager.getInstance().put(path,bitmap);
                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(imageView.getResources(),bitmap);
                    if (drawable != null){
                        drawable.setCornerRadius(50);
                        imageView.setImageDrawable(drawable);
                    }
                }
            }
        }
    }
}
