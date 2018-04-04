package com.mnnyang.gzuclassschedule.course;


import android.graphics.Bitmap;

import com.mnnyang.gzuclassschedule.BasePresenter;
import com.mnnyang.gzuclassschedule.BaseView;
import com.mnnyang.gzuclassschedule.data.bean.Course;

import java.util.ArrayList;

/**
 * Created by mnnyang on 17-10-3.
 */

public interface CourseContract {
    interface Presenter extends BasePresenter {
        void loadBackground();
        void updateCourseViewData(int csNameId);
        void deleteCourse(int courseId);
    }

    interface View extends BaseView<Presenter> {
        void initFirstEnter();
        void setBackground(Bitmap background);
        void setCourseData(ArrayList<Course> courses);
        void updateCoursePreference();
        void updateOtherPreference();
    }


}
