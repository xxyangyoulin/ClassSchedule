package com.mnnyang.gzuclassschedule.mvp.home;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.mnnyang.gzuclassschedule.BasePresenter;
import com.mnnyang.gzuclassschedule.BaseView;
import com.mnnyang.gzuclassschedule.data.beanv2.DownCourseWrapper;
import com.mnnyang.gzuclassschedule.data.beanv2.UserWrapper;

import java.util.List;

public interface HomeContract {
    interface Presenter extends BasePresenter {
        void loadUserInfo();

        void createQRCode(Resources resources);

        void showCourseGroup();

        void uploadLocalCourse();

        void downCourse();

        void cloudOverWriteLocal(List<DownCourseWrapper.DownCourse> downCourses);
    }

    interface View extends BaseView<HomeContract.Presenter> {
        boolean isActive();

        void showMassage(String msg);

        void showCacheData();

        void showLoading(String msg);

        void stopLoading();

        /**
         * 未登录状态页面
         */
        void noSignInPage();

        /**
         * 登录状态页面
         */
        void signInPage(UserWrapper.User user);

        void pleaseLoginIn();

        void updateShowAvator(@NonNull String email);

        void createQRCodeSucceed(Bitmap bitmap);

        void createQRCodeFailed(String msg);

        void cloudToLocalSucceed();
    }
}
