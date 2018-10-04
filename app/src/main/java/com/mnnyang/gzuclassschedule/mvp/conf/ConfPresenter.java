package com.mnnyang.gzuclassschedule.mvp.conf;

/**
 * Created by xxyangyoulin on 2018/3/13.
 */

public class ConfPresenter implements ConfContract.Presenter {
    private ConfContract.View mView;

    public ConfPresenter(ConfContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        //nothing
    }

    @Override
    public void onDestroy() {
        mView = null;
        System.gc();
    }
}
