package com.mnnyang.gzuclassschedule.setting;


import com.mnnyang.gzuclassschedule.BasePresenter;
import com.mnnyang.gzuclassschedule.BaseView;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.http.HttpCallback;

/**
 * Created by mnnyang on 17-10-3.
 */

public interface SettingContract {
    interface Presenter extends BasePresenter {
        boolean addCourse(Course course);
        boolean updateCourse(int courseId);
        void deleteAllCourse();
        void manageCourse();
        void imptGzuCourse();
    }

    interface View extends BaseView<Presenter> {
        void showNotice(String notice);
        void gotoImptActivity();
        void gotoCourseMgActivity();
        void showDeleting();
        void hideDeleting();
    }
}
