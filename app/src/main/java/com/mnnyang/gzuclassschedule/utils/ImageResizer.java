package com.mnnyang.gzuclassschedule.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;


import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by mnnyang on 17-4-11.
 */

public class ImageResizer {
    public static final String TAG = "ImageResizer";

    /**
     * 压缩加载图片 从文件<br>
     */
    public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {
        if (TextUtils.isEmpty(filePath)){
            LogUtil.w(JsonReader.class, "decodeSampledBitmapFromFile path is null");
            return null;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            return decodeSampledBitmapFromDescriptor(fis.getFD(), reqWidth, reqHeight);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 压缩加载图片 从文件<br>
     */
    public static Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor descriptor, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(descriptor, null, options);
        int inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(descriptor, null, options);
    }


    /**
     * 压缩加载图片 从资源<br>
     */
    public static Bitmap decodeSampledBitmapFromResources(Resources res, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        int inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 计算 options.inSampleSize
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqHeight == 0 || reqWidth == 0) {
            return 1;
        }

        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.i(TAG, "h=" + height + " w=" + width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.i(TAG, "inSampleSize=" + inSampleSize);
        return inSampleSize;
    }
}

