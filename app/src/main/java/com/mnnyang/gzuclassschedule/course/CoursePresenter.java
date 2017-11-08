package com.mnnyang.gzuclassschedule.course;

import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    @Override
    public void updateCourseViewData(final String courseTime) {
        Observable.create(new Observable.OnSubscribe<ArrayList<Course>>() {
            @Override
            public void call(Subscriber<? super ArrayList<Course>> subscriber) {
                CourseDbDao dao = CourseDbDao.newInstance();
                final ArrayList<Course> courses = dao.loadCourses(courseTime);
                subscriber.onNext(courses);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Course>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ArrayList<Course> courses) {
                        mCourseView.setCourseData(courses);
                    }
                });
    }
}
