package com.mnnyang.gzuclassschedule.add;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.custom.EditTextLayout;
import com.mnnyang.gzuclassschedule.custom.WheelView;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.DialogListener;
import com.mnnyang.gzuclassschedule.utils.LogUtils;
import com.mnnyang.gzuclassschedule.utils.Preferences;

import java.util.ArrayList;

public class AddActivity extends BaseActivity implements AddContract.View, View.OnClickListener {

    private AddContract.Presenter mPresenter;

    private EditText mEtName;
    private EditTextLayout mEtlClassroom;
    private EditTextLayout mEtlTeacher;
    private EditTextLayout mEtlTime;
    private EditTextLayout mEtlWeekRange;

    private int mWeek = 1;
    private int mNodeStart = 1;
    private int mEndWeek = 16;
    private int mStartWeek = 1;
    private int mNodeEnd = 2;

    private int mSelectedWeek = mWeek;
    private int mSelectedNodeStart = mNodeStart;
    private int mSelectedNodeEnd = mNodeEnd;
    private int mSelectedStartWeek = mStartWeek;
    private int mSelectedEndWeek = mEndWeek;

    private int mWeekType = Course.WEEK_ALL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initBackToolbar(getString(R.string.add_course));
        initInputView();
        initDefaultValues();

        mPresenter = new AddPresenter(this);
    }

    private void initDefaultValues() {
        showWeekNode();
        showWeekRange();
    }

    private void initInputView() {
        mEtName = findViewById(R.id.et_course_name);
        mEtlClassroom = findViewById(R.id.etl_classroom);
        mEtlTeacher = findViewById(R.id.etl_teacher);
        mEtlTime = findViewById(R.id.etl_time);
        mEtlWeekRange = findViewById(R.id.etl_week_range);

        mEtlTime.setOnClickListener(this);
        mEtlWeekRange.setOnClickListener(this);
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
                .setStartWeek(mStartWeek)
                .setWeekType(mWeekType)
                .setEndWeek(mEndWeek)
                .setWeek(mWeek);

        for (int i = mNodeStart; i <= mNodeEnd; i++) {
            course.addNode(i);
        }

        mPresenter.addCourse(course);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etl_time:
                showSelectTimeDialog();
                break;
            case R.id.etl_week_range:
                showWeekRangeDialog();
                break;

        }
    }

    private void showWeekRangeDialog() {
        //TODO show bug
        DialogHelper helper = new DialogHelper();
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_select_week_range, null);

        WheelView wvStart = (WheelView) view.findViewById(R.id.wv_week_start);
        final WheelView wvEnd = (WheelView) view.findViewById(R.id.wv_week_end);
        RadioGroup rgWeekType = (RadioGroup) view.findViewById(R.id.rg_week_type);

        ArrayList<String> weeks = new ArrayList<String>();
        for (int i = 1; i <= 25; i++) {
            weeks.add("第" + i + "周");
        }

        wvStart.setItems(weeks);
        wvEnd.setItems(weeks);
        wvStart.setSeletion(mSelectedStartWeek - 1);
        wvEnd.setSeletion(mSelectedEndWeek - 1);

        wvStart.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                LogUtils.e(this, selectedIndex + "");
                mStartWeek = selectedIndex;
                if (mStartWeek > mEndWeek) {
                    wvEnd.setSeletion(mStartWeek - 1);
                }
            }
        });
        wvEnd.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mEndWeek = selectedIndex;
                if (mStartWeek > mEndWeek) {
                    wvEnd.setSeletion(mStartWeek - 1);
                }
            }
        });

        rgWeekType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.arb_all:
                        mWeekType = Course.WEEK_ALL;
                        break;

                    case R.id.arb_single:
                        mWeekType = Course.WEEK_SINGLE;
                        break;

                    case R.id.arb_double:
                        mWeekType = Course.WEEK_DOUBLE;
                        break;
                }
            }
        });

        helper.showCustomDialog(this, view, getString(R.string.select_week_count), new DialogListener() {
            @Override
            public void onPositive(DialogInterface dialog, int which) {
                super.onPositive(dialog, which);
                mSelectedStartWeek = mStartWeek;
                mSelectedEndWeek = mEndWeek;
                showWeekRange();
            }
        });
    }


    private void showSelectTimeDialog() {
        DialogHelper helper = new DialogHelper();
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_select_week_and_node, null);
        final ArrayList<String> weeks = new ArrayList<>();
        final ArrayList<String> nodes = new ArrayList<>();

        WheelView wvWeek = (WheelView) view.findViewById(R.id.wv_week);
        WheelView wvStart = (WheelView) view.findViewById(R.id.wv_start_node);
        final WheelView wvEnd = (WheelView) view.findViewById(R.id.wv_end_node);

        final int noonNode = Preferences.getInt(getString(R.string.app_preference_noon_node), Integer.parseInt(getString(R.string.default_noon_node)));

        for (int i = 1; i <= 7; i++) {
            weeks.add(Constant.WEEK[i]);
        }

        int maxNode = Preferences.getInt(getString(R.string.app_preference_max_node), Constant.DEFAULT_MAX_NODE_COUNT);
        for (int i = 1; i <= maxNode; i++) {
            nodes.add("第" + i + "节");
        }

        wvWeek.setItems(weeks);
        wvStart.setItems(nodes);
        wvEnd.setItems(nodes);

        wvWeek.setSeletion(mSelectedWeek - 1);
        wvStart.setSeletion(mSelectedNodeStart - 1);
        wvEnd.setSeletion(mSelectedNodeEnd - 1);
        wvWeek.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mStartWeek = selectedIndex;
            }
        });

        wvWeek.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mWeek = selectedIndex;
            }
        });

        wvStart.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mNodeStart = selectedIndex;

                if (mNodeStart > mNodeEnd) {
                    wvEnd.setSeletion(mNodeStart - 1);
                    return;
                }

                if (mNodeEnd > noonNode && mNodeStart <= noonNode) {
                    wvEnd.setSeletion(noonNode - 1);
                }
            }
        });

        wvEnd.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mNodeEnd = selectedIndex;

                if (mNodeEnd > noonNode && mNodeStart <= noonNode) {
                    toast("早上课程的最大为 " + noonNode + " 节");
                    wvEnd.setSeletion(noonNode - 1);
                    return;
                }

                if (mNodeStart > mNodeEnd) {
                    wvEnd.setSeletion(mNodeStart - 1);
                }
            }
        });

        helper.showCustomDialog(this, view, getString(R.string.select_course_time), new DialogListener() {
            @Override
            public void onPositive(DialogInterface dialog, int which) {
                super.onPositive(dialog, which);
                mSelectedWeek = mWeek;
                mSelectedNodeStart = mNodeStart;
                mSelectedNodeEnd = mNodeEnd;
                showWeekNode();
            }
        });
    }

    private void showWeekNode() {
        String string;
        if (mNodeStart == mNodeEnd) {
            string = getString(R.string.string_course_time_2,
                    Constant.WEEK[mSelectedWeek], mSelectedNodeStart);
        } else {
            string = getString(R.string.string_course_time,
                    Constant.WEEK[mSelectedWeek],
                    mSelectedNodeStart, mSelectedNodeEnd);
        }
        mEtlTime.setText(string);
    }

    private void showWeekRange() {
        mEtlWeekRange.setText(getString(R.string.string_week_range,
                mSelectedStartWeek, mSelectedEndWeek));
    }

    @Override
    public void showAddFail(String msg) {
        toast(msg);
    }

    @Override
    public void onAddSucceed(Course course) {
        toast(course.getName() + getString(R.string.add_succeed));
        notifiUpdateMainPage(Constant.INTENT_UPDATE_TYPE_COURSE);
        finish();
    }
}
