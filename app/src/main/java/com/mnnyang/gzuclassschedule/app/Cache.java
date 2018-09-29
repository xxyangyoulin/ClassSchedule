package com.mnnyang.gzuclassschedule.app;

import android.text.TextUtils;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.mnnyang.gzuclassschedule.data.beanv2.UserWrapper;
import com.mnnyang.gzuclassschedule.utils.Preferences;

public class Cache {
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
