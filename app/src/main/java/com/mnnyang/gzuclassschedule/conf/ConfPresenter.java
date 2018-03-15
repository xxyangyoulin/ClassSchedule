package com.mnnyang.gzuclassschedule.conf;

/**
 * Created by xxyangyoulin on 2018/3/13.
 */

public class ConfPresenter implements ConfContract.Presenter {
    private ConfContract.View mView;

    public ConfPresenter(ConfContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void start() {
        //nothing
    }

}
