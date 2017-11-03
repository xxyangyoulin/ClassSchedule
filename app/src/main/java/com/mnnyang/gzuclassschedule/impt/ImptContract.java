package com.mnnyang.gzuclassschedule.impt;


import android.widget.ImageView;

import com.mnnyang.gzuclassschedule.BasePresenter;
import com.mnnyang.gzuclassschedule.BaseView;

import java.util.ArrayList;

/**
 * Created by mnnyang on 17-10-3.
 */

public interface ImptContract {
    interface Presenter extends BasePresenter {
        void importCourses(String xh, String pwd, String captcha,
                           String courseTime, String term);

        void loadCourseTimeAndTerm(String xh, String pwd, String captcha);

        void getCaptcha();
    }

    interface View extends BaseView<Presenter> {
        ImageView getCaptchaIV();

        void showImpting();

        void hideImpting();

        void showErrToast(String errMsg, boolean reLoad);

        void showSucceed();

        void showCourseTimeDialog(ArrayList<String> times);
    }

    interface Model {
        void getCaptcha(ImageView iv);
    }
}
