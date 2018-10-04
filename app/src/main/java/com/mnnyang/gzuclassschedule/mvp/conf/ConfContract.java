package com.mnnyang.gzuclassschedule.mvp.conf;

import com.mnnyang.gzuclassschedule.BasePresenter;
import com.mnnyang.gzuclassschedule.BaseView;

/**
 * Created by mnnyang on 17-11-3.
 */

public interface ConfContract {
    interface Presenter extends BasePresenter {
    }

    interface View extends BaseView<ConfContract.Presenter> {
        void confBgImage();
    }
}
