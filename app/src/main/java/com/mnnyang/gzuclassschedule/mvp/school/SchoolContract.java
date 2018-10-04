package com.mnnyang.gzuclassschedule.mvp.school;


import com.mnnyang.gzuclassschedule.BasePresenter;
import com.mnnyang.gzuclassschedule.BaseView;

/**
 * Created by mnnyang on 17-10-3.
 */

public interface SchoolContract {
    interface Presenter extends BasePresenter {
        void testUrl(String url);
    }

    interface View extends BaseView<Presenter> {
        void showNotice(String notice);

        void showInputDialog();

        void testingUrl(boolean bool);

        void testUrlFailed(String url);

        void testUrlSucceed(String url);
    }
}
