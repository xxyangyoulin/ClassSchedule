package com.mnnyang.gzuclassschedule.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AppUtils {
    public static String getGravatar(String email) {
        String emailMd5 = AppUtils.md5Hex(email);        //设置图片大小32px
        String avatar = "http://www.gravatar.com/avatar/" + emailMd5 + "?s=64";
        System.out.println(avatar);
        return avatar;
    }

    public static void updateWidget(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.mnnyang.action.UPDATE_WIDGET");
        intent.setComponent(new ComponentName("com.mnnyang.gzuclassschedule", "com.mnnyang.gzuclassschedule.widget.MyWidget"));
        context.sendBroadcast(intent);
    }

    public static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i]
                    & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }
    public static String md5Hex (String message) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return hex (md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return null;
    }
}
