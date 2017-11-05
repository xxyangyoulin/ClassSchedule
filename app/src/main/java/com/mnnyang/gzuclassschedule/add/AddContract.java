package com.mnnyang.gzuclassschedule.add;

import com.mnnyang.gzuclassschedule.BasePresenter;
import com.mnnyang.gzuclassschedule.BaseView;
import com.mnnyang.gzuclassschedule.data.bean.Course;

/**
 * Created by mnnyang on 17-11-3.
 */

public interface AddContract {
    interface Presenter extends BasePresenter {
        void addCourse(Course course);
    }

    interface View extends BaseView<AddContract.Presenter> {
        void showAddFail(String msg);
        void onAddSucceed(Course course);
    }
}
