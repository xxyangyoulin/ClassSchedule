package com.mnnyang.gzuclassschedule.custom.course;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.custom.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class CourseView extends FrameLayout {

    private int mWidth;
    private int mHeight;

    private int mRowCount = 7;
    private int mColCount = 16;

    private int mRowItemWidth = dip2px(50);
    private int mColItemHeight = dip2px(60);

    private int mCurrentIndex = 1;
    private String mNotCurrentPrefix = "[非本周]";

    /** 行item的宽度根据view的总宽度自动平均分配 */
    private boolean mRowItemWidthAuto = true;

    List<CourseAncestor> mCourseList = new ArrayList<>();

    private CourseAncestor mAddTagCourse;
    private View mAddTagCourseView;

    /** item view radius */
    private float mCourseItemRadius = 0;

    private Paint mLinePaint;
    private Path mLinePath = new Path();

    /** 显示垂直分割线 */
    private boolean mShowVerticalLine = false;

    /** 显示水平分割线 */
    private boolean mShowHorizontalLine = true;

    /** 第一次绘制 */
    private boolean mFirstDraw;

    /** text */
    private int mTextLRPadding = dip2px(2);
    private int mTextTBPadding = dip2px(4);
    private int textTBMargin = dip2px(3);
    private int textLRMargin = textTBMargin;
    private int mTextColor = Color.WHITE;
    private int mTextSize = 12;

    /** 不活跃的背景 */
    private int mInactiveBackgroundColor = 0xFFE3EEF5;
    private int mInactiveTextColor = 0xFFbadac9;

    public CourseView(@NonNull Context context) {
        super(context);
    }

    public CourseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        l("调用init");
        initPaint();
    }

    private void initPaint() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.LTGRAY);
        mLinePaint.setStrokeWidth(1);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        l("调用onMeasure");

        mHeight = mColItemHeight * mColCount;
        int heightResult = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);

        setMeasuredDimension(widthMeasureSpec, heightResult);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mRowItemWidthAuto) {
            mWidth = w;
            mRowItemWidth = mWidth / mRowCount;
        } else {
            mWidth = mRowItemWidth * mRowCount;
        }
    }

    /** 把数组中的数据全部添加到界面 */
    private void initCourseItemView() {
        l("调用initCourseItemView");

        for (CourseAncestor course : mCourseList) {
            realAddCourseItemView(course);
        }
    }

    /** 在界面初始化之后添加数据 */
    public void addCourse(CourseAncestor course) {
        if (course == null) {
            return;
        }

        if (!mCourseList.contains(course)) {
            mCourseList.add(course);
            realAddCourseItemView(course);
        }
    }

    private void realAddCourseItemView(CourseAncestor course) {
        updateItemStatus(course);

        View itemView = createItemView(course);

        LayoutParams params = new LayoutParams(mRowItemWidth,
                mColItemHeight * course.getRowNum());

        params.leftMargin = (course.getRow() - 1) * mRowItemWidth;
        params.topMargin = (course.getCol() - 1) * mColItemHeight;

        itemView.setLayoutParams(params);

        if (!course.isDisplayable()) {
            return;
        }

        if (course.getActiveStatus()) {
            addView(itemView);
        } else {
            addView(itemView, 0);
        }
    }

    private void updateItemStatus(CourseAncestor course) {
        /*更新course的活跃状态*/
        course.setActiveStatus(course.shouldShow(mCurrentIndex));
    }

    private void setItemViewBackground(CourseAncestor course, TextView tv) {
        StateListDrawable drawable;

        if (course.getActiveStatus()) {
            drawable = getShowBgDrawable(course.getColor(), course.getColor() & 0x80FFFFFF);
        } else {
            drawable = getShowBgDrawable(mInactiveBackgroundColor, mInactiveBackgroundColor & 0x80FFFFFF);
            tv.setTextColor(mInactiveTextColor);
        }
        tv.setBackground(drawable);

    }

    private StateListDrawable getShowBgDrawable(int color, int color2) {
        return Utils.getPressedSelector(getContext(),
                color, color2, mCourseItemRadius);
    }

    @NonNull
    private TextView getCourseTextView(int h, int w) {
        TextView tv = new TextView(getContext());
        LayoutParams params = new LayoutParams(w, h);
        tv.setLayoutParams(params);

        tv.setTextColor(mTextColor);
        tv.setLineSpacing(-2, 1);
        tv.setPadding(mTextLRPadding, mTextTBPadding, mTextLRPadding, mTextTBPadding);
        tv.setTextColor(mTextColor);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
        tv.setFocusable(true);
        tv.setClickable(true);
        //bold
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
        return tv;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        drawLine(canvas);
        super.dispatchDraw(canvas);


        if (!mFirstDraw) {
            initCourseItemView();
            mFirstDraw = true;
        }
    }

    private void drawLine(Canvas canvas) {
        //横线
        if (mShowHorizontalLine) {
            for (int i = 1; i < mColCount; i++) {
                mLinePath.reset();
                mLinePath.moveTo(0, i * mColItemHeight);
                mLinePath.lineTo(mWidth, i * mColItemHeight);
                canvas.drawPath(mLinePath, mLinePaint);
            }
        }

        //竖线
        if (mShowVerticalLine) {
            for (int i = 1; i < mRowCount; i++) {
                mLinePath.reset();
                mLinePath.moveTo(i * mRowItemWidth, 0);
                mLinePath.lineTo(i * mRowItemWidth, mHeight);
                canvas.drawPath(mLinePath, mLinePaint);
            }
        }
    }

    /*事件*/
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("COurseView", "onTouchEvent");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true; //TODO why?

            case MotionEvent.ACTION_MOVE:
                removeAddTagView();
                break;

            case MotionEvent.ACTION_UP:
                addTagCourseView((int) event.getX(), (int) event.getY());
                break;
        }

        return super.onTouchEvent(event);
    }

    private void addTagCourseView(int x, int y) {

        /*找到点击的方框坐标*/
        int x1 = x / mRowItemWidth + 1;
        int y1 = y / mColItemHeight + 1;

        if (x1 > mRowCount) x1 = mRowCount;

        if (y1 > mColCount) y1 = mColCount;

        if (mAddTagCourse == null)
            mAddTagCourse = new CourseAncestor();

        if (mAddTagCourseView == null)
            mAddTagCourseView = createAddTagView();
        else removeView(mAddTagCourseView);

        mAddTagCourse.setRow(x1);
        mAddTagCourse.setCol(y1);

        realAddTagCourseView();
    }

    /**
     * 移除添加按钮
     */
    public void removeAddTagView() {
        if (mAddTagCourseView != null) {
            removeView(mAddTagCourseView);
        }
    }

    public void resetView() {
        removeAllViews();
        initCourseItemView();
    }

    /**
     * 建立添加按钮
     */
    private View createAddTagView() {
        final BackgroundView bgLayout = new BackgroundView(getContext());

        ImageView iv = new ImageView(getContext());

        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(textLRMargin, textTBMargin, textLRMargin, textTBMargin);
        iv.setLayoutParams(params);


        iv.setImageResource(R.drawable.ic_svg_add);
        iv.setScaleType(ImageView.ScaleType.CENTER);
        iv.setBackgroundColor(Color.LTGRAY);

        StateListDrawable pressedSelector = Utils.getPressedSelector(getContext(),
                Color.LTGRAY, Color.LTGRAY, mCourseItemRadius);
        iv.setBackground(pressedSelector);
        iv.setClickable(true);
        iv.setFocusable(true);

        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onAdd(mAddTagCourse, mAddTagCourseView);
                    removeAddTagView();
                }
            }
        });

        bgLayout.addView(iv);
        return bgLayout;
    }

    /**
     * 建立itemview
     */
    @SuppressLint("ClickableViewAccessibility")
    private View createItemView(final CourseAncestor course) {
        final BackgroundView bgLayout = new BackgroundView(getContext());
        //TextView
        final TextView tv = getCourseTextView(mColItemHeight * course.getRowNum(), mRowItemWidth);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(textLRMargin, textTBMargin, textLRMargin, textTBMargin);
        tv.setLayoutParams(params);

        String showText = course.getActiveStatus() ? course.getText()
                : mNotCurrentPrefix + course.getText();
        tv.setText(showText);

        bgLayout.addView(tv);
        setItemViewBackground(course, tv);
        itemEvent(course, bgLayout, tv);

        return bgLayout;
    }

    private void itemEvent(final CourseAncestor course, final ViewGroup viewGroup, TextView textView) {
        final List<CourseAncestor> courses = new ArrayList<>();
        courses.add(course);

        /*查找在点击的item范围内重叠的item*/
        for (CourseAncestor findCourse : mCourseList) {
            if (findCourse.getRow() == course.getRow()
                    && course != findCourse) {

                if (findCourse.getCol() < course.getCol() + course.getRowNum()
                        && findCourse.getCol() >= course.getCol()) {
                    courses.add(findCourse);
                }
            }
        }

        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAddTagView();
                if (mItemClickListener != null) {
                    mItemClickListener.onClick(courses, viewGroup);
                }
            }
        });

        textView.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                removeAddTagView();
                if (mItemClickListener != null) {
                    mItemClickListener.onLongClick(courses, viewGroup);
                    return true;
                }
                return false;
            }
        });
    }

    private void realAddTagCourseView() {
        LayoutParams params = new LayoutParams(mRowItemWidth,
                mColItemHeight * mAddTagCourse.getRowNum());

        params.leftMargin = (mAddTagCourse.getRow() - 1) * mRowItemWidth;
        params.topMargin = (mAddTagCourse.getCol() - 1) * mColItemHeight;

        mAddTagCourseView.setLayoutParams(params);
        addView(mAddTagCourseView);
    }

    private OnItemClickListener mItemClickListener;

    public void clear() {
        removeAllViews();
        mCourseList.clear();
    }

    public abstract static class OnItemClickListener {
        public abstract void onClick(List<CourseAncestor> course, View itemView);

        public abstract void onLongClick(List<CourseAncestor> course, View itemView);

        public abstract void onAdd(CourseAncestor course, View addView);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public int dip2px(float dpValue) {
        return (int) (0.5f + dpValue * getContext().getResources().getDisplayMetrics().density);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public int sp2px(float spValue) {
        return (int) (0.5f + spValue * getContext().getResources().getDisplayMetrics().scaledDensity);
    }

    public static void l(String s) {
        System.out.println("------------------" + s);
    }

    /***************************************************/

    public int getRowCount() {
        return mRowCount;
    }

    public CourseView setRowCount(int rowCount) {
        mRowCount = rowCount;
        return this;
    }

    public int getColCount() {
        return mColCount;
    }

    public CourseView setColCount(int colCount) {
        mColCount = colCount;
        return this;
    }

    public int getRowItemWidth() {
        return mRowItemWidth;
    }

    public CourseView setRowItemWidth(int rowItemWidth) {

        mRowItemWidth = rowItemWidth;
        return this;
    }

    public int getColItemHeight() {
        return mColItemHeight;
    }

    public CourseView setColItemHeight(int colItemHeight) {
        mColItemHeight = colItemHeight;
        return this;
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public CourseView setCurrentIndex(int currentIndex) {
        this.mCurrentIndex = currentIndex;
        postInvalidate();
        return this;
    }

    public boolean isRowItemWidthAuto() {
        return mRowItemWidthAuto;
    }

    public CourseView setRowItemWidthAuto(boolean rowItemWidthAuto) {
        mRowItemWidthAuto = rowItemWidthAuto;
        return this;
    }

    public float getCourseItemRadius() {
        return mCourseItemRadius;
    }

    public CourseView setCourseItemRadius(float courseItemRadius) {
        mCourseItemRadius = courseItemRadius;
        postInvalidate();
        return this;
    }

    public boolean isShowVerticalLine() {
        return mShowVerticalLine;
    }

    public CourseView setShowVerticalLine(boolean showVerticalLine) {
        mShowVerticalLine = showVerticalLine;
        return this;
    }

    public boolean isShowHorizontalLine() {
        return mShowHorizontalLine;
    }

    public CourseView setShowHorizontalLine(boolean showHorizontalLine) {
        mShowHorizontalLine = showHorizontalLine;
        return this;
    }

    public int getTextLRPadding() {
        return mTextLRPadding;
    }

    public CourseView setTextLRPadding(int textLRPadding, int textTBPadding) {
        mTextLRPadding = textLRPadding;
        mTextTBPadding = textTBPadding;

        return this;
    }

    public int getTextTBPadding() {
        return mTextTBPadding;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public CourseView setTextColor(int textColor) {
        mTextColor = textColor;
        return this;
    }

    public int getInactiveBackgroundColor() {
        return mInactiveBackgroundColor;
    }

    public CourseView setInactiveBackgroundColor(int inactiveBackgroundColor) {
        mInactiveBackgroundColor = inactiveBackgroundColor;
        return this;
    }

    public CourseView setTextTBMargin(int textTBMargin, int textLRMargin) {
        this.textTBMargin = textTBMargin;
        this.textLRMargin = textLRMargin;
        return this;
    }

    public int getTextTBMargin() {
        return textTBMargin;
    }

    public int getTextLRMargin() {
        return textLRMargin;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public CourseView setTextSize(int textSize) {
        mTextSize = textSize;
        return this;
    }
}
