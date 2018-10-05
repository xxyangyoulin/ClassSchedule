package com.mnnyang.gzuclassschedule.mvp.school;

import com.mnnyang.gzuclassschedule.data.http.HttpCallback;
import com.mnnyang.gzuclassschedule.data.http.EduHttpUtils;

/**
 * Created by xxyangyoulin on 2018/4/9.
 */

public class SchoolPresenter implements SchoolContract.Presenter {

    private SchoolContract.View mView;
    private boolean testing;

    public SchoolPresenter(SchoolContract.View view) {
        mView = view;
        mView.setPresenter(this);

    }

    @Override
    public void start() {
        //do nothing
    }


    @Override
    public void testUrl(final String url) {
        if (testing) {
            return;
        }

        testing = true;
        mView.testingUrl(true);

        EduHttpUtils.newInstance().testUrl(url, new HttpCallback<String>() {
            @Override
            public void onSuccess(String s) {
                if (mView == null) {
                    //检查到view已经被销毁
                    return;
                }
                testing = false;
                mView.testingUrl(false);
                mView.testUrlSucceed(url);
            }

            @Override
            public void onFail(String errMsg) {
                if (mView == null) {
                    //检查到view已经被销毁
                    return;
                }
                testing = false;
                mView.testingUrl(false);
                mView.testUrlFailed(url);

            }
        });
    }

    @Override
    public void onDestroy() {
        mView = null;
        System.gc();
    }
}
