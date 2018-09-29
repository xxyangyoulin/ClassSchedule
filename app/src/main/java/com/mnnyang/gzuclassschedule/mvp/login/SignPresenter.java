package com.mnnyang.gzuclassschedule.mvp.login;

import android.text.TextUtils;

import com.mnnyang.gzuclassschedule.app.AppUtils;
import com.mnnyang.gzuclassschedule.data.beanv2.BaseBean;
import com.mnnyang.gzuclassschedule.data.http.HttpCallback;
import com.mnnyang.gzuclassschedule.data.http.MyHttpUtils;

public class SignPresenter implements SignContact.Presenter {

    private SignContact.View mView;

    public SignPresenter(SignContact.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void signIn(String email, String password) {
        if (mView == null) {
            //检查到view已经被销毁
            return;
        }
        if (isOkOfVerifyInput(email, password)) {
            return;
        }

        mView.showLoading("登录中");
        email = email.trim();
        password = password.trim();
        new MyHttpUtils().login(email, password, new HttpCallback<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (mView == null) {
                    //检查到view已经被销毁
                    return;
                }
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
                if (mView == null) {
                    //检查到view已经被销毁
                    return;
                }
                mView.stopLoading();
            }
        });
    }


    @Override
    public void signUp(String email, String password) {
        if (mView == null) {
            //检查到view已经被销毁
            return;
        }
        if (isOkOfVerifyInput(email, password)) {
            return;
        }

        mView.showLoading("注册中");

        email = email.trim();
        password = password.trim();
        new MyHttpUtils().register(email, password, new HttpCallback<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (mView == null) {
                    //检查到view已经被销毁
                    return;
                }
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
                if (mView == null) {
                    //检查到view已经被销毁
                    return;
                }
                mView.stopLoading();
                mView.signUpFailed(errMsg);
            }
        });
    }

    private boolean isOkOfVerifyInput(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            mView.showMassage("邮箱不能为空＞﹏＜");
            return true;
        }

        if (TextUtils.isEmpty(password)) {
            mView.showMassage("密码不能为空＞﹏＜");
            return true;
        }

        if (!AppUtils.isEmail(email)) {
            mView.showMassage("邮箱格式有误＞﹏＜");
            return true;
        }

        if (password.length() < 6) {
            mView.showMassage("密码长度需大于6位＞﹏＜");
            return true;
        }

        return false;
    }

    @Override
    public void onDestroy() {
        mView = null;
        System.gc();
    }
}
