package com.mnnyang.gzuclassschedule.setting;

import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;
import com.mnnyang.gzuclassschedule.http.HttpUtils;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mnnyang on 17-10-19.
 */

public class SettingPresenter implements SettingContract.Presenter {

    private SettingContract.View mView;

    public SettingPresenter(SettingContract.View view) {
        this.mView = view;
    }

    @Override
    public void start() {
        //nothing to do
    }

    @Override
    public boolean addCourse(Course course) {
        return false;
    }

    @Override
    public boolean updateCourse(int courseId) {
        return false;
    }

    @Override
    public void deleteAllCourse() {
        mView.showDeleting();
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean b = CourseDbDao.newInstance().removeAllData();
                subscriber.onNext(b);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        mView.hideDeleting();
                        if (aBoolean) {
                            mView.showNotice("清除成功");
                        } else {
                            mView.showNotice("清除失败,请重试");
                        }
                    }
                });
    }

    @Override
    public void manageCourse() {
        mView.gotoCourseMgActivity();
    }

    @Override
    public void imptGzuCourse() {
        mView.gotoImptActivity();
    }
}
