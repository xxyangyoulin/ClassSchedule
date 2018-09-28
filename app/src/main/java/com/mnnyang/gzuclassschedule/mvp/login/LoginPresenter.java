package com.mnnyang.gzuclassschedule.mvp.login;

import com.mnnyang.gzuclassschedule.data.beanv2.BaseBean;
import com.mnnyang.gzuclassschedule.data.http.HttpCallback;
import com.mnnyang.gzuclassschedule.data.http.MyHttpUtils;

public class LoginPresenter implements LoginContact.Presenter {

    private LoginContact.View mView;

    public LoginPresenter(LoginContact.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void signIn(String email, String password) {
        mView.showLoading("登录中");
        email = email.trim();
        password = password.trim();
        new MyHttpUtils().login(email, password, new HttpCallback<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                mView.stopLoading();

                if (baseBean != null) {
                    if (baseBean.getCode() == 1) {
                        mView.signInSucceed(baseBean);
                    } else {
                        mView.signInFailed(baseBean.getMsg());
                    }
                }
            }

            @Override
            public void onFail(String errMsg) {
                mView.stopLoading();

            }
        });
    }

    @Override
    public void signUp(String email, String password) {
        mView.showLoading("注册中");

        email = email.trim();
        password = password.trim();
        new MyHttpUtils().register(email, password, new HttpCallback<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                mView.stopLoading();

                if (baseBean != null) {
                    if (baseBean.getCode() == 1) {
                        mView.signUpSucceed(baseBean);
                    } else {
                        mView.signUpFailed(baseBean.getMsg());
                    }
                }
            }

            @Override
            public void onFail(String errMsg) {
                mView.stopLoading();
                mView.signUpFailed(errMsg);
            }
        });
    }

    @Override
    public void start() {

    }
}
