package com.mnnyang.gzuclassschedule.course;

/**
 * Created by mnnyang on 17-10-19.
 */

public class CoursePresenter implements CourseContract.Presenter {

    private CourseContract.View mCourseView;

    public CoursePresenter(CourseContract.View mCourseView) {
        this.mCourseView = mCourseView;
    }

    @Override
    public void start() {

    }

}
