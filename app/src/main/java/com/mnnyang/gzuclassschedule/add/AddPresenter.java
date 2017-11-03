package com.mnnyang.gzuclassschedule.add;

/**
 * Created by mnnyang on 17-11-3.
 */

public class AddPresenter implements AddContract.Presenter {
    AddContract.View mView;

    public AddPresenter(AddContract.View view) {
        mView = view;
    }

    @Override
    public void start() {

    }
}
