package com.mnnyang.gzuclassschedule.add;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.custom.EditTextLayout;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.spec.PopupWindowDialog;

public class AddActivity extends BaseActivity implements AddContract.View, View.OnClickListener {

    private AddContract.Presenter mPresenter;

    private EditText mEtName;
    private EditTextLayout mEtlClassroom;
    private EditTextLayout mEtlTeacher;
    private EditTextLayout mEtlTime;
    private EditTextLayout mEtlWeekRange;

    private int mSelectedWeek = 1;
    private int mSelectedNodeStart = 1;
    private int mSelectedNodeEnd = 2;
    private int mSelectedStartWeek = 1;
    private int mSelectedEndWeek = 16;

    private int mWeekType = Course.WEEK_ALL;

    /**
     * 编辑模式
     */
    private boolean isEditMode;
    private int mCourseId;
    private Button mBtnRemove;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initBackToolbar(getString(R.string.add_course));

        initView();

        initDefaultValues();

        mPresenter = new AddPresenter(this);
    }

    private void initDefaultValues() {
        Intent intent = getIntent();
        Course course = (Course) intent.getSerializableExtra(Constant.INTENT_COURSE);
        if (course != null) {
            //set toolbar title
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(getString(R.string.edit_course));
            }

            mCourseId = course.getCourseId();
            LogUtil.i(TAG, "id====" + mCourseId);

            mEtName.setText(course.getName());
            mEtlClassroom.setText(course.getClassRoom());
            mEtlTeacher.setText(course.getTeacher());

            mSelectedWeek = course.getWeek();
            mSelectedStartWeek = course.getStartWeek();
            mSelectedEndWeek = course.getEndWeek();
            mSelectedNodeStart = course.getNodes().get(0);
            mSelectedNodeEnd = course.getNodes().get(course.getNodes().size() - 1);
            mWeekType = course.getWeekType();


            mBtnRemove.setVisibility(View.VISIBLE);
            isEditMode = true;
        }

        updateWeekNode();
        updateWeekRange();
    }

    private void initView() {
        mEtName = findViewById(R.id.et_course_name);
        mEtlClassroom = findViewById(R.id.etl_classroom);
        mEtlTeacher = findViewById(R.id.etl_teacher);
        mEtlTime = findViewById(R.id.etl_time);
        mEtlWeekRange = findViewById(R.id.etl_week_range);

        mBtnRemove = findViewById(R.id.btn_remove);

        mEtlTime.setOnClickListener(this);
        mEtlWeekRange.setOnClickListener(this);

        mBtnRemove.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_add:
                add();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void add() {
        Course course = new Course();

        String currentCsName = Preferences.getString(getString(
                R.string.app_preference_current_sd_name),
                getString(R.string.default_course_name));

        course.setName(mEtName.getText().toString().trim())
                .setCsName(currentCsName.trim())
                .setClassRoom(mEtlClassroom.getText().trim())
                .setTeacher(mEtlTeacher.getText().trim())

                .setStartWeek(mSelectedStartWeek)
                .setEndWeek(mSelectedEndWeek)
                .setWeekType(mWeekType)
                .setWeek(mSelectedWeek);

        for (int i = mSelectedNodeStart; i <= mSelectedNodeEnd; i++) {
            course.addNode(i);
        }

        if (isEditMode) {
            course.setCourseId(mCourseId);
            mPresenter.updateCourse(course);
            return;
        }

        mPresenter.addCourse(course);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etl_time:
                timeAction();
                break;
            case R.id.etl_week_range:
                rangeAction();
                break;
            case R.id.btn_remove:
                remove();
                break;
        }
    }

    private void remove() {
        if (!isEditMode) {
            return;
        }
        mPresenter.removeCourse(mCourseId);
    }

    private void rangeAction() {
        new PopupWindowDialog().showWeekRangeDialog(this,
                mSelectedStartWeek, mSelectedEndWeek, mWeekType,
                new PopupWindowDialog.WeekRangeCallback() {
                    @Override
                    public void onSelected(int start, int end, int type) {
                        mSelectedStartWeek = start;
                        mSelectedEndWeek = end;
                        mWeekType = type;
                        updateWeekRange();
                    }
                });
    }

    private void timeAction() {
        new PopupWindowDialog().showSelectTimeDialog(this,
                mSelectedWeek, mSelectedNodeStart, mSelectedNodeEnd,
                new PopupWindowDialog.SelectTimeCallback() {
                    @Override
                    public void onSelected(int week, int nodeStart, int endStart) {
                        mSelectedWeek = week;
                        mSelectedNodeStart = nodeStart;
                        mSelectedNodeEnd = endStart;
                        updateWeekNode();
                    }
                });
    }

    private void updateWeekNode() {
        String string;
        if (mSelectedNodeStart == mSelectedNodeEnd) {
            string = getString(R.string.string_course_time_2,
                    Constant.WEEK[mSelectedWeek], mSelectedNodeStart);
        } else {
            string = getString(R.string.string_course_time,
                    Constant.WEEK[mSelectedWeek],
                    mSelectedNodeStart, mSelectedNodeEnd);
        }
        mEtlTime.setText(string);
    }

    private void updateWeekRange() {
        mEtlWeekRange.setText(getString(R.string.string_week_range,
                mSelectedStartWeek, mSelectedEndWeek));
    }

    @Override
    public void showAddFail(String msg) {
        toast(msg);
    }

    @Override
    public void onAddSucceed(Course course) {
        toast("【" + course.getName() + "】" + getString(R.string.add_succeed));
        notifiUpdateMainPage(Constant.INTENT_UPDATE_TYPE_COURSE);
        finish();
    }

    @Override
    public void onDelSucceed() {
        toast(getString(R.string.delete_succeed));
        notifiUpdateMainPage(Constant.INTENT_UPDATE_TYPE_COURSE);
        finish();
    }

    @Override
    public void onUpdateSucceed(Course course) {
        toast("【" + course.getName() + "】" + getString(R.string.update_succeed));
        notifiUpdateMainPage(Constant.INTENT_UPDATE_TYPE_COURSE);
        finish();
    }
}
