package com.mnnyang.gzuclassschedule.utils;

import android.util.Log;

/**
 * 日志工具<br>
 * Created by mnnyang on 17-4-8.
 */

public class LogUtil {

    private static final String STRING = "------->";
    private static final String SUFFIX = "<-------";
    private static final boolean SHOW = false;

    //信息级别
    public static void i(Object tag, String msg) {
        if (!SHOW) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.i(newTag, msg);
    }

    //DEBUG级别
    public static void d(Object tag, String msg) {
        if (!SHOW) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.d(newTag, msg);
    }

    //警告级别
    public static void w(Object tag, String msg) {
        if (!SHOW) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.w(newTag, msg);
    }

    //详细
    public static void v(Object tag, String msg) {
        if (!SHOW) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.v(newTag, msg);
    }

    //错误级别
    public static void e(Object tag, String msg) {
        if (!SHOW) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.e(newTag, msg);
    }

    public static String getNewTag(Object tag) {
        String newTag = "";
        if (tag == null) {
            tag = "";
        }

        if (tag instanceof String) {
            newTag = (String) tag;
        } else if (tag instanceof Class) {
            newTag = ((Class) tag).getSimpleName();
        } else {
            newTag = tag.getClass().getSimpleName();
        }
        return STRING + newTag + SUFFIX;
    }
}
