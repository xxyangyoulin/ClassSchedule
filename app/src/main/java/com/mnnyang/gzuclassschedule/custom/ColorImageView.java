package com.mnnyang.gzuclassschedule.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.mnnyang.gzuclassschedule.R;

/**
 * ColorImageView
 * Created by mnnyang on 17-6-1.
 */

@SuppressLint("AppCompatCustomView")
public class ColorImageView extends ImageView {

    int bgColor = -1;
    Paint bgPaint;
    private int height;
    private int width;
    private int cx;
    private int cy;
    private int radius;

    public ColorImageView(Context context) {
        super(context);
        initPaint();
    }

    public ColorImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorImageView);
        int color = array.getColor(R.styleable.ColorImageView_bgcolor, Color.BLUE);
        setBgColor(color);
        array.recycle();
    }

    private void initPaint() {
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setDither(true);
        bgPaint.setColor(Color.TRANSPARENT);
        bgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getMeasuredHeight();
        width = getMeasuredWidth();

        cx = width / 2;
        cy = height / 2;
        radius = height > width ? width / 2 : height / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (bgColor != -1) {
            bgPaint.setColor(bgColor);
        } else {
            bgPaint.setColor(Color.TRANSPARENT);
        }

        canvas.drawCircle(cx, cy, radius, bgPaint);
        super.onDraw(canvas);
    }

    public void setBgColor(int color) {
        bgColor = color | 0xFF000000;
        bgPaint.setColor(bgColor);
        invalidate();
    }

    public int getBgColor() {
        return bgColor;
    }
}