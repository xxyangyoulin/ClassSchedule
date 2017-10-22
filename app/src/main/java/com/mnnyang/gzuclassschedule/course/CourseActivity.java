package com.mnnyang.gzuclassschedule.course;

import android.os.Bundle;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;

public class CourseActivity extends BaseActivity implements CourseContract.View {

    CourseContract.Presenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        CoursePresenter persenter = new CoursePresenter(this);
    }

    @Override
    public void setPresenter(CourseContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
