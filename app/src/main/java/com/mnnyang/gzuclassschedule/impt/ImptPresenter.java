package com.mnnyang.gzuclassschedule.impt;

import com.mnnyang.gzuclassschedule.http.HttpCallback;
import com.mnnyang.gzuclassschedule.http.HttpUtils;

/**
 * Created by mnnyang on 17-10-23.
 */

public class ImptPresenter implements ImptContract.Presenter {
    private ImptContract.View mImptView;
    private ImptModel mModel;

    public ImptPresenter(ImptContract.View imptView) {
        mImptView = imptView;
        mModel = new ImptModel();
    }

    @Override
    public void start() {
        mModel.getCaptcha(mImptView.getCaptchaIV());
    }

    @Override
    public void importCourses(String xh, String pwd, String captcha) {
        mImptView.showImpting();
        HttpUtils.newInstance().impt(xh, pwd, captcha, new HttpCallback<String>() {
            @Override
            public void onSuccess(String s) {
                mImptView.hideImpting();
                mImptView.showSucceed();

            }

            @Override
            public void onFail(Exception e) {
                mImptView.hideImpting();
                mImptView.showFail(e.getMessage());
            }
        });
    }
}
