package com.mnnyang.gzuclassschedule.mvp.home;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.mnnyang.gzuclassschedule.BasePresenter;
import com.mnnyang.gzuclassschedule.BaseView;
import com.mnnyang.gzuclassschedule.data.beanv2.UserWrapper;
import com.mnnyang.gzuclassschedule.mvp.mg.MgContract;

public interface HomeContract {
    interface Presenter extends BasePresenter {
        void loadAvator(ImageView iv);

        void loadUserInfo();

        void createQRCode();

        void showCourseGroup();

        void uploadLocalCourse();

        void cloudOverWriteLocal();
    }

    interface View extends BaseView<HomeContract.Presenter> {

        void showCacheData();

        void showLoading(String msg);

        void stopLoading();

        void noSignInPage();

        void signedInPage();

        void userInfoSucceed(UserWrapper.User user);

        void updateShowAvator();

        void createQRCodeSucceed(Bitmap bitmap);

        void createQRCodeFailed(String msg);

        //void showCourseGroup(String[] title,int[] id);
    }
}
