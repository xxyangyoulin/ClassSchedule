package com.mnnyang.gzuclassschedule.mvp.impt;

import android.graphics.Bitmap;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.app;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.data.bean.CourseTime;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;
import com.mnnyang.gzuclassschedule.data.http.HttpCallback;
import com.mnnyang.gzuclassschedule.data.http.HttpUtils;
import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;
import com.mnnyang.gzuclassschedule.utils.spec.ParseCourse;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by mnnyang on 17-10-23.
 */

public class ImptPresenter implements ImptContract.Presenter {

    private ImptContract.View mImptView;
    private ImptContract.Model mModel;
    private String mSchoolUrl;

    private String xh;
    private String mNormalCourseHtml;
    private String mSelectYear;
    private String mSelectTerm;

    public ImptPresenter(ImptContract.View imptView, String schoolUrl) {
        mImptView = imptView;
        mImptView.setPresenter(this);

        mSchoolUrl = schoolUrl;
        mModel = new ImptModel();
    }

    @Override
    public void start() {
        getCaptcha();
    }


    @Override
    public void importCustomCourses(final String year, final String term) {
        LogUtil.d(this, "importCustomCourses");
        LogUtil.d(this, "sy" + mSelectYear + "st" + mSelectTerm + "y" + year + "t" + term);
        if (year.equals(mSelectYear) && term.equals(mSelectTerm)) {
            importDefaultCourses(year, term);
            return;
        }

        mImptView.showImpting();
        HttpUtils.newInstance().toImpt(mSchoolUrl, xh, year, term, new HttpCallback<String>() {
            @Override
            public void onSuccess(String s) {
                if(mImptView == null){
                    //view被销毁
                    return;
                }
                parseCoursesHtmlToDb(s, year + "-" + term);
            }

            @Override
            public void onFail(String errMsg) {
                if(mImptView == null){
                    //view被销毁
                    return;
                }
                mImptView.hideImpting();
                mImptView.showErrToast(errMsg, true);
            }
        });
    }

    @Override
    public void importDefaultCourses(final String year, final String term) {
        LogUtil.d(this, "importCustomCourses");
        mImptView.showImpting();
        parseCoursesHtmlToDb(mNormalCourseHtml, year + "-" + term);
    }

    @Override
    public void loadCourseTimeAndTerm(final String xh, String pwd, String captcha) {
        if (!verify(xh, pwd, captcha)) return;

        mImptView.showImpting();
        HttpUtils.newInstance().login(mSchoolUrl, xh, pwd, captcha, null, null,
                new HttpCallback<String>() {

                    @Override
                    public void onSuccess(String s) {
                        if(mImptView == null){
                            //view被销毁
                            return;
                        }
                        ImptPresenter.this.xh = xh;
                        mNormalCourseHtml = s;
                        mImptView.hideImpting();
                        parseTimeTermHtmlToShow(s);
                    }

                    @Override
                    public void onFail(String errMsg) {
                        if(mImptView == null){
                            //view被销毁
                            return;
                        }
                        mImptView.hideImpting();
                        mImptView.showErrToast(errMsg, true);
                    }
                });

    }

    private void parseTimeTermHtmlToShow(String html) {
        CourseTime ct = ParseCourse.parseTime(html);

        if (ct == null || ct.years.size() == 0) {
            mImptView.showErrToast("导入学期失败", true);
            return;
        }
        mSelectYear = ct.selectYear;
        mSelectTerm = ct.selectTerm;
        mImptView.showCourseTimeDialog(ct);
    }

    private void parseCoursesHtmlToDb(final String html, final String courseTimeTerm) {
        try {
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    final ArrayList<Course> courses = ParseCourse.parse(html);

                    //删除旧数据
                    CourseDbDao.instance().removeByCsName(courseTimeTerm);

                    //添加新数据
                    for (Course c : courses) {
                        c.setCsName(courseTimeTerm);
                        CourseDbDao.instance().addCourse(c);
                    }

                    emitter.onNext("导入成功");
                    emitter.onComplete();

                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(String s) {
                            if(mImptView == null){
                                //view被销毁
                                return;
                            }
                            LogUtil.i(this, "导入成功:" + courseTimeTerm);

                            Preferences.putInt(app.mContext.getString(
                                    R.string.app_preference_current_cs_name_id),
                                    CourseDbDao.instance().getCsNameId(courseTimeTerm));

                            mImptView.hideImpting();
                            mImptView.showSucceed();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if(mImptView == null){
                                //view被销毁
                                return;
                            }
                            mImptView.hideImpting();
                            mImptView.showErrToast("插入数据库失败", true);
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            mImptView.hideImpting();
            mImptView.showErrToast("导入错误", true);
        }
    }


    private boolean verify(String xh, String pwd, String captcha) {
        if(mImptView == null){
            //view被销毁
            return false;
        }
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


    private boolean captchaIsLoading = false;

    @Override
    public void getCaptcha() {
        //防止重复点击加载验证码按钮导致多次执行
        if (captchaIsLoading) {
            return;
        }

        captchaIsLoading = true;
        mImptView.captchaIsLoading(true);

        HttpUtils.newInstance().loadCaptcha(app.mContext.getCacheDir(), mSchoolUrl,
                new HttpCallback<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        if(mImptView == null){
                            //view被销毁
                            return;
                        }
                        mImptView.getCaptchaIV().setImageBitmap(bitmap);
                        captchaIsLoading = false;
                        mImptView.captchaIsLoading(false);
                    }

                    @Override
                    public void onFail(String errMsg) {
                        if(mImptView == null){
                            //view被销毁
                            return;
                        }
                        ToastUtils.show(errMsg);
                        mImptView.getCaptchaIV().setImageResource(R.drawable.ic_svg_refresh);
                        captchaIsLoading = false;
                        mImptView.captchaIsLoading(false);
                    }
                });
    }

    @Override
    public void onDestroy() {
        mImptView = null;
        System.gc();
    }
}
