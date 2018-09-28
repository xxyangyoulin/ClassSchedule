package com.mnnyang.gzuclassschedule.mvp.home;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.mnnyang.gzuclassschedule.BasePresenter;
import com.mnnyang.gzuclassschedule.BaseView;
import com.mnnyang.gzuclassschedule.mvp.mg.MgContract;

public interface HomeContract {
    interface Presenter extends BasePresenter {
        void loadAvator(ImageView iv);
        void createQRCode();

        void showCourseGroup();

        void uploadLocalCourse();

        void cloudOverWriteLocal();
    }

    interface View extends BaseView<HomeContract.Presenter> {
        void showLoading(String msg);

        void stopLoading();

        void createQRCodeSucceed(Bitmap bitmap);

        void createQRCodeFailed(String msg);

        //void showCourseGroup(String[] title,int[] id);
    }
}
