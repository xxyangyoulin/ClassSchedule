package com.mnnyang.gzuclassschedule.impt;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.app;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.data.bean.CourseTime;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;
import com.mnnyang.gzuclassschedule.http.HttpCallback;
import com.mnnyang.gzuclassschedule.http.HttpUtils;
import com.mnnyang.gzuclassschedule.utils.spec.CourseParse;
import com.mnnyang.gzuclassschedule.utils.LogUtils;
import com.mnnyang.gzuclassschedule.utils.Preferences;

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

    private String xh;
    private String mNormalCourseHtml;
    private String mSelectYear;
    private String mSelectterm;

    public ImptPresenter(ImptContract.View imptView) {
        mImptView = imptView;
        mModel = new ImptModel();
    }

    @Override
    public void start() {
        getCaptcha();
    }


    @Override
    public void importCustomCourses(final String year, final String term) {
        LogUtils.d(this, "importCustomCourses");
        LogUtils.d(this,"sy"+mSelectYear+"st"+mSelectterm+"y"+year+"t"+term);
        if (year.equals(mSelectYear) && term.equals(mSelectterm)) {
            importDefaultCourses(year, term);
            return;
        }

        mImptView.showImpting();
        HttpUtils.newInstance().toImpt(xh, year, term, new HttpCallback<String>() {
            @Override
            public void onSuccess(String s) {
                parseCoursesHtmlToDb(s, year + "-" + term);
            }

            @Override
            public void onFail(String errMsg) {
                mImptView.hideImpting();
                mImptView.showErrToast(errMsg, true);
            }
        });
    }

    @Override
    public void importDefaultCourses(final String year, final String term) {
        LogUtils.d(this, "importCustomCourses");
        mImptView.showImpting();
        parseCoursesHtmlToDb(mNormalCourseHtml, year + "-" + term);
    }

    @Override
    public void loadCourseTimeAndTerm(final String xh, String pwd, String captcha) {
        if (!verify(xh, pwd, captcha)) return;
        mImptView.showImpting();
        HttpUtils.newInstance().login(xh, pwd, captcha, null, null,
                new HttpCallback<String>() {

                    @Override
                    public void onSuccess(String s) {
                        ImptPresenter.this.xh = xh;
                        mNormalCourseHtml = s;
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
        CourseTime ct = CourseParse.parseTime(html);
        LogUtils.e(this,ct.toString());


        if (ct == null || ct.years.size() == 0) {
            mImptView.showErrToast("导入学期失败", true);
            return;
        }
        mSelectYear = ct.selectYear;
        mSelectterm = ct.selectTerm;
        mImptView.showCourseTimeDialog(ct);
    }

    private void parseCoursesHtmlToDb(final String html, final String courseTimeTerm) {
        try {
            Observable.create(new Observable.OnSubscribe<String>() {

                @Override
                public void call(Subscriber<? super String> subscriber) {
                    final ArrayList<Course> courses = CourseParse.parse(html);
                    for (Course c : courses) {
                        c.setCsName(courseTimeTerm);
                        CourseDbDao.newInstance().addCourse(c);
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

                            System.out.println("导入成功:" + courseTimeTerm);
                            Preferences.putString(app.mContext.getString(
                                    R.string.app_preference_current_sd_name), courseTimeTerm);
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

    @Override
    public void getCaptcha() {
        mModel.getCaptcha(mImptView.getCaptchaIV());
    }
}
