package com.mnnyang.gzuclassschedule.course;


import com.mnnyang.gzuclassschedule.BasePresenter;
import com.mnnyang.gzuclassschedule.BaseView;
import com.mnnyang.gzuclassschedule.data.bean.Course;

/**
 * Created by mnnyang on 17-10-3.
 */

public interface CourseContract {
    interface Presenter extends BasePresenter {
    }

    interface View extends BaseView<Presenter> {
        void addCourse(Course course);
    }


}
