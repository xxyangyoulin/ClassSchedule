package com.mnnyang.gzuclassschedule.mvp.add;

import android.text.TextUtils;

import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;
import com.mnnyang.gzuclassschedule.utils.LogUtil;

/**
 * Created by mnnyang on 17-11-3.
 */

public class AddPresenter implements AddContract.Presenter {
    AddContract.View mView;

    public AddPresenter(AddContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        //do nothing
    }

    @Override
    public void addCourse(Course course) {
        if(mView == null){
            //view被销毁
            return;
        }
        LogUtil.d(this, course.toString());

        if (TextUtils.isEmpty(course.getName())) {
            mView.showAddFail("请填写课程名称");
            return;
        }

        if (0 == course.getWeek()) {
            mView.showAddFail("请选择上课时间");
            return;
        }

        if (0 == course.getStartWeek()) {
            mView.showAddFail("请选择课程开始周");
            return;
        }

        CourseDbDao dao = CourseDbDao.instance();
        Course course1 = dao.addCourse(course);

        if (course1 != null) {
            mView.showAddFail("和课程 【" + course1.getName() + "-星期" + course1.getWeek()
                    + "第" + course1.getNodes().get(0) + "节】 " + "时间冲突");
            return;
        }
        mView.onAddSucceed(course);
    }

    @Override
    public void removeCourse(int courseId) {
        if(mView == null){
            //view被销毁
            return;
        }
        CourseDbDao.instance().removeCourse(courseId);
        mView.onDelSucceed();
    }

    @Override
    public void updateCourse(Course course) {
        if(mView == null){
            //view被销毁
            return;
        }
        LogUtil.d(this, course.toString());

        if (TextUtils.isEmpty(course.getName())) {
            mView.showAddFail("请填写课程名称");
            return;
        }

        if (0 == course.getWeek()) {
            mView.showAddFail("请选择上课时间");
            return;
        }

        if (0 == course.getStartWeek()) {
            mView.showAddFail("请选择课程开始周");
            return;
        }

        CourseDbDao dao = CourseDbDao.instance();
        Course course1 = dao.updateCourse(course);

        if (course1 != null) {
            mView.showAddFail("和课程 【" + course1.getName() + "-星期" + course1.getWeek()
                    + "第" + course1.getNodes().get(0) + "节】 " + "时间冲突");
            return;
        }
        mView.onUpdateSucceed(course);
    }

    @Override
    public void onDestroy() {
        mView = null;
        System.gc();
    }
}
