package com.mnnyang.gzuclassschedule.mg;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.app;
import com.mnnyang.gzuclassschedule.data.bean.CsItem;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;
import com.mnnyang.gzuclassschedule.utils.Preferences;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mnnyang on 17-11-4.
 */

public class MgPresenter implements MgContract.Presenter {
    MgContract.View mView;
    MgContract.Model mModel;
    ArrayList<CsItem> mCsItems;

    public MgPresenter(MgContract.View view, ArrayList<CsItem> csItems) {
        mView = view;
        mCsItems = csItems;
        mModel = new MgModel();
    }

    @Override
    public void start() {
        Observable.create(new Observable.OnSubscribe<ArrayList<CsItem>>() {
            @Override
            public void call(Subscriber<? super ArrayList<CsItem>> subscriber) {
                ArrayList<CsItem> data = mModel.getCsItemData();
                subscriber.onNext(data);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<CsItem>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ArrayList<CsItem> items) {
                        mCsItems.clear();
                        mCsItems.addAll(items);
                        mView.showList(mCsItems);
                    }
                });
    }

    @Override
    public void deleteCsName(int csNameId) {
        //TODO delete
    }

    @Override
    public void switchCsName(int csNameId) {
        String name = CourseDbDao.newInstance().getCsName(csNameId);
        Preferences.putString(app.mContext.getString(
                R.string.app_preference_current_sd_name), name);
        mView.showNotice("切换成功");
        mView.gotoCourseActivity();
    }
}
