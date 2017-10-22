package com.mnnyang.gzuclassschedule.course;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.custom.CourseView;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.utils.ScreenUtils;

public class CourseActivity extends BaseActivity implements CourseContract.View {

    CourseContract.Presenter mPresenter;
    private NavigationView mNavNiew;
    private DrawerLayout mDrawerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.setSystemBarTransparent(this);
        setContentView(R.layout.activity_course);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavNiew = (NavigationView) findViewById(R.id.nav_view);
        initToolbar();
        initCourseView();
        initFab();

        CoursePresenter persenter = new CoursePresenter(this);
    }

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }


    private void initCourseView() {
        CourseView courseView = (CourseView) findViewById(R.id.course_view);
        courseView.setWeekText("一", "二", "三", "四", "五", "六", "日")
                .setMonthTextSize(10)
                .setDividerSize(0)
                .getCourseTableView()
                .setNodeWidth(28);
        courseView.addCourse(new Course().setName("科学与技术1").setClassRoom("博学楼234").setWeek(1).addNode(1).addNode(2));
        courseView.addCourse(new Course().setName("科学与技术2").setClassRoom("博学楼234").setWeek(2).addNode(3).addNode(4));
        courseView.addCourse(new Course().setName("科学与技术65").setClassRoom("博学楼234").setWeek(3).addNode(5).addNode(6));
        courseView.addCourse(new Course().setName("科学与技术4").setClassRoom("博学楼234").setWeek(2).addNode(5).addNode(6));
        courseView.addCourse(new Course().setName("科学与技术7").setClassRoom("博学楼234").setWeek(4).addNode(1).addNode(2));
        courseView.addCourse(new Course().setName("科学与技术8").setClassRoom("博学楼234").setWeek(5).addNode(7).addNode(8));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(mNavNiew);
                break;
        }
        return true;
    }

    @Override
    public void setPresenter(CourseContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void addCourse(Course course) {

    }
}
