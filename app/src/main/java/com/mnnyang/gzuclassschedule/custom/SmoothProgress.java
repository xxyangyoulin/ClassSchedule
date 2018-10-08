package com.mnnyang.gzuclassschedule.custom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.mnnyang.gzuclassschedule.R;


public class SmoothProgress extends View {

    int lastProgress = 0;
    int progress = 0;
    private int mHeight;
    private float cellWidth;
    private Paint mPaint;
    private ValueAnimator mValueAnimator;

    public SmoothProgress(Context context) {
        super(context);
        init();

    }

    public SmoothProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmoothProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.accent_green));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        cellWidth = (width / 100.0f);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRoundRect(0, 0, cellWidth * progress, mHeight, 10, 10, mPaint);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {

        System.out.println(progress);
        if (progress > 100) {
            progress = 100;
        }

        if (progress < lastProgress) {
            lastProgress = 0;
        } else {
            lastProgress = this.progress;
        }


        SmoothProgress.this.setVisibility(VISIBLE);

        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }

        mValueAnimator = ValueAnimator.ofInt(lastProgress, progress);
        mValueAnimator.setInterpolator(new DecelerateInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                SmoothProgress.this.progress = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (SmoothProgress.this.progress == 100) {
                    SmoothProgress.this.setVisibility(GONE);
                    lastProgress = 0;
                    SmoothProgress.this.progress = 0;
                }
            }

        });


        int cellTime = progress < 30 ? 2000 : (progress >= 100 ? 10 : 50);
        int duration = cellTime * (progress - lastProgress);

        //In order to prevent outside
        if (duration < 0) {
            duration = 300;
        }
        mValueAnimator.setDuration(duration);
        mValueAnimator.start();

    }
}