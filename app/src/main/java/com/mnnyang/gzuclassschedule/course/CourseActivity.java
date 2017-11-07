package com.mnnyang.gzuclassschedule.course;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.custom.CourseView;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.setting.SettingActivity;
import com.mnnyang.gzuclassschedule.utils.LogUtils;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CourseActivity extends BaseActivity implements CourseContract.View, View.OnClickListener {

    CourseContract.Presenter mPresenter;
    private CourseView mCourseView;
    private LinearLayout mLayoutWeekTitle;
    private TextView mTvWeekCount;
    private int mCurrentWeekCount;
    private PopupWindow mPopupWindow;
    private int mCurrentMonth;
    private String mCurrentScheduleName;
    private FloatingActionButton mFab;
    private UpdateReceiver mUpdateReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        initToolbar();
        initWeekTitle();
        initCourseView();
        initFab();

        mPresenter = new CoursePresenter(this);
        registerReceiver();
        updateView();
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
        mCourseView.setWeekText("一", "二", "三", "四", "五", "六", "日")
                .setMonthTextSize(10)
                .setDividerSize(0);

        mCourseView.getCtView()
                .setHorizontalDividerMargin(2);
    }


    private void registerReceiver() {
        mUpdateReceiver = new UpdateReceiver();
        IntentFilter intentFilter = new IntentFilter(Constant.INTENT_UPDATE);
        registerReceiver(mUpdateReceiver, intentFilter);
    }

    class UpdateReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra(Constant.INTENT_UPDATE_TYPE, 0);

            System.out.println(type);

            switch (type) {
                case Constant.INTENT_UPDATE_TYPE_COURSE:
                    updateCoursePreference();
                    break;
                case Constant.INTENT_UPDATE_TYPE_OTHER:
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

        mCurrentScheduleName = Preferences.getString(
                getString(R.string.app_preference_current_sd_name),
                getString(R.string.default_course_name));

        boolean showNoon = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext())
                .getBoolean(getString(R.string.app_preference_show_noon), false);
        int maxNode = Preferences.getInt(getString(R.string.app_preference_max_node),Constant.DEFAULT_MAX_NODE_COUNT);

        mCourseView.getCtView()
                .setShowNoon(showNoon)
                .setNodeCount(maxNode)
                .setNoonNode(Preferences.getInt(getString(R.string.app_preference_noon_node), Integer.parseInt(getString(R.string.default_noon_node))));

        LogUtils.d(this, "当前课表:" + mCurrentScheduleName);

        mPresenter.updateCourseViewData(mCurrentScheduleName);
    }

    public void updateOtherPreference() {
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

        String beginMillis = Preferences.getString(getString(
                R.string.app_preference_start_week_begin_millis), "");
        long currentMillis = Calendar.getInstance().getTimeInMillis();
        if (!TextUtils.isEmpty(beginMillis)) {
            long intBeginMillis = Long.valueOf(beginMillis);
            if (intBeginMillis > currentMillis) {
                PreferencesCurrentWeek(1);
                return;
            }
            int weekGap = TimeUtils.getWeekGap(intBeginMillis, currentMillis);
            System.out.println(intBeginMillis + "----" + currentMillis);
            mCurrentWeekCount += weekGap;
        } else {
            PreferencesCurrentWeek(1);
        }
        mTvWeekCount.setText("第" + mCurrentWeekCount + "周");
        mCourseView.getCtView().setCurrentWeekCount(mCurrentWeekCount);
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

        mPopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int xoff = mLayoutWeekTitle.getWidth() - mPopupWindow.getContentView().getMeasuredWidth();
        int yoff = -mTvWeekCount.getHeight();
        mPopupWindow.showAsDropDown(v, xoff / 2, yoff);

        if (mCurrentWeekCount <= 3) {
            return;
        }

        popupView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
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
        Calendar calendar = Calendar.getInstance();
        Date weekBegin = TimeUtils.getNowWeekBegin();
        calendar.setTime(weekBegin);
        if (currentWeekCount > 1) {
            calendar.add(Calendar.DATE, -7 * (currentWeekCount - 1));
        }
        Preferences.putString(getString(R.string.app_preference_start_week_begin_millis),
                calendar.getTimeInMillis() + "");
    }

    private void fab(View v) {
        Intent intent = new Intent(CourseActivity.this, SettingActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        System.exit(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUpdateReceiver != null) {
            unregisterReceiver(mUpdateReceiver);
        }
    }
}
