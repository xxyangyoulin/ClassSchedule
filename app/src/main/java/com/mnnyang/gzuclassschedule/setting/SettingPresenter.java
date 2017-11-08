package com.mnnyang.gzuclassschedule.setting;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.google.gson.Gson;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.app;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.data.bean.Version;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;
import com.mnnyang.gzuclassschedule.http.HttpCallback;
import com.mnnyang.gzuclassschedule.http.HttpUtils;
import com.mnnyang.gzuclassschedule.utils.ActivityUtil;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.VersionUpdate;

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
                            Preferences.clear(app.mContext.getString(
                                    R.string.app_preference_current_sd_name));
                            mView.showNotice("清除成功");
                        } else {
                            mView.showNotice("清除失败,请重试");
                        }
                    }
                });
    }
}
