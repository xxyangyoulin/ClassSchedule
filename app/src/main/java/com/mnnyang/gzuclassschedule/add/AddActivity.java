package com.mnnyang.gzuclassschedule.add;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.custom.EditTextLayout;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.DialogListener;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;

public class AddActivity extends BaseActivity implements AddContract.View, View.OnClickListener {

    private AddContract.Presenter mPresenter;

    private EditText mEtName;
    private EditTextLayout mEtlClassroom;
    private EditTextLayout mEtlTeacher;
    private EditTextLayout mEtlTime;
    private EditTextLayout mEtlWeekRange;

    private int mSelectedWeek = 0;
    private int mSelectedStart = 0;
    private int mSelectedEnd = 1;
    private int mEndWeek = 15;
    private int mStartWeek = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initBackToolbar(getString(R.string.add_course));
        initInputView();

        mPresenter = new AddPresenter(this);
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

        String name = mEtName.getText().toString().trim();
        course.setName(name);
        course.setCsName(currentCsName.trim());
        course.setClassRoom(mEtlClassroom.getText().trim());
        course.setTeacher(mEtlTeacher.getText().trim());
        course.setStartWeek(mStartWeek + 1);
        course.setEndWeek(mEndWeek + 1);
        course.setWeek(mSelectedWeek + 1);

        for (int i = mSelectedStart; i <= mSelectedEnd; i++) {
            course.addNode(i+1);
        }

        mPresenter.addCourse(course);
    }

    /**
     * 通知更新
     */
    private void notifiUpdate() {
        Intent intent = new Intent();
        intent.setAction(Constant.INTENT_UPDATE);
        sendBroadcast(intent);
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
        WheelView wvEnd = (WheelView) view.findViewById(R.id.wv_week_end);

        wvStart.setWheelAdapter(new ArrayWheelAdapter(this)); // 文本数据源
        wvEnd.setWheelAdapter(new ArrayWheelAdapter(this)); // 文本数据源
        wvStart.setSkin(WheelView.Skin.Holo);
        wvEnd.setSkin(WheelView.Skin.Holo);

        wvStart.setSelection(mStartWeek);
        wvEnd.setSelection(mEndWeek);

        wvStart.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                mStartWeek = position;
            }
        });
        wvEnd.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                mEndWeek = position;
            }
        });

        ArrayList<String> weeks = new ArrayList<String>();
        for (int i = 1; i <= 25; i++) {
            weeks.add("第" + i + "周");
        }
        wvStart.setWheelData(weeks);
        wvEnd.setWheelData(weeks);

        helper.showCustomDialog(this, view, "选择周数", new DialogListener() {
            @Override
            public void onPositive(DialogInterface dialog, int which) {
                super.onPositive(dialog, which);
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
        wvWeek.setWheelAdapter(new ArrayWheelAdapter(this)); // 文本数据源
        wvStart.setWheelAdapter(new ArrayWheelAdapter(this)); // 文本数据源
        wvEnd.setWheelAdapter(new ArrayWheelAdapter(this)); // 文本数据源

        wvWeek.setSkin(WheelView.Skin.Holo); // common皮肤
        wvStart.setSkin(WheelView.Skin.Holo);
        wvEnd.setSkin(WheelView.Skin.Holo);


        for (int i = 1; i <= 7; i++) {
            weeks.add(Constant.WEEK[i]);
        }

        int maxNode = Preferences.getInt(getString(R.string.app_preference_max_node), 11);
        for (int i = 1; i <= maxNode; i++) {
            nodes.add("第" + i + "节");
        }

        wvWeek.setWheelData(weeks);  // 数据集合
        wvStart.setWheelData(nodes);
        wvEnd.setWheelData(nodes);

        wvWeek.setSelection(mSelectedWeek);
        wvStart.setSelection(mSelectedStart);
        wvEnd.setSelection(mSelectedEnd);

        wvWeek.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                mSelectedWeek = position;
            }
        });

        wvStart.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                mSelectedStart = position;

                if (mSelectedEnd < mSelectedStart) {
                    wvEnd.setSelection(mSelectedStart);
                }
            }
        });

        wvEnd.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                mSelectedEnd = position;
                if (mSelectedEnd < mSelectedStart) {
                    wvEnd.setSelection(mSelectedStart);
                }
            }
        });

        helper.showCustomDialog(this, view, "选择上课时间", new DialogListener() {
            @Override
            public void onPositive(DialogInterface dialog, int which) {
                super.onPositive(dialog, which);

                showWeekNode();
            }
        });
    }

    private void showWeekNode() {
        String string;
        if (mSelectedStart == mSelectedEnd) {
            string = getString(R.string.string_course_time_2,
                    Constant.WEEK[mSelectedWeek + 1], mSelectedStart + 1);
        } else {
            string = getString(R.string.string_course_time,
                    Constant.WEEK[mSelectedWeek + 1],
                    mSelectedStart + 1, mSelectedEnd + 1);
        }
        mEtlTime.setText(string);
    }

    private void showWeekRange() {
        mEtlWeekRange.setText(getString(R.string.string_week_range,
                mStartWeek + 1, mEndWeek + 1));
    }

    @Override
    public void showAddFail(String msg) {
        toast(msg);
    }

    @Override
    public void onAddSucceed(Course course) {
        toast(course.getName() + "添加成功");
        notifiUpdate();
    }
}
