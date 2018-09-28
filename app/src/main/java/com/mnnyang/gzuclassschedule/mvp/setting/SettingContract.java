package com.mnnyang.gzuclassschedule.mvp.setting;


import com.mnnyang.gzuclassschedule.BasePresenter;
import com.mnnyang.gzuclassschedule.BaseView;

/**
 * Created by mnnyang on 17-10-3.
 */

public interface SettingContract {
    interface Presenter extends BasePresenter {
        void feedback();
    }

    interface View extends BaseView<Presenter> {
        void showNotice(String notice);
    }
}
