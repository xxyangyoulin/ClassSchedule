package com.mnnyang.gzuclassschedule.app;

import android.content.Context;
import android.text.TextUtils;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.mnnyang.gzuclassschedule.data.beanv2.UserWrapper;
import com.mnnyang.gzuclassschedule.data.greendao.CourseGroupDao;
import com.mnnyang.gzuclassschedule.data.greendao.CourseV2Dao;
import com.mnnyang.gzuclassschedule.data.greendao.DaoMaster;
import com.mnnyang.gzuclassschedule.data.greendao.DaoSession;
import com.mnnyang.gzuclassschedule.data.greendao.MyOpenHelper;
import com.mnnyang.gzuclassschedule.utils.Preferences;

public class Cache {
    private Context mContext;
    private CourseGroupDao mCourseGroupDao;
    private CourseV2Dao mCourseV2Dao;

    private String mEmail;
    private UserWrapper.User mUser;
    private ClearableCookieJar cookieJar;

    private Cache() {
    }

    private static final class Holder {
        private static final Cache instance = new Cache();
    }

    public static Cache instance() {
        return Holder.instance;
    }

    public void init(Context context) {
        mContext = context;
        initGreenDao(context);
    }

    private void initGreenDao(Context context) {
        MyOpenHelper devOpenHelper = new MyOpenHelper(
                context, "coursev2.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();

        mCourseGroupDao = daoSession.getCourseGroupDao();
        mCourseV2Dao = daoSession.getCourseV2Dao();
    }

    public Context getContext() {
        return mContext;
    }

    public Cache setContext(Context context) {
        mContext = context;
        return this;
    }

    public CourseGroupDao getCourseGroupDao() {
        return mCourseGroupDao;
    }

    public Cache setCourseGroupDao(CourseGroupDao courseGroupDao) {
        mCourseGroupDao = courseGroupDao;
        return this;
    }

    public CourseV2Dao getCourseV2Dao() {
        return mCourseV2Dao;
    }

    public Cache setCourseV2Dao(CourseV2Dao courseV2Dao) {
        mCourseV2Dao = courseV2Dao;
        return this;
    }

    public UserWrapper.User getUser() {
        return mUser;
    }

    public Cache setUser(UserWrapper.User user) {
        mUser = user;
        setEmail(user.getEmail());
        return this;
    }

    public String getEmail() {
        if (TextUtils.isEmpty(mEmail)) {
            mEmail = Preferences.getString(Constant.PREFERENCE_USER_EMAIL, "");
        }
        return mEmail;
    }

    public Cache setEmail(String email) {
        this.mEmail = email;
        Preferences.putString(Constant.PREFERENCE_USER_EMAIL, email);

        return this;
    }

    public ClearableCookieJar getCookieJar() {
        return cookieJar;
    }

    public Cache setCookieJar(ClearableCookieJar cookieJar) {
        this.cookieJar = cookieJar;
        return this;
    }

    public Cache clearCookie() {
        if (cookieJar != null) {
            cookieJar.clear();
            cookieJar.clearSession();
        }
        return this;
    }
}
