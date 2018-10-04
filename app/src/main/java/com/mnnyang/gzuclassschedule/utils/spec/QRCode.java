package com.mnnyang.gzuclassschedule.utils.spec;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.google.zxing.WriterException;
import com.yzq.zxinglibrary.encode.CodeCreator;

/*
 * contentEtString：字符串内容
 * w：图片的宽
 * h：图片的高
 * logo：不需要logo的话直接传null
 * */
public class QRCode {
    public Bitmap makeQRCodeImage(String content, int width, int height, Bitmap logo) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        String contentEtString = content.trim();

        if (contentEtString.length() == 0) {
            return null;
        }

        try {
            return CodeCreator.createQRCode(contentEtString, width, height, logo);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
