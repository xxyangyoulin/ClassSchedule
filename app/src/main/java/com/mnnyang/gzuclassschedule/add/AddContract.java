package com.mnnyang.gzuclassschedule.add;

import com.mnnyang.gzuclassschedule.BasePresenter;
import com.mnnyang.gzuclassschedule.BaseView;

/**
 * Created by mnnyang on 17-11-3.
 */

public interface AddContract {
    interface Presenter extends BasePresenter {
    }

    interface View extends BaseView<AddContract.Presenter> {

    }
}
