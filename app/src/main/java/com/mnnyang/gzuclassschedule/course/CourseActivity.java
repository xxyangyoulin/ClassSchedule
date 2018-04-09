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
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.add.AddActivity;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.custom.course.CourseTableView;
import com.mnnyang.gzuclassschedule.custom.course.CourseView;
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

import static com.mnnyang.gzuclassschedule.app.Constant.INTENT_UPDATE_TYPE_OTHER;

public class CourseActivity extends BaseActivity implements CourseContract.View,
        View.OnClickListener, CourseTableView.OnItemClickListener {

    CourseContract.Presenter mPresenter;
    private CourseView mCourseView;
    private LinearLayout mLayoutWeekTitle;
    private TextView mTvWeekCount;
    private int mCurrentWeekCount;
    private PopupWindow mPopupWindow;
    private int mCurrentMonth;
    private FloatingActionButton mFab;
    private UpdateReceiver mUpdateReceiver;
    private ShowDetailDialog mDialog;
    private int mCurrentCsNameId;
    private ImageView mIvBackground;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        initFirstStart();
        initToolbar();
        initBackground();
        initWeekTitle();
        initCourseView();
        initFab();
        registerReceiver();
        mPresenter = new CoursePresenter(this);

        updateView();
    }

    private void initBackground() {
        mIvBackground = findViewById(R.id.iv_background);

    }

    private void initWeekTitle() {
        mLayoutWeekTitle = (LinearLayout) findViewById(R.id.layout_week_title);
        mTvWeekCount = (TextView) findViewById(R.id.tv_toolbar_subtitle);
        TextView tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText(getString(R.string.app_name));
        mTvWeekCount.setOnClickListener(this);
    }


    private void initFab() {
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        mCourseView = (CourseView) findViewById(R.id.course_view);
        mCourseView.setWeekText(Constant.WEEK_SINGLE)
                .setMonthTextSize(10)
                .setDividerSize(0)
                .setOnItemClickListener(this);

        mCourseView.getCtView().setHorizontalDividerMargin(2);
    }

    @Override
    public void onClick(Course course, LinearLayout itemLayout) {
        mDialog = new ShowDetailDialog();
        mDialog.show(this, course, new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mDialog = null;
            }
        });
    }

    @Override
    public void onLongClick(final Course course, LinearLayout itemLayout) {
        DialogHelper dialogHelper = new DialogHelper();
        dialogHelper.showNormalDialog(this, getString(R.string.confirm_to_delete),
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
        mCourseView.setMonth(mCurrentMonth);

        //get id
        mCurrentCsNameId = Preferences.getInt(
                getString(R.string.app_preference_current_cs_name_id), 0);

        LogUtil.i(this,"当前课表-->"+mCurrentCsNameId);
        //set name
        //removed

        int maxNode = Preferences.getInt(getString(R.string.app_preference_max_node), Constant.DEFAULT_MAX_NODE_COUNT);

        mCourseView.getCtView()
                .setNodeCount(maxNode);

        mPresenter.updateCourseViewData(mCurrentCsNameId);
    }

    public void updateOtherPreference() {
        mPresenter.loadBackground();
        updateFabVisible();
    }

    private void updateFabVisible() {
        if (PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                .getBoolean(getString(R.string.app_preference_hide_fab), false)) {
            mFab.hide();
            return;
        }
        mFab.show();
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
        mCourseView.getCtView().setCurrentWeekCount(mCurrentWeekCount);
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
        boolean enabled = Preferences.getBoolean(getString(R.string.app_preference_bg_enabled),
                false);

        if (enabled) {
            mIvBackground.setImageBitmap(bitmap);
        } else {
            mIvBackground.setImageBitmap(null);
        }

    }

    @Override
    public void setCourseData(ArrayList<Course> courses) {
        if (mCourseView != null) {
            mCourseView.setCourseData(courses);
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
                    mCourseView.getCtView().setCurrentWeekCount(mCurrentWeekCount);
                    mCourseView.updateView();
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
