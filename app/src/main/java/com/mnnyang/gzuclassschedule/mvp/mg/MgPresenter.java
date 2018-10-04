package com.mnnyang.gzuclassschedule.mvp.mg;

import android.text.TextUtils;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Cache;
import com.mnnyang.gzuclassschedule.app.app;
import com.mnnyang.gzuclassschedule.data.beanv2.CourseGroup;
import com.mnnyang.gzuclassschedule.data.greendao.CourseGroupDao;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.Preferences;

import java.util.List;


/**
 * Created by mnnyang on 17-11-4.
 */

public class MgPresenter implements MgContract.Presenter {
    private MgContract.View mView;
    private List<CourseGroup> mCsItems;

    public MgPresenter(MgContract.View view, List<CourseGroup> csItems) {
        mView = view;
        mView.setPresenter(this);

        mCsItems = csItems;
    }

    @Override
    public void start() {
        reloadCsNameList();
    }

    @Override
    public void reloadCsNameList() {
        List<CourseGroup> groups = Cache.instance().getCourseGroupDao().queryBuilder().list();
        if (mView == null) {
            //检查到view已经被销毁
            return;
        }
        mCsItems.clear();
        mCsItems.addAll(groups);
        mView.showList();
    }

    @Override
    public void addCsName(String csName) {
        if (mView == null) {
            //检查到view已经被销毁
            return;
        }
        if (TextUtils.isEmpty(csName)) {
            mView.showNotice(app.mContext.getString(R.string.course_name_can_not_be_empty));
        } else {
            //TODO 检查
            CourseGroupDao groupDao = Cache.instance().getCourseGroupDao();
            CourseGroup group = groupDao.queryBuilder().where(CourseGroupDao.Properties.CgName.eq(csName)).unique();
            if (group != null) {
                //notice conflict
                mView.showNotice(app.mContext.getString(R.string.course_name_is_conflicting));
            } else {
                //add cs_name
                groupDao.insert(new CourseGroup(null, csName, null));
                mView.addCsNameSucceed();
            }
        }
    }

    @Override
    public void editCsName(long id, String newCsName) {
        CourseGroupDao groupDao = Cache.instance().getCourseGroupDao();
        if (mView == null) {
            //检查到view已经被销毁
            return;
        }
        CourseGroup group = groupDao.queryBuilder().where(CourseGroupDao.Properties.CgName.eq(newCsName)).unique();
        if (group != null) {
            //notice conflict
            mView.showNotice(app.mContext.getString(R.string.course_name_is_conflicting));
            return;
        }

        Cache.instance().getCourseGroupDao().update(new CourseGroup(id, newCsName, null));
        mView.editCsNameSucceed();
    }

    @Override
    public void deleteCsName(final long csNameId) {
        CourseGroupDao groupDao = Cache.instance().getCourseGroupDao();
        groupDao.deleteByKey(csNameId);
        if (mView == null) {
            //检查到view已经被销毁
            return;
        }
        mView.deleteFinish();
    }

    @Override
    public void switchCsName(long csNameId) {
        Preferences.putLong(app.mContext.getString(
                R.string.app_preference_current_cs_name_id), csNameId);

        if (mView == null) {
            //检查到view已经被销毁
            return;
        }
        mView.showNotice("切换成功");
        mView.gotoCourseActivity();
    }

    @Override
    public void onDestroy() {
        mView = null;
        System.gc();
    }
}
