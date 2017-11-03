package com.mnnyang.gzuclassschedule.impt;

import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;
import com.mnnyang.gzuclassschedule.http.HttpCallback;
import com.mnnyang.gzuclassschedule.http.HttpUtils;
import com.mnnyang.gzuclassschedule.utils.CourseParse;
import com.mnnyang.gzuclassschedule.utils.LogUtils;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mnnyang on 17-10-23.
 */

public class ImptPresenter implements ImptContract.Presenter {
    private ImptContract.View mImptView;
    private ImptModel mModel;

    public ImptPresenter(ImptContract.View imptView) {
        mImptView = imptView;
        mModel = new ImptModel();
    }

    @Override
    public void start() {
        getCaptcha();
    }

    @Override
    public void importCourses(String xh, String pwd, String captcha,
                              String courseTime, String term) {

        if (!verify(xh, pwd, captcha)) return;

        mImptView.showImpting();
        HttpUtils.newInstance().login(xh, pwd, captcha, courseTime, term,
                new HttpCallback<String>() {

                    @Override
                    public void onSuccess(String s) {
                        LogUtils.i(this, "课表\n" + s);
                        parseCoursesHtmlToDb(s);
                    }

                    @Override
                    public void onFail(String errMsg) {
                        mImptView.hideImpting();
                        mImptView.showErrToast(errMsg, true);
                    }
                });
    }

    @Override
    public void loadCourseTimeAndTerm(String xh, String pwd, String captcha) {
        if (!verify(xh, pwd, captcha)) return;
        mImptView.showImpting();
        HttpUtils.newInstance().login(xh, pwd, captcha, null, null,
                new HttpCallback<String>() {

                    @Override
                    public void onSuccess(String s) {
                        LogUtils.i(this, "课表time:\n" + s);
                        mImptView.hideImpting();
                        parseTimeTermHtmlToShow(s);
                    }

                    @Override
                    public void onFail(String errMsg) {
                        mImptView.hideImpting();
                        mImptView.showErrToast(errMsg, true);
                    }
                });

    }

    private void parseTimeTermHtmlToShow(String html) {
        ArrayList<String> times = CourseParse.parseTime(html);
        mImptView.showCourseTimeDialog(times);
    }

    private void parseCoursesHtmlToDb(final String html) {
        try {
            //TODO rxjava
            Observable.create(new Observable.OnSubscribe<String>() {

                @Override
                public void call(Subscriber<? super String> subscriber) {
                    final ArrayList<Course> courses = CourseParse.parse(html);
                    //TODO 删除
                    demo(courses);
                    for (Course cours : courses) {
                        CourseDbDao.newInstance().addCourse(cours);
                    }
                    subscriber.onNext("导入成功");
                    subscriber.onCompleted();

                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {

                        @Override
                        public void onCompleted() {
                            LogUtils.d(this, "完成");
                        }

                        @Override
                        public void onError(Throwable e) {
                            mImptView.hideImpting();
                            mImptView.showErrToast("插入数据库失败", true);
                        }

                        @Override
                        public void onNext(String s) {
                            mImptView.hideImpting();
                            mImptView.showSucceed();
                        }
                    });


        } catch (Exception e) {
            e.printStackTrace();
            mImptView.hideImpting();
            mImptView.showErrToast("导入错误", true);
        }
    }

    private boolean verify(String xh, String pwd, String captcha) {
        if (xh.isEmpty()) {
            mImptView.showErrToast("请填写学号", false);
            return false;
        }

        if (pwd.isEmpty()) {
            mImptView.showErrToast("请填写密码", false);
            return false;
        }

        if (captcha.isEmpty()) {
            mImptView.showErrToast("请填写验证码", false);
            return false;
        }
        return true;
    }

    private void demo(ArrayList<Course> courses) {
        for (Course cours : courses) {
            cours.setCourseTime("2017-2018");
        }
    }

    @Override
    public void getCaptcha() {
        mModel.getCaptcha(mImptView.getCaptchaIV());
    }
}
