package com.mnnyang.gzuclassschedule.mvp.course;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.app;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;
import com.mnnyang.gzuclassschedule.utils.ImageResizer;
import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.ScreenUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by mnnyang on 17-10-19.
 */

public class CoursePresenter implements CourseContract.Presenter {

    private CourseContract.View mView;

    public CoursePresenter(CourseContract.View mCourseView) {
        this.mView = mCourseView;
        mView.setPresenter(this);

    }

    @Override
    public void start() {
        //nothing
    }

    @Override
    public void loadBackground() {
        String path = Preferences.getString(app.mContext.getString(R.string.app_preference_bg_iamge_path), "");
        if (!TextUtils.isEmpty(path)) {
            loadImage(path);
        } else {
            LogUtil.e(this, "no background");
        }
    }

    private void loadImage(final String path) {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                Bitmap bitmap = ImageResizer.decodeSampledBitmapFromFile(path,
                        ScreenUtils.getSWidth(), 0);
                if (bitmap == null) {
                    emitter.onError(new FileNotFoundException());
                } else {
                    emitter.onNext(bitmap);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        mView.setBackground(bitmap);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void updateCourseViewData(final int csNameId) {
        Observable.create(new ObservableOnSubscribe<ArrayList<Course>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<Course>> emitter) throws Exception {
                CourseDbDao dao = CourseDbDao.instance();
                final ArrayList<Course> courses = dao.loadCourses(csNameId);
                emitter.onNext(courses);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Course>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<Course> courses) {
                        if(mView == null){
                            //view被销毁
                            return;
                        }
                        mView.setCourseData(courses);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void deleteCourse(int courseId) {
        CourseDbDao.instance().removeCourse(courseId);
        mView.updateCoursePreference(); //must be main thread
    }

    @Override
    public void onDestroy() {
        mView = null;
        System.gc();
    }
}
