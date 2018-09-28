package com.mnnyang.gzuclassschedule.utils.event;

import com.mnnyang.gzuclassschedule.data.beanv2.UserWrapper;

public class LoginEvent {
    private UserWrapper.User mUser;

    public UserWrapper.User getUser() {
        return mUser;
    }

    public LoginEvent setUser(UserWrapper.User user) {
        mUser = user;
        return this;
    }
}
