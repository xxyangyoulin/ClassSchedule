package com.mnnyang.gzuclassschedule.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mnnyang.gzuclassschedule.data.bean.Course;

import java.util.ArrayList;

import static com.mnnyang.gzuclassschedule.custom.util.Utils.dip2px;

/**
 * Created by mnnyang on 17-10-20.
 */

public class CourseView extends LinearLayout {

    private String[] WEEK = {"周一", "周二", "周三", "周四", "周五", "周六", "周七"};
    /**
     * 星期高度
     */
    private int mWeekHeight;

    /**
     * 星期宽度
     */
    private int mWeekWidth;
    /**
     * 月份宽度
     */
    private int mMonthWidth;

    /**
     * 分割线尺寸
     */
    private int mDividerSize;

    /**
     * view宽度
     */
    private int mViewWidth;

    /**
     * 月份大小
     */
    private int mMonthTextSize;

    /**
     * 星期大小
     */
    private int mWeekTextSize;

    /**
     * 水平分割线颜色
     */
    private int mHorizontalDividerColor;

    /**
     * 顶部背景颜色
     */
    private int mTopWeekBgColor;

    /**
     * 是否显示周末
     */
    private boolean mShowWeekend = true;

    private CourseTableView.OnItemClickListener mItemClickListener;
    private CourseTableView mCourseTableView = new CourseTableView(getContext());
    private int mMonthTextColor;
    private int mWeekTextColor;

    private int mMonth = 7;

    public CourseView(Context context) {
        super(context);
        setCourseData();
    }

    public CourseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setCourseData();
    }

    public CourseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCourseData();
    }

    private void setCourseData() {
        setOrientation(VERTICAL);
        initDefaultSize();
    }

    public CourseView setCourseData(ArrayList<Course> courses) {
        mCourseTableView.setCourseData(courses);
        return this;
    }

    /**
     * @param course success return null or return conflict object
     * @return
     */
    public Course addCourse(@NonNull Course course) {
        return mCourseTableView.addCourse(course);
    }

    private void initDefaultSize() {
        mWeekHeight = dip2px(getContext(), 45);
        mMonthWidth = dip2px(getContext(), 22);
        mDividerSize = dip2px(getContext(), 1);
        mMonthTextSize = 13;
        mWeekTextSize = 13;

        mHorizontalDividerColor = 0x15000000;
        mTopWeekBgColor = Color.TRANSPARENT;
        mWeekTextColor = 0xaa000000;
        mMonthTextColor = 0xd0000000;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mWeekWidth = (int) ((mViewWidth - mMonthWidth) / (mShowWeekend ? 7 : 5) + 0.5f);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    boolean isFirst = true;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (isFirst) {
            addTopWeekView();
            addCourseTableView();
            isFirst = false;
        }
        super.dispatchDraw(canvas);
    }

    public void updateView() {
        removeAllViews();
        addTopWeekView();

        ViewGroup viewGroup = (ViewGroup) mCourseTableView.getParent();
        viewGroup.removeAllViews();

        addCourseTableView();
        invalidate();
    }

    private void addCourseTableView() {
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.setVerticalScrollBarEnabled(false);
//        TODO 记录
        scrollView.setOverScrollMode(OVER_SCROLL_NEVER);

        mCourseTableView.setTopWeekHeight(mWeekHeight)
                .setOnItemClickListener(mItemClickListener)
                .setNodeWidth(mMonthWidth);

        mCourseTableView.setHorizontalFadingEdgeEnabled(false);

        scrollView.addView(mCourseTableView);
        addView(scrollView);
    }

    /*********************topWeek****************************/

    private void addTopWeekView() {
        LinearLayout weekView = new LinearLayout(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mViewWidth, mWeekHeight);
        weekView.setLayoutParams(params);
        weekView.setBackgroundColor(mTopWeekBgColor);
        weekView.setGravity(Gravity.CENTER);
        setTopWeekView(weekView);

        addView(weekView);
        View divider = new View(getContext());
        LayoutParams params1 = new LayoutParams(mViewWidth, mDividerSize);
        divider.setLayoutParams(params1);
        divider.setBackgroundColor(mHorizontalDividerColor);
        addView(divider);
    }

    private void setTopWeekView(LinearLayout view) {
        view.setOrientation(HORIZONTAL);
        TextView month = getTextView(mMonth + "\n月",
                mMonthTextSize, mMonthWidth + mDividerSize, mWeekHeight);
        month.setTextColor(mMonthTextColor);
        view.addView(month);

        for (int i = 0; i < (mShowWeekend ? 7 : 5); i++) {
            String text = WEEK[i];
            TextView week = getTextView(text,
                    mWeekTextSize, mWeekWidth + mDividerSize, mWeekHeight);
            week.setTextColor(mWeekTextColor);
            view.addView(week);
        }
    }

    private TextView getTextView(String text, int textSize, int w, int h) {
        TextView tv = new TextView(getContext());
        tv.setHeight(h);
        tv.setWidth(w);
        tv.setText(text);

        tv.setTextSize(textSize);
        tv.setGravity(Gravity.CENTER);

        return tv;
    }

    public CourseView setOnItemClickListener(CourseTableView.OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
        if (mCourseTableView != null) {
            mCourseTableView.setOnItemClickListener(itemClickListener);
        }
        return this;
    }

    /***********************get/set****************************/

    public int getWeekHeight() {
        return mWeekHeight;
    }

    public CourseView setWeekHeight(int weekHeight) {
        mWeekHeight = weekHeight;
        return this;
    }

    public int getWeekWidth() {
        return mWeekWidth;
    }

    public CourseView setWeekWidth(int weekWidth) {
        mWeekWidth = weekWidth;
        return this;
    }

    public int getMonthWidth() {
        return mMonthWidth;
    }

    public CourseView setMonthWidth(int monthWidth) {
        mMonthWidth = monthWidth;
        return this;
    }

    public int getDividerSize() {
        return mDividerSize;
    }

    public CourseView setDividerSize(int dividerSize) {
        mDividerSize = dividerSize;
        return this;
    }

    public int getMonthTextSize() {
        return mMonthTextSize;
    }

    public CourseView setMonthTextSize(int monthTextSize) {
        mMonthTextSize = monthTextSize;
        return this;
    }

    public int getWeekTextSize() {
        return mWeekTextSize;
    }

    public CourseView setWeekTextSize(int weekTextSize) {
        mWeekTextSize = weekTextSize;
        return this;
    }

    public int getHorizontalDividerColor() {
        return mHorizontalDividerColor;
    }

    public CourseView setHorizontalDividerColor(int horizontalDividerColor) {
        mHorizontalDividerColor = horizontalDividerColor;
        return this;
    }

    public int getTopWeekBgColor() {
        return mTopWeekBgColor;
    }

    public CourseView setTopWeekBgColor(int topWeekBgColor) {
        mTopWeekBgColor = topWeekBgColor;
        return this;
    }

    public boolean isShowWeekend() {
        return mShowWeekend;
    }

    public CourseView setShowWeekend(boolean showWeekend) {
        mShowWeekend = showWeekend;
        mCourseTableView.setShowWeekend(showWeekend);
        return this;
    }

    public CourseTableView getCourseTableView() {
        return mCourseTableView;
    }

    public int getMonthTextColor() {
        return mMonthTextColor;
    }

    public CourseView setMonthTextColor(int monthTextColor) {
        mMonthTextColor = monthTextColor;
        return this;
    }

    public int getWeekTextColor() {
        return mWeekTextColor;
    }

    public CourseView setWeekTextColor(int weekTextColor) {
        mWeekTextColor = weekTextColor;
        return this;
    }

    public CourseView setWeekText(String ...words) {
        if (words == null || words.length < 7) {
            return this;
        }

        System.arraycopy(words, 0, WEEK, 0, words.length);
        return this;
    }

    public int getMonth() {
        return mMonth;
    }

    public CourseView setMonth(int month) {
        mMonth = month;
        if (!isFirst) {
            updateView();
        }
        return this;
    }
}
