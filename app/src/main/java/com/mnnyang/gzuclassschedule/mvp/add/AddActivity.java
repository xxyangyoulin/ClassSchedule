package com.mnnyang.gzuclassschedule.mvp.add;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Cache;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.custom.EditTextLayout;
import com.mnnyang.gzuclassschedule.custom.course.CourseAncestor;
import com.mnnyang.gzuclassschedule.data.beanv2.CourseV2;
import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.ScreenUtils;
import com.mnnyang.gzuclassschedule.utils.event.CourseDataChangeEvent;
import com.mnnyang.gzuclassschedule.utils.spec.PopupWindowDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class AddActivity extends BaseActivity implements AddContract.View, View.OnClickListener {

    private boolean mAddMode = true;
    private AddContract.Presenter mPresenter;
    private CourseAncestor intentAncestor;
    private int mDefaultWeek = 1;
    private int mDefaultStartNode = 1;
    private int mDefaultEndNode = 2;


    private ImageView mIvAddLocation;
    private LinearLayout mLayoutLocationContainer;
    private ImageView mIvSubmit;
    private EditTextLayout mEtlName;
    private EditTextLayout mEtlTeacher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        handleIntent();
        initView();

        new AddPresenter(this).start();
        initListener();
    }

    private void initView() {
        mEtlName = findViewById(R.id.etl_name);
        mEtlTeacher = findViewById(R.id.etl_teacher);

        mIvAddLocation = findViewById(R.id.iv_add_location);
        mLayoutLocationContainer = findViewById(R.id.layout_location_container);
        mIvSubmit = findViewById(R.id.iv_submit);

        initBackToolbar("新增");
        addLocation(false);
    }

    private void initListener() {
        mIvAddLocation.setOnClickListener(this);
        mIvSubmit.setOnClickListener(this);
    }

    private void handleIntent() {
        Intent intent = getIntent();
        intentAncestor = (CourseAncestor) intent.getSerializableExtra(Constant.INTENT_ADD_COURSE_ANCESTOR);
        if (intentAncestor != null) {
            mAddMode = true;
            mDefaultWeek = intentAncestor.getRow();
            mDefaultStartNode = intentAncestor.getCol();
            mDefaultEndNode = mDefaultStartNode + intentAncestor.getRowNum();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_location:
                addLocation(true);
                break;
            case R.id.iv_submit:
                submit();
                break;
        }
    }

    private void submit() {
        String name = mEtlName.getText().trim();
        if (TextUtils.isEmpty(name)) {
            toast("课程名称不能为空！");
            return;
        }

        String teacher = mEtlTeacher.getText().trim();

        int childCount = mLayoutLocationContainer.getChildCount();

        boolean hasLocation = false;
        for (int i = 0; i < childCount; i++) {
            View locationItem = mLayoutLocationContainer.getChildAt(i);
            Object obj = locationItem.getTag();

            if (obj != null) {
                hasLocation = true;
                CourseV2 courseV2 = (CourseV2) obj;
                courseV2.setCouName(name);
                courseV2.setCouTeacher(teacher);

                //TODO save
                LogUtil.e(this, "add---" + courseV2);
                Cache.instance().getCourseV2Dao().insert(courseV2);
                EventBus.getDefault().post(new CourseDataChangeEvent());
                finish();
            }
        }

        if (!hasLocation) {
            toast("没有设置课程时间！");
        }
    }

    private void addLocation(boolean closeable) {
        final LinearLayout locationItem = (LinearLayout) View.inflate(this,
                R.layout.layout_location_item, null);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = ScreenUtils.dp2px(8);

        if (closeable) {
            locationItem.findViewById(R.id.iv_clear).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLayoutLocationContainer.removeView(locationItem);
                }
            });
        } else {
            locationItem.findViewById(R.id.iv_clear).setVisibility(View.INVISIBLE);
        }

        if (intentAncestor != null) {
            TextView tvText = locationItem.findViewById(R.id.tv_text);
            String builder = "周" + Constant.WEEK_SINGLE[mDefaultWeek - 1] +
                    " 第" + mDefaultStartNode + "-" +
                    mDefaultEndNode + "节";
            tvText.setText(builder);

            locationItem.setTag(new CourseV2()
                    .setCouWeek(mDefaultWeek)
                    .setCouAllWeek(Constant.DEFAULT_ALL_WEEK)
                    .setCouStartNode(mDefaultStartNode)
                    .setCouNodeCount(mDefaultEndNode - mDefaultStartNode + 1));
        }

        locationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLocationItem(locationItem);
            }
        });

        mLayoutLocationContainer.addView(locationItem, params);
    }

    private void clickLocationItem(final LinearLayout locationItem) {
        PopupWindowDialog dialog = new PopupWindowDialog();
        dialog.showSelectTimeDialog(this, "博学楼235", null,
                mDefaultWeek, mDefaultStartNode, mDefaultEndNode, new PopupWindowDialog.SelectTimeCallback() {
                    @Override
                    public void onSelected(String location, List<Integer> selectWeeks, int week, int nodeStart, int nodeEnd) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("周").append(Constant.WEEK_SINGLE[week - 1])
                                .append(" 第").append(nodeStart).append("-")
                                .append(nodeEnd).append("节");
                        if (!TextUtils.isEmpty(location)) {
                            builder.append("【").append(location).append("】");
                        }

                        ((TextView) locationItem.findViewById(R.id.tv_text))
                                .setText(builder.toString());


                        builder = new StringBuilder();
                        for (Integer selectWeek : selectWeeks) {
                            builder.append(selectWeek).append(",");
                        }
                        String allWeek = builder.toString();
                        if (allWeek.length() > 0) {
                            allWeek = allWeek.substring(0, allWeek.length() - 1);
                        }

                        long couCgId = Preferences.getLong(getString(R.string.app_preference_current_cs_name_id), 0);

                        CourseV2 courseV2 = new CourseV2()
                                .setCouLocation(location)
                                .setCouWeek(week)
                                .setCouAllWeek(allWeek)
                                .setCouStartNode(nodeStart)
                                .setCouNodeCount(nodeEnd - nodeStart + 1)
                                .setCouCgId(couCgId);

                        locationItem.setTag(courseV2);
                    }
                });
    }

    @Override
    public void showAddFail(String msg) {

    }

    @Override
    public void onAddSucceed(CourseV2 courseV2) {

    }

    @Override
    public void onDelSucceed() {

    }

    @Override
    public void onUpdateSucceed(CourseV2 courseV2) {

    }

    @Override
    public void setPresenter(AddContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    //
    //private AddContract.Presenter mPresenter;
    //
    //private EditText mEtName;
    //private EditTextLayout mEtlClassroom;
    //private EditTextLayout mEtlTeacher;
    //private EditTextLayout mEtlTime;
    //private EditTextLayout mEtlWeekRange;
    //
    //private int mSelectedWeek = 1;
    //private int mSelectedNodeStart = 1;
    //private int mSelectedNodeEnd = 2;
    //private int mSelectedStartWeek = 1;
    //private int mSelectedEndWeek = 16;
    //
    ////private int mWeekType = Course.SHOW_ALL;
    //
    ///**
    // * 编辑模式
    // */
    //private boolean isEditMode;
    //private CourseV2 mCourseV2;
    //
    //private long mCourseId;
    //private Button mBtnRemove;
    //private int mSelectedNodeCount;
    //
    //@Override
    //public void onCreate(Bundle savedInstanceState) {
    //    super.onCreate(savedInstanceState);
    //    setContentView(R.layout.activity_add);
    //    initBackToolbar(getString(R.string.add_course));
    //
    //    initView();
    //
    //    initDefaultValues();
    //
    //    new AddPresenter(this);
    //}
    //
    //private void initDefaultValues() {
    //    Intent intent = getIntent();
    //    CourseV2 courseV2 = (CourseV2) intent.getSerializableExtra(Constant.INTENT_COURSE);
    //
    //    boolean isAdd = intent.getBooleanExtra(Constant.INTENT_ADD, false);
    //
    //    if (courseV2 != null) {
    //        //set toolbar title
    //        if (getSupportActionBar() != null) {
    //            getSupportActionBar().setTitle(getString(R.string.edit_course));
    //        }
    //
    //        mCourseV2 = courseV2;
    //        mCourseId = courseV2.getCouId();
    //
    //        mEtName.setText(courseV2.getCouName());
    //        mEtlClassroom.setText(courseV2.getCouLocation());
    //        mEtlTeacher.setText(courseV2.getCouTeacher());
    //
    //        mSelectedWeek = courseV2.getCouWeek();
    //        //mSelectedStartWeek = courseV2.getStartWeek();
    //        //mSelectedEndWeek = courseV2.getEndWeek();
    //        mSelectedNodeStart = courseV2.getCouStartNode();
    //        //mSelectedNodeEnd = courseV2.getNodes().get(course.getNodes().size() - 1);
    //        mSelectedNodeCount = courseV2.getCouNodeCount();
    //        //mWeekType = courseV2.getWeekType();
    //
    //        mBtnRemove.setVisibility(View.VISIBLE);
    //        isEditMode = true;
    //
    //    } else if (isAdd) {
    //        CourseAncestor ancestor = (CourseAncestor) intent.getSerializableExtra(Constant.INTENT_COURSE_ANCESTOR);
    //        mSelectedWeek = ancestor.getRow();
    //        mSelectedNodeStart = ancestor.getCol();
    //        mSelectedNodeEnd = ancestor.getCol() + ancestor.getRowNum();
    //    }
    //
    //    updateWeekNode();
    //    updateWeekRange();
    //}
    //
    //private void initView() {
    //    mEtName = findViewById(R.id.et_course_name);
    //    mEtlClassroom = findViewById(R.id.etl_classroom);
    //    mEtlTeacher = findViewById(R.id.etl_teacher);
    //    mEtlTime = findViewById(R.id.etl_time);
    //    mEtlWeekRange = findViewById(R.id.etl_week_range);
    //
    //    mBtnRemove = findViewById(R.id.btn_remove);
    //
    //    mEtlTime.setOnClickListener(this);
    //    mEtlWeekRange.setOnClickListener(this);
    //
    //    mBtnRemove.setOnClickListener(this);
    //}
    //
    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    //    getMenuInflater().inflate(R.menu.toolbar_add, menu);
    //    return true;
    //}
    //
    //@Override
    //public boolean onOptionsItemSelected(MenuItem item) {
    //    switch (item.getItemId()) {
    //        case android.R.id.home:
    //            finish();
    //            break;
    //        case R.id.action_add:
    //            add();
    //            break;
    //    }
    //    return super.onOptionsItemSelected(item);
    //}
    //
    //
    //private void add() {
    //    CourseV2 courseV2 = new CourseV2();
    //
    //    long currentCsNameId = Preferences.getInt(getString(R.string.app_preference_current_cs_name_id), 0);
    //
    //    CourseGroup group = Cache.instance().getCourseGroupDao().queryBuilder()
    //            .where(CourseGroupDao.Properties.CgId.eq(currentCsNameId)).unique();
    //
    //    if (group == null) {
    //        throw new NullPointerException("当前课表为空");
    //    }
    //
    //    LogUtil.i(this, "当前课表-->" + currentCsNameId);
    //
    //    courseV2.setCouName(mEtName.getText().toString().trim())
    //            .setCouCgId(currentCsNameId)
    //            .setCouLocation(mEtlClassroom.getText().trim())
    //            .setCouTeacher(mEtlTeacher.getText().trim())
    //            .setCouWeek(mSelectedWeek);
    //
    //    courseV2.setCouStartNode(mSelectedNodeStart);
    //    courseV2.setCouNodeCount(mSelectedNodeEnd - mSelectedNodeStart + 1);
    //
    //    if (isEditMode) {
    //        courseV2.setCouId(mCourseId);
    //        mPresenter.updateCourse(courseV2);
    //        return;
    //    }
    //
    //    mPresenter.addCourse(courseV2);
    //}
    //
    //@Override
    //public void onClick(View v) {
    //    switch (v.getId()) {
    //        case R.id.etl_time:
    //            timeAction();
    //            break;
    //        case R.id.etl_week_range:
    //            rangeAction();
    //            break;
    //        case R.id.btn_remove:
    //            remove();
    //            break;
    //    }
    //}
    //
    //private void remove() {
    //    if (!isEditMode) {
    //        return;
    //    }
    //
    //    new DialogHelper().showNormalDialog(this, getString(R.string.confirm_to_delete),
    //            "课程 【" + mCourseV2.getCouName() + "】" + Constant.WEEK[mCourseV2.getCouWeek()]
    //                    + "第" + mCourseV2.getCouStartNode() + "节 " + "",
    //            new DialogListener() {
    //                @Override
    //                public void onPositive(DialogInterface dialog, int which) {
    //                    super.onPositive(dialog, which);
    //                    mPresenter.removeCourse(mCourseId);
    //                }
    //            });
    //}
    //
    //private void rangeAction() {
    //    new PopupWindowDialog().showWeekRangeDialog(this,
    //            mSelectedStartWeek, mSelectedEndWeek, 0,
    //            new PopupWindowDialog.WeekRangeCallback() {
    //                @Override
    //                public void onSelected(int start, int end, int type) {
    //                    mSelectedStartWeek = start;
    //                    mSelectedEndWeek = end;
    //                    //mWeekType = type;
    //                    updateWeekRange();
    //                }
    //            });
    //}
    //
    //private void timeAction() {
    //    new PopupWindowDialog().showSelectTimeDialog(this,
    //            mSelectedWeek, mSelectedNodeStart, mSelectedNodeEnd,
    //            new PopupWindowDialog.SelectTimeCallback() {
    //                @Override
    //                public void onSelected(int week, int nodeStart, int endStart) {
    //                    mSelectedWeek = week;
    //                    mSelectedNodeStart = nodeStart;
    //                    mSelectedNodeEnd = endStart;
    //                    updateWeekNode();
    //                }
    //            });
    //}
    //
    //private void updateWeekNode() {
    //    String string;
    //    if (mSelectedNodeStart == mSelectedNodeEnd) {
    //        string = getString(R.string.string_course_time_2,
    //                Constant.WEEK[mSelectedWeek], mSelectedNodeStart);
    //    } else {
    //        string = getString(R.string.string_course_time,
    //                Constant.WEEK[mSelectedWeek],
    //                mSelectedNodeStart, mSelectedNodeEnd);
    //    }
    //    mEtlTime.setText(string);
    //}
    //
    //private void updateWeekRange() {
    //    mEtlWeekRange.setText(getString(R.string.string_week_range,
    //            mSelectedStartWeek, mSelectedEndWeek));
    //}
    //
    //
    //@Override
    //public void showAddFail(String msg) {
    //    toast(msg);
    //}
    //
    //
    //@Override
    //public void onAddSucceed(CourseV2 courseV2) {
    //    toast("【" + courseV2.getCouName() + "】" + getString(R.string.add_succeed));
    //    EventBus.getDefault().post(new CourseDataChangeEvent());
    //    AppUtils.updateWidget(this);
    //    finish();
    //}
    //
    //@Override
    //public void onDelSucceed() {
    //    toast(getString(R.string.delete_succeed));
    //    EventBus.getDefault().post(new CourseDataChangeEvent());
    //    AppUtils.updateWidget(this);
    //    finish();
    //}
    //
    //@Override
    //public void onUpdateSucceed(CourseV2 courseV2) {
    //    toast("【" + courseV2.getCouName() + "】" + getString(R.string.update_succeed));
    //    EventBus.getDefault().post(new CourseDataChangeEvent());
    //    AppUtils.updateWidget(this);
    //    finish();
    //}
    //
    //@Override
    //public void setPresenter(AddContract.Presenter presenter) {
    //    mPresenter = presenter;
    //}
    //
    //@Override
    //protected void onDestroy() {
    //    mPresenter.onDestroy();
    //    super.onDestroy();
    //}
}
