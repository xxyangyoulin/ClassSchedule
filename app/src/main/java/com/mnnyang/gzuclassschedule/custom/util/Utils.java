package com.mnnyang.gzuclassschedule.custom.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

import com.mnnyang.gzuclassschedule.R;

import java.util.Random;


/**
 * shape选择器生成工具
 * Created by mnnyang on 17-10-22.
 */

public class Utils {
    private static int[] colorList = new int[]{
            0xFF8AD297,
            0xFFF9A883,
            0xFF88CFCC,
            0xFFF19C99,
            0xFFF7C56B,
            0xFFD2A596,
            0xFF67BDDE,
            0xFF9CCF5A,
            0xFF9AB4CF,
            0xFFE593AD,
            0xFFE2C38A,
            0xFFB29FD2,
            0xFFE2C490,
            0xFFE2C490,
    };

    private static int[] darkColorList = new int[]{
            0xFF5ABF6C,
            0xFFF79060,
            0xFF63C0BD,
            0xFFED837F,
            0xFFF5B94E,
            0xFFCA9483,
            0xFF31A6D3,
            0xFF8BC73D,
            0xFF87A6C6,
            0xFFDF7999,
            0xFFD6A858,
            0xFF997FC3,
            0xFFDDB97B,
            0xFFd3dEe5};

    public static GradientDrawable
    getDrawable(Context context, int rgb,
                float radius, int stroke, int strokeColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(rgb);
        gradientDrawable.setCornerRadius(dip2px(context, radius));
        gradientDrawable.setStroke(dip2px(context, stroke), strokeColor);
        return gradientDrawable;
    }

    public static StateListDrawable
    getPressedSelector(Context context, int color, int pressedColor, float radius) {
        GradientDrawable normalD = getDrawable(context, color, radius, 0, 0);
        GradientDrawable pressedD = getDrawable(context, pressedColor, radius, 0, 0);

        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, pressedD);
        drawable.addState(new int[]{}, normalD);
        return drawable;
    }
    private static Random random = new Random();

    public static int getRandomColor() {
        return colorList[random.nextInt(20) % colorList.length];
    }

    public static int getDarkRandomColor() {
        return darkColorList[random.nextInt(20) % colorList.length];
    }

    public static int getColor(Resources resources, int colorId) {
        return resources.getColor(colorId);
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) (0.5f + dpValue * context.getResources().getDisplayMetrics().density);
    }
}


