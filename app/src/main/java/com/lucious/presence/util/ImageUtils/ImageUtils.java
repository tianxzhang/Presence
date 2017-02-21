package com.lucious.presence.util.ImageUtils;

import android.graphics.Bitmap;

import com.lucious.presence.BaseApplication;

/**
 * Created by ztx on 2017/1/25.
 */

public class ImageUtils {
    public static Bitmap getBitmap(String url) {
        Bitmap result = BaseApplication.memoryCache.getBitmapFromCache(url);
        if(result == null) {
            result = BaseApplication.fileCache.getImage(url);
            if(result == null) {
                result = ImageGetFromHttp.downloadBitmap(url);
                if(result == null) {
                    BaseApplication.fileCache.saveBitmap(result,url);
                }
            }
        }
    }
}
