package com.mnnyang.gzuclassschedule.mvp.course;


import android.graphics.Bitmap;

import com.mnnyang.gzuclassschedule.BasePresenter;
import com.mnnyang.gzuclassschedule.BaseView;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.data.beanv2.CourseV2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mnnyang on 17-10-3.
 */

public interface CourseContract {
    interface Presenter extends BasePresenter {
        void loadBackground();
        void updateCourseViewData(long csNameId);
        void deleteCourse(long courseId);
    }

    interface View extends BaseView<Presenter> {
        void initFirstStart();
        void setBackground(Bitmap background);
        //void setCourseData(ArrayList<Course> courses);
        void setCourseData(List<CourseV2> courses);
        void updateCoursePreference();
        void updateOtherPreference();
    }


}
