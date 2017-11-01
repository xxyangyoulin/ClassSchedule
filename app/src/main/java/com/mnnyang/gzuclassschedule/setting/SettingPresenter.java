package com.mnnyang.gzuclassschedule.setting;

import com.mnnyang.gzuclassschedule.data.bean.Course;

/**
 * Created by mnnyang on 17-10-19.
 */

public class SettingPresenter implements SettingContract.Presenter {

    private SettingContract.View mView;

    public SettingPresenter(SettingContract.View view) {
        this.mView = view;
    }

    @Override
    public void start() {
        //nothing to do
    }

    @Override
    public boolean addCourse(Course course) {
        return false;
    }

    @Override
    public boolean updateCourse(int courseId) {
        return false;
    }

    @Override
    public boolean deleteAllCourse() {
        return false;
    }

    @Override
    public void imptGzuCourse() {
        mView.gotoImptActivity();
    }
}
