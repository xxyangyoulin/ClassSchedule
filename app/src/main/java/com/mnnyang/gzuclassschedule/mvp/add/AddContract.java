package com.mnnyang.gzuclassschedule.mvp.add;

import com.mnnyang.gzuclassschedule.BasePresenter;
import com.mnnyang.gzuclassschedule.BaseView;
import com.mnnyang.gzuclassschedule.data.beanv2.CourseV2;

/**
 * Created by mnnyang on 17-11-3.
 */

public interface AddContract {
    interface Presenter extends BasePresenter {
        void addCourse(CourseV2 courseV2);
        void removeCourse(long courseId);
        void updateCourse(CourseV2 courseV2);
    }

    interface View extends BaseView<AddContract.Presenter> {
        void showAddFail(String msg);
        void onAddSucceed(CourseV2 courseV2);
        void onDelSucceed();
        void onUpdateSucceed(CourseV2 courseV2);
    }
}
