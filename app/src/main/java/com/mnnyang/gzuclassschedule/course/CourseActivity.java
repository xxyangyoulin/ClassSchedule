package com.mnnyang.gzuclassschedule.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.custom.CourseView;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;
import com.mnnyang.gzuclassschedule.setting.SettingActivity;

public class CourseActivity extends BaseActivity implements CourseContract.View {

    CourseContract.Presenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        initToolbar();
        initCourseView();

        mPresenter = new CoursePresenter(this);
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
        CourseDbDao dao = CourseDbDao.newInstance();
        Course course = new Course().setName("科学与技术1").setClassRoom("博学楼234").setWeek(1).addNode(1).addNode(2);
        courseView.addCourse(course);
        dao.addCourse(course);
        dao.getCourses("null");

        courseView.getCourseTableView().setHorizontalDividerMargin(2);

        courseView.addCourse(new Course().setName("科学与技术2").setClassRoom("博学楼234").setWeek(2).addNode(3).addNode(4));
        courseView.addCourse(new Course().setName("科学与技术65").setClassRoom("博学楼234").setWeek(3).addNode(5).addNode(6));
        courseView.addCourse(new Course().setName("科学与技术4").setClassRoom("博学楼234").setWeek(2).addNode(5).addNode(6));
        courseView.addCourse(new Course().setName("科学与技术7").setClassRoom("博学楼234").setWeek(4).addNode(1).addNode(2));
        courseView.addCourse(new Course().setName("科学与技术8").setClassRoom("博学楼234").setWeek(5).addNode(7).addNode(8));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void addCourse(Course course) {

    }
}
