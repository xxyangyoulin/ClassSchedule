package com.mnnyang.gzuclassschedule.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.custom.CourseView;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;
import com.mnnyang.gzuclassschedule.setting.SettingActivity;

import java.util.ArrayList;

public class CourseActivity extends BaseActivity implements CourseContract.View {

    CourseContract.Presenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        initToolbar();
        initCourseView();
        initFab();

        mPresenter = new CoursePresenter(this);
    }

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.toolbar_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_set) {
                    Intent intent = new Intent(CourseActivity.this, SettingActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    private void initCourseView() {
        CourseView courseView = (CourseView) findViewById(R.id.course_view);
        courseView.setWeekText("一", "二", "三", "四", "五", "六", "日")
                .setMonthTextSize(10)
                .setDividerSize(0)
                .getCourseTableView()
                .setNodeWidth(28);

        courseView.getCourseTableView().setHorizontalDividerMargin(2);

        CourseDbDao dao = CourseDbDao.newInstance();
        ArrayList<Course> courses = dao.getCourses("");
        courseView.setCourseData(courses);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void addCourse(Course course) {

    }
}
