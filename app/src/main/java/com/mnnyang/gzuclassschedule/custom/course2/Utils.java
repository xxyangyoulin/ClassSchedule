package com.mnnyang.gzuclassschedule.custom.course2;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class Utils {
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

    public static int dip2px(Context context, float dpValue) {
        return (int) (0.5f + dpValue * context.getResources().getDisplayMetrics().density);
    }
}


