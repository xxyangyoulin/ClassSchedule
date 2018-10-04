package com.mnnyang.gzuclassschedule.mvp.login;

import com.mnnyang.gzuclassschedule.BasePresenter;
import com.mnnyang.gzuclassschedule.BaseView;
import com.mnnyang.gzuclassschedule.data.beanv2.BaseBean;

public interface SignContact {
    interface Presenter extends BasePresenter {
        void signIn(String email, String password);

        void signUp(String email, String password);
    }

    interface View extends BaseView<Presenter> {
        void showMassage(String msg);

        void showLoading(String msg);

        void stopLoading();

        void signInSucceed(BaseBean bean);

        void signInFailed(String msg);

        void signUpSucceed(BaseBean bean);

        void signUpFailed(String msg);

    }
}
