package com.mnnyang.gzuclassschedule.course;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.add.AddActivity;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.custom.course2.CourseAncestor;
import com.mnnyang.gzuclassschedule.custom.course2.CourseView;
import com.mnnyang.gzuclassschedule.custom.util.Utils;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;
import com.mnnyang.gzuclassschedule.setting.SettingActivity;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.DialogListener;
import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.TimeUtils;
import com.mnnyang.gzuclassschedule.utils.spec.ShowDetailDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.mnnyang.gzuclassschedule.app.Constant.INTENT_UPDATE_TYPE_OTHER;
import static com.mnnyang.gzuclassschedule.utils.ScreenUtils.dp2px;

public class CourseActivity extends BaseActivity implements CourseContract.View,
        View.OnClickListener {

    CourseContract.Presenter mPresenter;
    private LinearLayout mLayoutWeekTitle;
    private TextView mTvWeekCount;
    private int mCurrentWeekCount;
    private PopupWindow mPopupWindow;
    private int mCurrentMonth;
    private UpdateReceiver mUpdateReceiver;
    private ShowDetailDialog mDialog;
    private int mCurrentCsNameId;
    private com.mnnyang.gzuclassschedule.custom.course2.CourseView mCourseViewV2;
    private LinearLayout mLayoutWeekGroup;
    private LinearLayout mLayoutNodeGroup;
    private int WEEK_TEXT_SIZE = 13;
    private int NODE_TEXT_SIZE = 12;
    private int NODE_WIDTH = 28;
    private TextView mMMonthTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        mLayoutWeekGroup = findViewById(R.id.layout_week_group);
        mLayoutNodeGroup = findViewById(R.id.layout_node_group);

        initFirstStart();
        initToolbar();
        initWeekTitle();
        initCourseView();
        initWeekNodeGroup();
        registerReceiver();
        mPresenter = new CoursePresenter(this);

        updateView();
    }


    private void initWeekTitle() {
        mLayoutWeekTitle = findViewById(R.id.layout_week_title);
        mTvWeekCount = findViewById(R.id.tv_toolbar_subtitle);
        mTvWeekCount.setOnClickListener(this);
        TextView tvTitle = findViewById(R.id.tv_toolbar_title);
        tvTitle.setText(getString(R.string.app_name));
    }

    private void initWeekNodeGroup() {
        mLayoutNodeGroup.removeAllViews();
        mLayoutWeekGroup.removeAllViews();


        for (int i = -1; i < 7; i++) {
            TextView textView = new TextView(getApplicationContext());
            textView.setGravity(Gravity.CENTER);

            textView.setWidth(0);
            LinearLayout.LayoutParams params = null;

            if (i == -1) {
                params = new LinearLayout.LayoutParams(
                        Utils.dip2px(getApplicationContext(), NODE_WIDTH),
                        ViewGroup.LayoutParams.MATCH_PARENT);
                textView.setTextSize(NODE_TEXT_SIZE);
                textView.setText(mCurrentMonth + "\n月");


                mMMonthTextView = textView;
            } else {
                params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                params.weight = 1;
                textView.setTextSize(WEEK_TEXT_SIZE);
                textView.setText(Constant.WEEK_SINGLE[i]);
            }

            mLayoutWeekGroup.addView(textView, params);
        }

        for (int i = 1; i <= 16; i++) {
            TextView textView = new TextView(getApplicationContext());
            textView.setTextSize(NODE_TEXT_SIZE);
            textView.setGravity(Gravity.CENTER);
            textView.setText("" + i);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(getApplicationContext(), 60));


            mLayoutNodeGroup.addView(textView, params);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.toolbar_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_set) {
                    Intent intent = new Intent(CourseActivity.this,
                            SettingActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    private void initCourseView() {
        mCourseViewV2 = findViewById(R.id.course_view_v2);
        mCourseViewV2.setCourseItemRadius(3)
                .setTextTBMargin(dp2px(1), dp2px(1));

        mCourseViewV2.setOnItemClickListener(new CourseView.OnItemClickListener() {
            @Override
            public void onClick(List<CourseAncestor> course, View itemView) {
                mDialog = new ShowDetailDialog();
                mDialog.show(CourseActivity.this, (Course) course.get(0), new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mDialog = null;
                    }
                });
            }

            @Override
            public void onLongClick(List<CourseAncestor> courses, View itemView) {
                final Course course = (Course) courses.get(0);
                DialogHelper dialogHelper = new DialogHelper();
                dialogHelper.showNormalDialog(CourseActivity.this, getString(R.string.confirm_to_delete),
                        "课程 【" + course.getName() + "】" + Constant.WEEK[course.getWeek()]
                                + "第" + course.getNodes().get(0) + "节 " + "",
                        new DialogListener() {
                            @Override
                            public void onPositive(DialogInterface dialog, int which) {
                                super.onPositive(dialog, which);
                                //delete
                                mPresenter.deleteCourse(course.getCourseId());
                            }
                        });
            }

            public void onAdd(CourseAncestor course, View addView) {
                Intent intent = new Intent(CourseActivity.this, AddActivity.class);
                intent.putExtra(Constant.INTENT_COURSE_ANCESTOR, course);
                intent.putExtra(Constant.INTENT_ADD, true);
                startActivity(intent);
            }

        });
    }

    private void registerReceiver() {
        mUpdateReceiver = new UpdateReceiver();
        IntentFilter intentFilter = new IntentFilter(Constant.INTENT_UPDATE);
        registerReceiver(mUpdateReceiver, intentFilter);
    }

    class UpdateReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra(Constant.INTENT_UPDATE_TYPE, 0);

            LogUtil.i(this, "type" + type);

            switch (type) {
                case Constant.INTENT_UPDATE_TYPE_COURSE:
                    updateCoursePreference();
                    break;
                case INTENT_UPDATE_TYPE_OTHER:
                    updateOtherPreference();
                    break;
            }
        }
    }


    private void updateView() {
        updateCoursePreference();
        updateOtherPreference();
    }

    public void updateCoursePreference() {
        updateCurrentWeek();
        mCurrentMonth = TimeUtils.getNowMonth();
        mMMonthTextView.setText(mCurrentMonth + "\n月");

        //get id
        mCurrentCsNameId = Preferences.getInt(
                getString(R.string.app_preference_current_cs_name_id), 0);

        LogUtil.i(this, "当前课表-->" + mCurrentCsNameId);
        //int maxNode = Preferences.getInt(getString(R.string.app_preference_max_node), Constant.DEFAULT_MAX_NODE_COUNT);

        mPresenter.updateCourseViewData(mCurrentCsNameId);
    }

    public void updateOtherPreference() {
        mPresenter.loadBackground();
    }

    private void updateCurrentWeek() {
        mCurrentWeekCount = 1;

        //获取开始时间
        String beginMillis = Preferences.getString(getString(
                R.string.app_preference_start_week_begin_millis), "");

        //获取当前时间
        long currentMillis = Calendar.getInstance().getTimeInMillis();

        //存在开始时间
        if (!TextUtils.isEmpty(beginMillis)) {
            long intBeginMillis = Long.valueOf(beginMillis);

            //获取到的配置是时间大于当前时间 重置为第一周
            if (intBeginMillis > currentMillis) {
                LogUtil.e(this, "intBeginMillis > currentMillis");
                PreferencesCurrentWeek(1);

            } else {
                //计算出开始时间到现在时间的周数
                int weekGap = TimeUtils.getWeekGap(intBeginMillis, currentMillis);

//                LogUtil.e(this, "差距为" + weekGap);
                System.out.println(intBeginMillis + "----" + currentMillis);
                mCurrentWeekCount += weekGap;
            }

        } else {
            //不存在开始时间 初始化为第一周
            PreferencesCurrentWeek(1);
        }
        mTvWeekCount.setText("第" + mCurrentWeekCount + "周");
        mCourseViewV2.setCurrentIndex(mCurrentWeekCount);
    }

    @Override
    public void initFirstStart() {
        boolean isFirst = Preferences.getBoolean(getString(R.string.app_preference_app_is_first_start), true);
        if (!isFirst) {
            return;
        }

        int csNameId = CourseDbDao.newInstance().getCsNameId(getString(R.string.default_course_name));

        Preferences.putInt(getString(R.string.app_preference_current_cs_name_id), csNameId);

        Preferences.putBoolean(getString(R.string.app_preference_app_is_first_start), false);
    }

    @Override
    public void setBackground(Bitmap bitmap) {
    }

    @Override
    public void setCourseData(ArrayList<Course> courses) {
        mCourseViewV2.clear();

        int i = 0;
        for (Course course : courses) {
            course.init(course.getWeek(), course.getNodes().get(0), course.getNodes().size(), Utils.getRandomColor(i++));
            course.setStartIndex(course.getStartWeek());
            course.setEndIndex(course.getEndWeek());
            course.setText(course.getName()+"\n@"+course.getClassRoom());
            course.setShowIndex(course.getWeekType());

            LogUtil.w(this, course.toString());
            mCourseViewV2.addCourse(course);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                fab(v);
                break;
            case R.id.tv_toolbar_subtitle:
                weekTitle(v);
                break;
        }
    }

    private void weekTitle(View v) {
        popupWindow(v);

    }

    private void popupWindow(View v) {
        mPopupWindow = new PopupWindow(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final HorizontalScrollView popupView = getPopupWindowView();
        mPopupWindow.setContentView(popupView);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.animDown);
        //是否允许popup超出屏幕范围
        mPopupWindow.setClippingEnabled(true);

        mPopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);

        int xoff = mLayoutWeekTitle.getWidth() - mPopupWindow.getContentView().getMeasuredWidth();
        int yoff = -mTvWeekCount.getHeight();
        mPopupWindow.showAsDropDown(v, xoff / 2, yoff);

        if (mCurrentWeekCount <= 3) {
            return;
        }

        popupView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        popupView.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                        ViewGroup llView = (ViewGroup) popupView.getChildAt(0);
                        int width = llView.getChildAt(0).getWidth();
                        int x = width * (mCurrentWeekCount - 3);
                        popupView.scrollTo(x, 0);
                    }
                });
    }

    @NonNull
    private HorizontalScrollView getPopupWindowView() {
        final HorizontalScrollView popupView = new HorizontalScrollView(getBaseContext());
        LinearLayout linearLayout = new LinearLayout(getBaseContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(Color.BLACK);

        for (int i = 1; i <= 25; i++) {
            TextView tv = (TextView) LayoutInflater.from(getBaseContext())
                    .inflate(R.layout.layout_week_text_view, null);
            linearLayout.addView(tv);
            tv.setText("第" + i + "周");
            tv.setTag(i);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(v.getTag());
                    mCurrentWeekCount = (int) v.getTag();
                    mPopupWindow.dismiss();
                    PreferencesCurrentWeek((Integer) v.getTag());
                    mCourseViewV2.setCurrentIndex(mCurrentWeekCount);
                    mCourseViewV2.resetView();
                    mTvWeekCount.setText("第" + mCurrentWeekCount + "周");
                }
            });
        }
        popupView.addView(linearLayout);
        return popupView;
    }

    private void PreferencesCurrentWeek(int currentWeekCount) {
        //得到一个当前周 周一的日期
        Calendar calendar = Calendar.getInstance();
        Date weekBegin = TimeUtils.getNowWeekBegin();
        calendar.setTime(weekBegin);

        if (currentWeekCount > 1) {
            calendar.add(Calendar.DATE, -7 * (currentWeekCount - 1));
        }

        LogUtil.e(this, "preferences date" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        Preferences.putString(getString(R.string.app_preference_start_week_begin_millis),
                calendar.getTimeInMillis() + "");
    }

    private void fab(View v) {
        Intent intent = new Intent(CourseActivity.this, AddActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mDialog != null) {
                    mDialog.dismiss();
                    return true;
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDialog != null) {
            mDialog.dismiss();
            System.out.println("去关闭");
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUpdateReceiver != null) {
            unregisterReceiver(mUpdateReceiver);
        }
    }
}
