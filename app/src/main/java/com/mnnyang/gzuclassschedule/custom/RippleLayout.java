package com.mnnyang.gzuclassschedule.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.custom.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * xxyangyoulin
 * 2018年10月06日13:37:22
 */
public class RippleLayout extends LinearLayout {

    private Paint mPaint;
    private Random mRandom;
    private List<Circle> mCircles;
    private int mWidth;
    private int mHeight;

    /**
     * 圆圈的个数
     */
    private int mCircleMax = 20;

    public RippleLayout(Context context) {
        super(context);
        init();
    }

    public RippleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RippleLayout);
        mCircleMax = array.getInteger(R.styleable.RippleLayout_count, mCircleMax);
        array.recycle();
    }

    private void init() {
        mRandom = new Random();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Utils.getDarkRandomColor());
        setBackgroundColor(0x20ffffff);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        initCircle();
    }

    private void initCircle() {
        if (mCircles == null) {
            mCircles = new ArrayList<>();
        } else {
            mCircles.clear();
        }

        for (int i = 0; i < mCircleMax; i++) {
            mCircles.add(resetCircle(new Circle(),true));
        }
    }

    @Override
    public void draw(Canvas canvas) {
        for (Circle circle : mCircles) {
            mPaint.setColor(circle.color | (((int) (0xFF * (1 - circle.currRadius * 1f / circle.maxRadius))) << 24));
            canvas.drawCircle(circle.x, circle.y, circle.currRadius, mPaint);
            circle.currRadius += 1;
            if (circle.currRadius > circle.maxRadius) {
                resetCircle(circle, false);
            }
        }
        super.draw(canvas);

        postDelayed(action, 16);
    }

    private Circle resetCircle(Circle circle, boolean init) {
        circle.currRadius = 0;
        circle.x = mRandom.nextInt(mWidth);
        circle.y = mRandom.nextInt(mHeight);
        circle.maxRadius = mRandom.nextInt(120) + 50;
        circle.setColor(Utils.getDarkRandomColor());

        if(init){
            circle.currRadius = mRandom.nextInt(circle.maxRadius);
        }

        return circle;
    }

    Runnable action = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    static class Circle {
        int x;
        int y;
        int maxRadius;
        int currRadius = 0;
        int color;

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color & 0xFFFFFF;
        }
    }
}
