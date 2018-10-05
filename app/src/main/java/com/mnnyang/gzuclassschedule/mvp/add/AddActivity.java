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
import com.mnnyang.gzuclassschedule.app.AppUtils;
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

public class AddActivity extends BaseActivity implements AddContract.View, View.OnClickListener {

    private AddContract.Presenter mPresenter;

    private boolean mAddMode = true;

    private CourseAncestor mAncestor;
    private CourseV2 mIntentCourseV2;

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

        mIvSubmit.setImageResource(R.drawable.ic_done_black_24dp);
        initBackToolbar(mAddMode ? "添加" : "编辑");
        addLocation(false);
    }

    private void initListener() {
        mIvAddLocation.setOnClickListener(this);
        mIvSubmit.setOnClickListener(this);
    }

    private void handleIntent() {
        Intent intent = getIntent();
        mAncestor = (CourseAncestor) intent.getSerializableExtra(Constant.INTENT_ADD_COURSE_ANCESTOR);
        if (mAncestor != null) {
            mAddMode = true;
        } else {
            mIntentCourseV2 = (CourseV2) intent.getSerializableExtra(Constant.INTENT_EDIT_COURSE);
            if (mIntentCourseV2 != null) {
                mAddMode = false; //is edit mode
                mIntentCourseV2.init();// 从桌面点击过来必然已经初始化 其他位置过来不一定
            }
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
        //name
        String name = mEtlName.getText().trim();
        if (TextUtils.isEmpty(name)) {
            toast("课程名称不能为空！");
            return;
        }

        //teacher
        String teacher = mEtlTeacher.getText().trim();
        //group

        long couCgId = Preferences.getLong(getString(R.string.app_preference_current_cs_name_id), 0);
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

                if (mAddMode) {
                    courseV2.setCouCgId(couCgId);
                    courseV2.init();
                    long insert = Cache.instance().getCourseV2Dao().insert(courseV2);
                    if (insert > 0) {
                        toast("添加成功！" + insert);
                        EventBus.getDefault().post(new CourseDataChangeEvent());
                        finish();
                    } else {
                        toast("添加失败！");
                    }
                } else {
                    //edit mode
                    if (!(courseV2.getCouId() > 0)) {
                        throw new RuntimeException("要更新的课程没有找到id");
                    }
                    courseV2.init();
                    Cache.instance().getCourseV2Dao().update(courseV2);
                    toast("编辑成功！");
                    EventBus.getDefault().post(new CourseDataChangeEvent());
                    finish();
                }

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

            initEmptyLocation(locationItem);

        } else {// 建立默认的上课时间和上课地点
            locationItem.findViewById(R.id.iv_clear).setVisibility(View.INVISIBLE);

            if (mAncestor != null) {
                // 屏幕点击过来

                CourseV2 defaultCourse = new CourseV2().setCouOnlyId(AppUtils.createUUID())
                        .setCouAllWeek(Constant.DEFAULT_ALL_WEEK)
                        .setCouWeek(mAncestor.getRow())
                        .setCouStartNode(mAncestor.getCol())
                        .setCouNodeCount(mAncestor.getRowNum())
                        .init();

                initNodeInfo(locationItem, defaultCourse);
            } else if (mIntentCourseV2 != null) {
                // 编辑过来
                initNodeInfo(locationItem, mIntentCourseV2);

                mEtlName.setText(mIntentCourseV2.getCouName());
                mEtlTeacher.setText(mIntentCourseV2.getCouTeacher());
            } else {
                //
                LogUtil.e(this, "initEmptyLocation");
                initEmptyLocation(locationItem);
            }
        }

        locationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLocationItem(locationItem);
            }
        });

        mLayoutLocationContainer.addView(locationItem, params);
    }

    private void initEmptyLocation(LinearLayout locationItem) {
        CourseV2 defaultCourse = new CourseV2().setCouOnlyId(AppUtils.createUUID())
                .setCouAllWeek(Constant.DEFAULT_ALL_WEEK)
                .setCouWeek(1)
                .setCouStartNode(1)
                .setCouNodeCount(1);
        initNodeInfo(locationItem, defaultCourse);
    }

    private void initNodeInfo(LinearLayout locationItem, CourseV2 courseV2) {
        TextView tvText = locationItem.findViewById(R.id.tv_text);
        String builder = "周" + Constant.WEEK_SINGLE[courseV2.getCouWeek() - 1] +
                " 第" + courseV2.getCouStartNode() + "-" +
                (courseV2.getCouStartNode() + courseV2.getCouNodeCount() - 1) + "节";
        tvText.setText(builder);

        locationItem.setTag(courseV2);
    }

    private void clickLocationItem(final LinearLayout locationItem) {
        PopupWindowDialog dialog = new PopupWindowDialog();

        CourseV2 courseV2 = null;
        Object obj = locationItem.getTag();
        // has tag data
        if (obj != null && obj instanceof CourseV2) {
            courseV2 = (CourseV2) obj;
        } else {
            throw new RuntimeException("Course data tag not be found");
        }

        dialog.showSelectTimeDialog(this, courseV2, new PopupWindowDialog.SelectTimeCallback() {
            @Override
            public void onSelected(CourseV2 course) {
                StringBuilder builder = new StringBuilder();
                builder.append("周").append(Constant.WEEK_SINGLE[course.getCouWeek() - 1])
                        .append(" 第").append(course.getCouStartNode()).append("-")
                        .append(course.getCouStartNode() + course.getCouNodeCount() - 1).append("节");
                if (!TextUtils.isEmpty(course.getCouLocation())) {
                    builder.append("【").append(course.getCouLocation()).append("】");
                }

                ((TextView) locationItem.findViewById(R.id.tv_text))
                        .setText(builder.toString());
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
}
