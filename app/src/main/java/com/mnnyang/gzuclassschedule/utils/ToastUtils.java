package com.mnnyang.starmusic.util.general;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


/**
 * Toast 工具类<br>
 * 需要init<br>
 * Created by mnnyang on 17-4-8.
 */
public class ToastUtils {

    public static Context context;
    private static Toast toast;

    private static Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 初始化Toast工具类<br>
     */
    public static void init(Context context) {
        ToastUtils.context = context.getApplicationContext();
    }

    public static void show(int resId) {
        show(context.getString(resId));
    }

    public static void show(String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                toast.show();
            }
        });
    }
}
