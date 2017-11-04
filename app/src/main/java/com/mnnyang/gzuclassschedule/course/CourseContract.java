package com.mnnyang.gzuclassschedule.course;


import com.mnnyang.gzuclassschedule.BasePresenter;
import com.mnnyang.gzuclassschedule.BaseView;
import com.mnnyang.gzuclassschedule.data.bean.Course;

import java.util.ArrayList;

/**
 * Created by mnnyang on 17-10-3.
 */

public interface CourseContract {
    interface Presenter extends BasePresenter {
        void updateCourseViewData(String courseTime);
    }

    interface View extends BaseView<Presenter> {
        void setCourseData(ArrayList<Course> courses);
    }


}
