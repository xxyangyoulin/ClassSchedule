package com.mnnyang.gzuclassschedule.impt;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.custom.CourseTableView;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;
import com.mnnyang.gzuclassschedule.http.HttpCallback;
import com.mnnyang.gzuclassschedule.http.HttpUtils;
import com.mnnyang.gzuclassschedule.utils.CourseParse;
import com.mnnyang.gzuclassschedule.utils.LogUtils;

import java.util.ArrayList;

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
    public void importCourses(String xh, String pwd, String captcha) {
        mImptView.showImpting();
        HttpUtils.newInstance().impt(xh, pwd, captcha, new HttpCallback<String>() {
            @Override
            public void onSuccess(String s) {
                LogUtils.i(this, "课表\n" + s);
                parseHtml(s);
            }

            @Override
            public void onFail(String errMsg) {
                mImptView.hideImpting();
                mImptView.showFail(errMsg);
            }

        });
    }

    private void parseHtml(String html) {
        try {
            ArrayList<Course> courses = CourseParse.parse(html);

            demo(courses);

            new Thread(new dbRun(courses)).start();
            mImptView.showSucceed();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mImptView.hideImpting();
        }
    }

    private void demo(ArrayList<Course> courses) {
        for (Course cours : courses) {
            cours.setCourseTime("2017-2018");
        }
    }

    static class dbRun implements Runnable {
        ArrayList<Course> mCourses;

        public dbRun(ArrayList<Course> courses) {
            mCourses = courses;
        }

        @Override
        public void run() {
            if (mCourses == null) {
                LogUtils.e(this, "data is null");
                return;
            }
            for (Course cours : mCourses) {
                CourseDbDao.newInstance().addCourse(cours);
            }
        }
    }

    @Override
    public void getCaptcha() {
        mModel.getCaptcha(mImptView.getCaptchaIV());
    }
}
