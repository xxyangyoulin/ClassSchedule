package com.mnnyang.gzuclassschedule.utils;

import android.content.Context;

/**
 * 配置储存工具<br>
 * 需要init<br>
 * Created by mnnyang on 17-4-8.
 */

public class Preferences {
    public static Context context;
    public static String configFileName = "configFileName";

    public static void init(Context context) {
        Preferences.context = context.getApplicationContext();
        configFileName = context.getPackageName();
    }

    public static void putBoolean(String key, boolean value) {
        context.getSharedPreferences(configFileName, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, value)
                .commit();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return context.getSharedPreferences(configFileName, Context.MODE_PRIVATE)
                .getBoolean(key, defValue);
    }

    public static void putString(String key, String value) {
        context.getSharedPreferences(configFileName, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .commit();
    }

    public static String getString(String key, String defValue) {
        return context.getSharedPreferences(configFileName, Context.MODE_PRIVATE)
                .getString(key, defValue);
    }


    public static void putFloat(String key, float value) {
        context.getSharedPreferences(configFileName, Context.MODE_PRIVATE)
                .edit()
                .putFloat(key, value)
                .commit();
    }

    public static float getFloat(String key, float defValue) {
        return context.getSharedPreferences(configFileName, Context.MODE_PRIVATE)
                .getFloat(key, defValue);
    }

    public static void putInt(String key, int value) {
        context.getSharedPreferences(configFileName, Context.MODE_PRIVATE)
                .edit()
                .putInt(key, value)
                .commit();
    }

    public static int getInt(String key, int defValue) {
        return context.getSharedPreferences(configFileName, Context.MODE_PRIVATE)
                .getInt(key, defValue);
    }

    public static void putLong(String key, long value) {
        context.getSharedPreferences(configFileName, Context.MODE_PRIVATE)
                .edit()
                .putLong(key, value)
                .commit();
    }

    public static long getLong(String key, long defValue) {
        return context.getSharedPreferences(configFileName, Context.MODE_PRIVATE)
                .getLong(key, defValue);
    }

}