package com.mnnyang.gzuclassschedule.mvp.about;

import com.mnnyang.gzuclassschedule.BasePresenter;
import com.mnnyang.gzuclassschedule.BaseView;
import com.mnnyang.gzuclassschedule.data.bean.Version;

/**
 * Created by mnnyang on 17-11-3.
 */

public interface AboutContract {
    interface Presenter extends BasePresenter {
        void checkUpdate();
    }

    interface View extends BaseView<AboutContract.Presenter> {
        void showNotice(String notice);
        void showUpdateVersionInfo(Version version);
    }
}
