package com.mnnyang.gzuclassschedule.mvp.course;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.RecyclerBaseAdapter;
import com.mnnyang.gzuclassschedule.app.AppUtils;
import com.mnnyang.gzuclassschedule.app.Cache;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.custom.course.CourseAncestor;
import com.mnnyang.gzuclassschedule.custom.course.CourseView;
import com.mnnyang.gzuclassschedule.custom.util.Utils;
import com.mnnyang.gzuclassschedule.data.beanv2.CourseGroup;
import com.mnnyang.gzuclassschedule.data.beanv2.CourseV2;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;
import com.mnnyang.gzuclassschedule.data.greendao.CourseGroupDao;
import com.mnnyang.gzuclassschedule.data.greendao.CourseV2Dao;
import com.mnnyang.gzuclassschedule.mvp.add.AddActivity;
import com.mnnyang.gzuclassschedule.mvp.home.HomeActivity;
import com.mnnyang.gzuclassschedule.mvp.mg.MgActivity;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.DialogListener;
import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.ScreenUtils;
import com.mnnyang.gzuclassschedule.utils.TimeUtils;
import com.mnnyang.gzuclassschedule.utils.event.CourseDataChangeEvent;
import com.mnnyang.gzuclassschedule.utils.spec.ShowDetailDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.mnnyang.gzuclassschedule.utils.ScreenUtils.dp2px;

/**
 * TODO 添加删除撤销
 */
public class CourseActivity extends BaseActivity implements CourseContract.View,
        View.OnClickListener {

    CourseContract.Presenter mPresenter;
    private TextView mTvWeekCount;
    private int mCurrentWeekCount;
    private int mCurrentMonth;
    private ShowDetailDialog mDialog;
    private CourseView mCourseViewV2;
    private LinearLayout mLayoutWeekGroup;
    private LinearLayout mLayoutNodeGroup;
    private int WEEK_TEXT_SIZE = 13;
    private int NODE_TEXT_SIZE = 12;
    private int NODE_WIDTH = 28;
    private TextView mMMonthTextView;
    private RecyclerView mRvSelectWeek;
    private int mHeightSelectWeek;
    private boolean mSelectWeekIsShow = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        //EvenBus
        EventBus.getDefault().register(this);

        mLayoutWeekGroup = findViewById(R.id.layout_week_group);
        mLayoutNodeGroup = findViewById(R.id.layout_node_group);

        initFirstStart();
        initToolbar();
        initWeek();
        initCourseView();
        initWeekNodeGroup();
        mPresenter = new CoursePresenter(this);

        updateView();
    }

    private void initWeek() {
        initWeekTitle();
        initSelectWeek();
    }

    private void initSelectWeek() {
        mRvSelectWeek = findViewById(R.id.recycler_view_select_week);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRvSelectWeek.getLayoutParams();
        params.topMargin = -ScreenUtils.dp2px(45);
        mRvSelectWeek.setLayoutParams(params);

        mRvSelectWeek.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                RecyclerView.HORIZONTAL, false));
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            strings.add("第" + i + "周");
        }
        SelectWeekAdapter selectWeekAdapter = new SelectWeekAdapter(R.layout.adapter_select_week, strings);
        mRvSelectWeek.setAdapter(selectWeekAdapter);

        mRvSelectWeek.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mHeightSelectWeek = bottom - top;
            }
        });


        selectWeekAdapter.setItemClickListener(new RecyclerBaseAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, final RecyclerBaseAdapter.ViewHolder holder) {
                mCurrentWeekCount = holder.getAdapterPosition() + 1;
                AppUtils.PreferencesCurrentWeek(getBaseContext(), mCurrentWeekCount);
                mCourseViewV2.setCurrentIndex(mCurrentWeekCount);
                mCourseViewV2.resetView();
                mTvWeekCount.setText("第" + mCurrentWeekCount + "周");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animSelectWeek(false);
                        AppUtils.updateWidget(getApplicationContext());
                    }
                }, 150);
            }

            @Override
            public void onItemLongClick(View view, RecyclerBaseAdapter.ViewHolder holder) {

            }
        });
    }


    private void animSelectWeek(boolean show) {
        mSelectWeekIsShow = show;

        int start = 0, end = 0;
        if (show) {
            start = -mHeightSelectWeek;
        } else {
            end = -mHeightSelectWeek;
        }

        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                System.out.println(animation.getAnimatedValue());
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRvSelectWeek.getLayoutParams();
                params.topMargin = (int) animation.getAnimatedValue();
                mRvSelectWeek.setLayoutParams(params);
            }
        });
        animator.start();
    }


    private void initWeekTitle() {
        mTvWeekCount = findViewById(R.id.tv_toolbar_subtitle);
        mTvWeekCount.setOnClickListener(this);
        TextView tvTitle = findViewById(R.id.tv_toolbar_title);
        tvTitle.setText(getString(R.string.class_schedule));
    }

    private void initWeekNodeGroup() {
        mLayoutNodeGroup.removeAllViews();
        mLayoutWeekGroup.removeAllViews();

        for (int i = -1; i < 7; i++) {
            TextView textView = new TextView(getApplicationContext());
            textView.setGravity(Gravity.CENTER);

            textView.setWidth(0);
            textView.setTextColor(0xd0212121);
            LinearLayout.LayoutParams params;

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

        int nodeItemHeight = Utils.dip2px(getApplicationContext(), 55);
        for (int i = 1; i <= 16; i++) {
            TextView textView = new TextView(getApplicationContext());
            textView.setTextSize(NODE_TEXT_SIZE);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.GRAY);
            textView.setText(String.valueOf(i));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, nodeItemHeight);
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
                            HomeActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCourseView() {
        mCourseViewV2 = findViewById(R.id.course_view_v2);
        mCourseViewV2.setCourseItemRadius(3)
                .setTextTBMargin(dp2px(1), dp2px(1));

        mCourseViewV2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("touch");
                return false;
            }
        });

        mCourseViewV2.setOnItemClickListener(new CourseView.OnItemClickListener() {
            @Override
            public void onClick(List<CourseAncestor> course, View itemView) {
                mDialog = new ShowDetailDialog();
                mDialog.show(CourseActivity.this, (CourseV2) course.get(0), new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mDialog = null;
                    }
                });
            }

            @Override
            public void onLongClick(List<CourseAncestor> courses, View itemView) {
                final CourseV2 course = (CourseV2) courses.get(0);
                DialogHelper dialogHelper = new DialogHelper();
                dialogHelper.showNormalDialog(CourseActivity.this, getString(R.string.confirm_to_delete),
                        "课程 【" + course.getCouName() + "】" + Constant.WEEK[course.getCouWeek()]
                                + "第" + course.getCouStartNode() + "节 " + "",
                        new DialogListener() {
                            @Override
                            public void onPositive(DialogInterface dialog, int which) {
                                super.onPositive(dialog, which);
                                //delete
                                mPresenter.deleteCourse(course.getCouId());
                            }
                        });
            }

            public void onAdd(CourseAncestor course, View addView) {
                Intent intent = new Intent(CourseActivity.this, AddActivity.class);
                intent.putExtra(Constant.INTENT_ADD_COURSE_ANCESTOR, course);
                intent.putExtra(Constant.INTENT_ADD, true);
                startActivity(intent);
            }

        });
    }

    private void updateView() {
        updateCoursePreference();
        AppUtils.updateWidget(getApplicationContext());
    }

    @SuppressLint("SetTextI18n")
    public void updateCoursePreference() {
        updateCurrentWeek();
        mCurrentMonth = TimeUtils.getNowMonth();
        mMMonthTextView.setText(mCurrentMonth + "\n月");

        //get id
        long currentCsNameId = Preferences.getLong(
                getString(R.string.app_preference_current_cs_name_id), 0L);

        LogUtil.i(this, "当前课表-->" + currentCsNameId);
        mPresenter.updateCourseViewData(currentCsNameId);
    }

    @SuppressLint("SetTextI18n")
    private void updateCurrentWeek() {
        mCurrentWeekCount = AppUtils.getCurrentWeek(getBaseContext());
        mTvWeekCount.setText("第" + mCurrentWeekCount + "周");
        mCourseViewV2.setCurrentIndex(mCurrentWeekCount);
    }

    @Override
    public void initFirstStart() {
        boolean isFirst = Preferences.getBoolean(getString(R.string.app_preference_app_is_first_start), true);
        if (!isFirst) {
            return;
        }
        CourseGroupDao groupDao = Cache.instance().getCourseGroupDao();

        CourseGroup defaultGroup = groupDao
                .queryBuilder()
                .where(CourseGroupDao.Properties.CgName.eq("默认课表"))
                .unique();

        long insert;
        if (defaultGroup == null) {
            insert = groupDao.insert(new CourseGroup(0L, "默认", ""));
        } else {
            insert = defaultGroup.getCgId();
        }

        Preferences.putLong(getString(R.string.app_preference_current_cs_name_id), insert);

        //migrate old data
        AppUtils.copyOldData(this);

        Preferences.putBoolean(getString(R.string.app_preference_app_is_first_start), false);

        if (CourseDbDao.instance().loadCsNameList().size() > 0) {
            isV1Update();
        }
    }

    private void isV1Update() {
        LogUtil.e(this, "属于从1.x升级上来");
        toast("请选择正在使用的课表");
        startActivity(new Intent(this, MgActivity.class));
    }

    @Override
    public void setBackground(Bitmap bitmap) {

    }

    @Override
    public void setCourseData(List<CourseV2> courses) {
        mCourseViewV2.clear();

        CourseV2Dao courseV2Dao = Cache.instance().getCourseV2Dao();
        LogUtil.d(this, "当前课程数：" + courses.size());

        for (CourseV2 course : courses) {
            if (course.getCouColor() == null || course.getCouColor() == -1) {
                course.setCouColor(Utils.getRandomColor());
                courseV2Dao.update(course);
            }
            course.init();

            LogUtil.e(this, "即将显示：" + course.toString());

            mCourseViewV2.addCourse(course);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_toolbar_subtitle:
                weekTitle(v);
                break;
        }
    }

    private void weekTitle(View v) {
        animSelectWeek(!mSelectWeekIsShow);
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
    public void onBackPressed() {
        //TODO 保留返回任务？
        // super.onBackPressed();
        moveTaskToBack(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDialog != null) mDialog.dismiss();
        return super.onTouchEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void courseChangeEvent(CourseDataChangeEvent event) {
        //更新主界面
        updateView();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
        //EvenBus
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void setPresenter(CourseContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
