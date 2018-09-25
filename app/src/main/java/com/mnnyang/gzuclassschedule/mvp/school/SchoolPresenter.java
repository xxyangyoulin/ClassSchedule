package com.mnnyang.gzuclassschedule.school;

import com.mnnyang.gzuclassschedule.http.HttpCallback;
import com.mnnyang.gzuclassschedule.http.HttpUtils;

/**
 * Created by xxyangyoulin on 2018/4/9.
 */

public class SchoolPresenter implements SchoolContract.Presenter {

    private SchoolContract.View mView;
    private boolean testing;

    public SchoolPresenter(SchoolContract.View view) {
        mView = view;
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

        HttpUtils.newInstance().testUrl(url, new HttpCallback<String>() {
            @Override
            public void onSuccess(String s) {
                testing = false;
                mView.testingUrl(false);
                mView.testUrlSucceed(url);
            }

            @Override
            public void onFail(String errMsg) {
                testing = false;
                mView.testingUrl(false);
                mView.testUrlFailed(url);

            }
        });
    }
}
