package com.mnnyang.gzuclassschedule.custom;

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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.custom.util.Utils;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;

import static android.widget.LinearLayout.VERTICAL;
import static com.mnnyang.gzuclassschedule.custom.util.Utils.dip2px;
import static com.mnnyang.gzuclassschedule.custom.util.Utils.getColors;

/**
 * Created by mnnyang on 17-10-20.
 */

public class CourseTableView extends RelativeLayout {
    /**
     * 实线
     */
    public static final int DIVIDER_TYPE_SOLID = 1;
    /**
     * 虚线
     */
    public static final int DIVIDER_TYPE_DASHED = 2;

    /**
     * view高度
     */
    private int mViewHeight;
    /**
     * view宽度
     */
    private int mViewWidth;
    /**
     * 星期高度
     */
    private int mTopWeekHeight;

    /**
     * 课程模块高度
     */
    private int mItemHeight;

    /**
     * 课程模块宽度
     */
    private int mItemWidth;
    /**
     * 节模块宽度
     */
    private int mNodeWidth;

    /**
     * 分割线尺寸
     */
    private int mDividerSize;
    /**
     * 垂直分割线margin值
     */
    private int mVerticalDividerMargin;
    /**
     * 水平分割线margin值
     */
    private int mHorizontalDividerMargin;

    /**
     * 模块水平padding
     */
    private int mItemPaddingH;
    /**
     * 模块垂直padding
     */
    private int mItemPaddingV;
    /**
     * 课程模块字体大小
     */
    private int mCourseTextSize;

    /**
     * node字体大小
     */
    private int mNodeTextSize;

    /**
     * node背景颜色
     */
    private int mNodeBgColor;

    /**
     * 水平分割线颜色
     */
    private int mHorizontalDividerColor;

    /**
     * 垂直分割线颜色
     */
    private int mVerticalDividerColor;

    /**
     * 水平分割线风格 1实线 2虚线
     */
    private int mHorizontalDividerType;
    /**
     * 垂直分割线风格 1实线 2虚线
     */
    private int mVerticalDividerType;

    /**
     * 课程颜色
     */
    private int mCourseTextColor;
    /**
     * node字体颜色
     */
    private int mNodeTextColor;

    /**
     * 一天课程最大值
     */
    private int mNodeCount;

    /**
     * 限制课程文本最大行
     */
    private int mCourseTextMaxLineCount;
    /**
     * 是否显示周末
     */
    private boolean mShowWeekend = true;

    /**
     * 显示正午
     */
    private boolean mShowNoon = true;
    /**
     * 显示水平分割线
     */
    private boolean mShowHorizontalDivider = true;
    /**
     * 显示垂直分割线
     */
    private boolean mShowVerticalDivider = false;
    /**
     * 显示节数分割线
     */
    private boolean mShowNodeDivider = false;
    /**
     * 省略显示文本
     */
    private boolean mOmitCourseName = false;

    /**
     * 当前周数
     */
    private int mCurrentWeekCount;

    /**
     * 中午节数
     */
    private int mNoonNode;

    private Paint mDividerPaint;

    private ArrayList<Course> mCourses = new ArrayList<>();

    private boolean mHideNoCurrentWeek;

    public static int colors[];
    private int mCourseItemRadius;

    public CourseTableView(Context context) {
        super(context);
        init();
    }

    public CourseTableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CourseTableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initDefaultSize();
        initPaint();
    }

    private void initDefaultSize() {
        mTopWeekHeight = dip2px(getContext(), 45);
        mItemHeight = dip2px(getContext(), 50);
        mNodeWidth = dip2px(getContext(), 28);
        mDividerSize = dip2px(getContext(), 1);
        mVerticalDividerMargin = dip2px(getContext(), 1);
        mItemPaddingV = mItemPaddingH = dip2px(getContext(), 2);

        mCourseTextMaxLineCount = 6;
        mNoonNode = 4;
        mNodeCount = 11;
        mCurrentWeekCount = 1;
        mCourseItemRadius = 3;

        mCourseTextSize = 12;
        mNodeTextSize = 12;

        mVerticalDividerColor = 0x40000000;
        mNodeTextColor = 0xFF909090;
        mCourseTextColor = 0xffffffff;
        mHorizontalDividerColor = 0x20000000;
        mNodeBgColor = Color.TRANSPARENT;

        mVerticalDividerType = DIVIDER_TYPE_SOLID;
        mHorizontalDividerType = DIVIDER_TYPE_DASHED;

        colors = getColors(getContext());
    }

    private void initPaint() {
        mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDividerPaint.setColor(mHorizontalDividerColor);
        mDividerPaint.setStyle(Paint.Style.STROKE);
        mDividerPaint.setStrokeWidth(mDividerSize);
    }

    public CourseTableView setCourseData(ArrayList<Course> courseData) {
        for (Course course : courseData) {
            addCourse(course);
        }
        return this;
    }

    /**
     * 添加课程添加成功返回null 添加失败返回冲突课程<br>
     * 应检查课程信息的准确性后再调用该方法 <br>
     *
     * @return success return null or return conflict object
     */
    public Course addCourse(@NonNull Course course) {
        for (Course c : mCourses) {
            if (c.equals(course)) {
                LogUtils.e(this, c.getName() + " and " + course.getName() + " conflict");
                return c;
            }
            setShowAble(c, course);
        }

        if (course.getNodes().size() > 0
                && course.getWeek() > 0) {

            setCourseItemViewColor(course);
            mCourses.add(course);

            if (!mIsFirst) {
                addCourseItemView(course);
                updateView();
            }

        } else {
            LogUtils.e(this, "Time is not complete!--->" + course.toString());
        }

        return null;
    }

    HashMap<String, Integer> nameColorMap = new HashMap<>();

    private void setCourseItemViewColor(Course course) {
        Integer integer = nameColorMap.get(course.getName());
        if (integer == null) {
            int size = nameColorMap.size();
            if (size >= colors.length - 2) {
                size = size % (colors.length - 2);
            }

            nameColorMap.put(course.getName(), colors[size]);
            nameColorMap.put(course.getName() + "_p", colors[size + 1]);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogUtils.d(this, "onMeasure");
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        initSize();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initSize() {
        mViewHeight = mNodeCount * mItemHeight + (mNodeCount + 1) * mDividerSize;
        if (mViewHeight < mTopWeekHeight) {
            throw new IllegalArgumentException("CourseTableView too small !");
        }

        mItemWidth = (int) ((mViewWidth - mNodeWidth) /
                (mShowWeekend ? 7 : 5) + 0.5f);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = mViewHeight;
        setLayoutParams(params);
    }

    /**********************addView*******************************/

    private void addCourseItemView() {
        for (Course course : mCourses) {
            addCourseItemView(course);
        }
    }

    private void addNodeView() {
        int margin = 0;

        //如果显示正午 +1
        int nodeMax = mNodeCount + (mShowNoon ? 1 : 0);
        for (int i = 0; i < nodeMax; i++) {
            String text = "";

            if (true == mShowNoon) {
                if (i < mNoonNode) {
                    text = String.valueOf(i + 1);
                } else if (i == mNoonNode) {
                    text = "中\n午";
                } else {
                    text = String.valueOf(i);
                }
            } else {
                text = String.valueOf(i + 1);
            }

            TextView tv = getNodeTextView(text);
            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = margin;
            tv.setLayoutParams(params);
            tv.setTextColor(mNodeTextColor);
            addView(tv);

            margin += (mItemHeight + mDividerSize);
        }
    }


    public void addCourseItemView(final Course course) {
        if (course == null) {
            throw new IllegalArgumentException("course is null !");
        }

        if (course.getWeek() <= 0 || course.getWeek() > 7) {
            LogUtils.e(this, course.getName() + ":week err");
            return;
        }

        if (course.getNodes().size() == 0 && course.getWeek() <= 0) {
            LogUtils.e(this, course.getName() + ":nodes err");
            return;
        }
        //itemView
        final LinearLayout itemView = getCourseItemView(course);
        itemView.setTag(course);
        //单双周背景处理与隐藏和显示
        setBgAndVisiable(course, itemView);
        //startNode
        int startNode = course.getNodes().get(0);
        /*for (Integer integer : course.getNodes()) {
            System.out.println(integer);
        }*/
        if (mShowNoon) {
            if (startNode > mNoonNode) {
                startNode++;
            }
        }
        startNode--;
        if (startNode == -2) {
            startNode = mNoonNode;
        }

        final int itemHeight = (mDividerSize + mItemHeight) * (course.getNodes().size())
                - mVerticalDividerMargin * 2;
        int itemWidth = mItemWidth - 2 * mHorizontalDividerMargin+mDividerSize;
        int topMargin = startNode * (mItemHeight + mDividerSize)
                - mDividerSize + mVerticalDividerMargin;
        int leftMargin = mNodeWidth + mDividerSize / 2 + (course.getWeek() - 1)
                * (mItemWidth + mDividerSize) + mHorizontalDividerMargin;

        LayoutParams params = new LayoutParams(
                itemWidth, itemHeight);
        params.leftMargin = leftMargin;
        params.topMargin = topMargin;
        itemView.setLayoutParams(params);
        //
        limitLineCount(itemView);
        addView(itemView);
    }

    private void setBgAndVisiable(Course course, LinearLayout itemView) {
        StateListDrawable drawable;
        if (course.getWeekType() == Course.ALL_WEEK) {
            drawable = Utils.getPressedSelector(getContext(),
                    nameColorMap.get(course.getName()),
                    nameColorMap.get(course.getName() + "_p"), mCourseItemRadius);
            itemView.setBackground(drawable);
        } else {
            int remainder = mCurrentWeekCount % 2;
            if (remainder == 1 && course.getWeekType() == Course.SINGLE_WEEK
                    || remainder == 0 && course.getWeekType() == Course.DOUBLE_WEEK) {
                //当前周的
                drawable = Utils.getPressedSelector(getContext(),
                        nameColorMap.get(course.getName()),
                        nameColorMap.get(course.getName() + "_p"), mCourseItemRadius);
                itemView.setBackground(drawable);
            } else {
                drawable = Utils.getPressedSelector(getContext(),
                        colors[colors.length - 2], colors[colors.length - 1], mCourseItemRadius);
                itemView.setBackground(drawable);

                TextView tv = (TextView) itemView.getChildAt(0);
                tv.setTextColor(Utils.getColor(getResources(), R.color.color_text_not_current));
                tv.setText("[非本周]" + tv.getText());
                if (((Course) itemView.getTag()).isShowOverlap()) {
                    itemView.setVisibility(VISIBLE);
                } else {
                    itemView.setVisibility(GONE);
                }
            }
        }
    }

    /**
     * 限制行数
     */
    private void limitLineCount(final LinearLayout itemView) {
        itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                itemView.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);

                TextView tv = (TextView) itemView.getChildAt(0);
                omitCourseName(tv);
            }
        });
    }

    /**
     * 省略课程名称
     *
     * @param tv
     */
    private void omitCourseName(TextView tv) {
        if (!mOmitCourseName) {
            return;
        }
        try {
            if (tv.getLineCount() > mCourseTextMaxLineCount) {
                String text = tv.getText().toString();
                text = text.replace("..", "");
                String[] split = text.split("\\n@");
                String s = split[0].substring(0, split[0].length() - 2) + "..\n@" + split[1];
                tv.setText(s);
                omitCourseName(tv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    boolean mIsFirst = true;

    /**********************draw***************************/
    @Override
    protected void dispatchDraw(Canvas canvas) {
        LogUtils.v(this, "disDraw");
        normalDraw(canvas);

        if (mIsFirst) {
            mIsFirst = false;
            addNodeView();
            addCourseItemView();
        }

        super.dispatchDraw(canvas);
    }


    public void updateView() {
        removeAllViews();
        addNodeView();

        resetShowAble();

        addCourseItemView();
        invalidate();
    }

    //重置重叠数据的显示
    private void resetShowAble() {
        for (int i = 0; i < mCourses.size(); i++) {
            for (int k = i + 1; k < mCourses.size(); k++) {
                setShowAble(mCourses.get(i), mCourses.get(k));
            }
        }
    }

    /**
     * 处理重叠显示问题
     */
    private void setShowAble(@NonNull Course course, Course c) {
        if (c.compareTo(course) == 0) {
            LogUtils.d(this, c.getName() + "-" + course.getName() + " overlap");
            int remainder = mCurrentWeekCount % 2;
            if (remainder == 0) {
                c.setShowOverlap(c.getWeekType() == Course.DOUBLE_WEEK);
                course.setShowOverlap(course.getWeekType() == Course.DOUBLE_WEEK);
            } else {
                c.setShowOverlap(c.getWeekType() == Course.SINGLE_WEEK);
                course.setShowOverlap(course.getWeekType() == Course.SINGLE_WEEK);
            }
        }
    }

    private void normalDraw(Canvas canvas) {
        drawHorizontalDivider(canvas);
        drawVerticalDivider(canvas);
        drawNodeBgColor(canvas);
    }

    private void drawNodeBgColor(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(mNodeBgColor);

        canvas.drawRect(0, 0, mNodeWidth,
                mViewHeight + (mShowNoon ? mViewHeight : 0), mPaint);
    }

    Path mDividerPath = new Path();

    private void drawHorizontalDivider(Canvas canvas) {
        if (!mShowHorizontalDivider) {
            return;
        }

        int y = mItemHeight;
        int endMax = mNodeCount - 1 + (mShowNoon ? 1 : 0);

        int startX = mShowNodeDivider ? 0 : mNodeWidth;

        mDividerPaint.setColor(mHorizontalDividerColor);
        if (mHorizontalDividerType == DIVIDER_TYPE_DASHED) {
            mDividerPaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        } else {
            mDividerPaint.setPathEffect(null);
        }

        for (int i = 0; i < endMax; i++) {
            mDividerPath.reset();
            mDividerPath.moveTo(startX, y);
            mDividerPath.lineTo(mViewWidth, y);
            canvas.drawPath(mDividerPath, mDividerPaint);
            y += (mItemHeight + mDividerSize);
        }
    }

    private void drawVerticalDivider(Canvas canvas) {
        if (!mShowVerticalDivider) {
            return;
        }

        int sY = 0;
        int x = mNodeWidth;
        int lineLength = mViewHeight + (mShowNoon ? (mItemHeight + mDividerSize) : 0);
        int maxWeek = mShowWeekend ? 7 : 5;

        mDividerPaint.setColor(mVerticalDividerColor);
        if (mVerticalDividerType == DIVIDER_TYPE_DASHED) {
            mDividerPaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        } else {
            mDividerPaint.setPathEffect(null);
        }

        for (int i = 0; i < maxWeek; i++) {
            mDividerPath.reset();
            mDividerPath.moveTo(x, sY);
            mDividerPath.lineTo(x, lineLength);

            canvas.drawPath(mDividerPath, mDividerPaint);
            x += (mItemWidth + mDividerSize);
        }
    }

    /************************build view*****************************/
    public LinearLayout getCourseItemView(@NonNull final Course course) {
        //showText
        String name = course.getName();
        String showText = name + "\n@" + course.getClassRoom();

        //bgLayout
        final LinearLayout bgLayout = new LinearLayout(getContext());
        bgLayout.setOrientation(VERTICAL);

        //TextView
        TextView tv = getCourseTextView(mItemHeight, mItemWidth);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(params);
        tv.setText(showText);
        tv.setLineSpacing(-2, 1);
        tv.setTextSize(mCourseTextSize);
        tv.setTextColor(mCourseTextColor);

        bgLayout.addView(tv);

        //event
        bgLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onClick(course, bgLayout);
                }
            }
        });

        return bgLayout;
    }

    @NonNull
    private TextView getCourseTextView(int h, int w) {
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
        tv.setLayoutParams(params);
        tv.setPadding(mItemPaddingH, mItemPaddingV, mItemPaddingH, mItemPaddingV);
        //bold
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
        return tv;
    }

    private TextView getNodeTextView(String text) {
        TextView tv = new TextView(getContext());
        tv.setHeight(mItemHeight);
        tv.setWidth(mNodeWidth);
        tv.setTextSize(mNodeTextSize);
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);

        return tv;
    }


    /**********************event**************************/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtils.v(this, "触摸");


        return super.onTouchEvent(event);
    }

    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onClick(Course course, LinearLayout itemLayout);

    }

    public CourseTableView setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
        return this;
    }

    /**********************Other**************************/

    public int getTopWeekHeight() {
        return mTopWeekHeight;
    }

    public CourseTableView setTopWeekHeight(int topWeekHeight) {
        mTopWeekHeight = topWeekHeight;
        return this;
    }

    public int getItemHeight() {
        return mItemHeight;
    }

    public CourseTableView setItemHeight(int itemHeight) {
        mItemHeight = itemHeight;
        return this;
    }

    public int getNodeWidth() {
        return mNodeWidth;
    }

    public CourseTableView setNodeWidth(int nodeWidth) {
        mNodeWidth = nodeWidth;
        return this;
    }

    public int getDividerSize() {
        return mDividerSize;
    }

    public CourseTableView setDividerSize(int dividerSize) {
        mDividerSize = dividerSize;
        return this;
    }

    public int getVerticalDividerMargin() {
        return mVerticalDividerMargin;
    }

    public CourseTableView setVerticalDividerMargin(int verticalDividerMargin) {
        mVerticalDividerMargin = verticalDividerMargin;
        return this;
    }

    public int getItemPaddingH() {
        return mItemPaddingH;
    }

    public CourseTableView setItemPaddingH(int itemPaddingH) {
        mItemPaddingH = itemPaddingH;
        return this;
    }

    public int getItemPaddingV() {
        return mItemPaddingV;
    }

    public CourseTableView setItemPaddingV(int itemPaddingV) {
        mItemPaddingV = itemPaddingV;
        return this;
    }

    public int getCourseTextSize() {
        return mCourseTextSize;
    }

    public CourseTableView setCourseTextSize(int courseTextSize) {
        this.mCourseTextSize = courseTextSize;
        return this;
    }

    public int getHorizontalDividerColor() {
        return mHorizontalDividerColor;
    }

    public CourseTableView setHorizontalDividerColor(int horizontalDividerColor) {
        mHorizontalDividerColor = horizontalDividerColor;
        mDividerPaint.setColor(horizontalDividerColor);
        return this;
    }

    public int getNodeCount() {
        return mNodeCount;
    }

    public CourseTableView setNodeCount(int nodeCount) {
        mNodeCount = nodeCount;
        return this;
    }

    public boolean isShowWeekend() {
        return mShowWeekend;
    }

    public CourseTableView setShowWeekend(boolean showWeekend) {
        mShowWeekend = showWeekend;
        return this;
    }


    public int getNodeTextSize() {
        return mNodeTextSize;
    }

    public CourseTableView setNodeTextSize(int nodeTextSize) {
        mNodeTextSize = nodeTextSize;
        return this;
    }

    public boolean isShowNoon() {
        return mShowNoon;
    }

    public CourseTableView setShowNoon(boolean showNoon) {
        mShowNoon = showNoon;
        return this;
    }

    public int getNoonNode() {
        return mNoonNode;
    }

    public CourseTableView setNoonNode(int noonNode) {
        mNoonNode = noonNode;
        return this;
    }

    public int getHorizontalDividerMargin() {
        return mHorizontalDividerMargin;
    }

    public CourseTableView setHorizontalDividerMargin(int horizontalDividerMargin) {
        mHorizontalDividerMargin = horizontalDividerMargin;
        return this;
    }

    public int getCourseTextColor() {
        return mCourseTextColor;
    }

    public CourseTableView setCourseTextColor(int courseTextColor) {
        mCourseTextColor = courseTextColor;
        return this;
    }

    public int getVerticalDividerColor() {
        return mVerticalDividerColor;
    }

    public CourseTableView setVerticalDividerColor(int verticalDividerColor) {
        mVerticalDividerColor = verticalDividerColor;
        return this;
    }

    public boolean isShowHorizontalDivider() {
        return mShowHorizontalDivider;
    }

    public CourseTableView setShowHorizontalDivider(boolean showHorizontalDivider) {
        mShowHorizontalDivider = showHorizontalDivider;
        return this;
    }

    public boolean isShowVerticalDivider() {
        return mShowVerticalDivider;
    }

    public CourseTableView setShowVerticalDivider(boolean showVerticalDivider) {
        mShowVerticalDivider = showVerticalDivider;
        return this;
    }

    public int getHorizontalDividerType() {
        return mHorizontalDividerType;
    }

    public CourseTableView setHorizontalDividerType(int horizontalDividerType) {
        mHorizontalDividerType = horizontalDividerType;
        return this;
    }

    public int getCourseTextMaxLineCount() {
        return mCourseTextMaxLineCount;
    }

    public CourseTableView setCourseTextMaxLineCount(int courseTextMaxLineCount) {
        mCourseTextMaxLineCount = courseTextMaxLineCount;
        return this;
    }

    public boolean isShowNodeDivider() {
        return mShowNodeDivider;
    }

    public CourseTableView setShowNodeDivider(boolean showNodeDivider) {
        mShowNodeDivider = showNodeDivider;
        return this;
    }

    public int getNodeBgColor() {
        return mNodeBgColor;
    }

    public CourseTableView setNodeBgColor(int nodeBgColor) {
        mNodeBgColor = nodeBgColor;
        return this;
    }

    public int getVerticalDividerType() {
        return mVerticalDividerType;
    }

    public CourseTableView setVerticalDividerType(int verticalDividerType) {
        mVerticalDividerType = verticalDividerType;
        return this;
    }

    public boolean isOmitCourseName() {
        return mOmitCourseName;
    }

    public CourseTableView setOmitCourseName(boolean omitCourseName) {
        mOmitCourseName = omitCourseName;
        return this;
    }

    public int getCurrentWeekCount() {
        return mCurrentWeekCount;
    }

    public CourseTableView setCurrentWeekCount(int currentWeekCount) {
        this.mCurrentWeekCount = currentWeekCount;
        return this;
    }

    public int getCourseItemRadius() {
        return mCourseItemRadius;
    }

    public CourseTableView setCourseItemRadius(int courseItemRadius) {
        mCourseItemRadius = courseItemRadius;
        return this;
    }
}
