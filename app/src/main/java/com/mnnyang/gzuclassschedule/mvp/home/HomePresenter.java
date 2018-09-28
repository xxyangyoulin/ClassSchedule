package com.mnnyang.gzuclassschedule.mvp.home;


import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.mnnyang.gzuclassschedule.app.AppUtils;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.data.bean.CsItem;
import com.mnnyang.gzuclassschedule.data.bean.CsName;
import com.mnnyang.gzuclassschedule.data.beanv2.BaseBean;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;
import com.mnnyang.gzuclassschedule.data.http.HttpCallback;
import com.mnnyang.gzuclassschedule.data.http.MyHttpUtils;
import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.mnnyang.gzuclassschedule.utils.ScreenUtils;
import com.mnnyang.gzuclassschedule.utils.spec.QRCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View mView;

    public HomePresenter(HomeContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
    }


    @Override
    public void loadAvator(ImageView iv) {

    }

    public static String getGravatar(String email) {
        String emailMd5 = AppUtils.md5Hex(email);        //设置图片大小32px
        String avatar = "http://www.gravatar.com/avatar/" + emailMd5 + "?s=64";
        System.out.println(avatar);
        return avatar;
    }


    @Override
    public void createQRCode() {

        mView.showLoading("正在努力生成二维码");

        final String encodeContent = "hello";
        final int width = ScreenUtils.dp2px(150);

        Disposable subscribe = Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                Bitmap bitmap = new QRCode().makeQRCodeImage(encodeContent, width, width, null);
                if (bitmap != null) {
                    emitter.onNext(bitmap);
                    emitter.onComplete();
                } else {
                    emitter.onError(new Exception("生成二维码失败"));
                }
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        mView.stopLoading();
                        mView.createQRCodeSucceed(bitmap);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.stopLoading();
                        mView.createQRCodeFailed(throwable.getMessage());
                    }
                });
    }

    @Override
    public void showCourseGroup() {

    }

    @Override
    public void uploadLocalCourse() {
        JSONObject result = buildJsonOfAllCourse();
        System.out.println(result.toString());
        MyHttpUtils utils = new MyHttpUtils();
        utils.uploadCourse(result.toString(), new HttpCallback<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {

            }

            @Override
            public void onFail(String errMsg) {

            }
        });
    }

    @NonNull
    private JSONObject buildJsonOfAllCourse() {
        ArrayList<CsItem> csItems = CourseDbDao.instance().loadCsNameList();

        JSONObject result = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            result.put("data", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (CsItem csItem : csItems) {
            CsName csName = csItem.getCsName();
            ArrayList<Course> courses = CourseDbDao.instance().loadCourses(csName.getName());
            for (Course course : courses) {
                try {
                    JSONObject jsonItem = new JSONObject();
                    jsonItem.put("name", course.getName());
                    jsonItem.put("location", course.getClassRoom());
                    jsonItem.put("week", course.getWeek());
                    jsonItem.put("teacher", course.getTeacher());

                    List<Integer> nodes = course.getNodes();
                    if (!nodes.isEmpty()) {
                        jsonItem.put("start_node", nodes.get(0));
                        jsonItem.put("node_count", nodes.size());
                    }

                    jsonItem.put("start_week", course.getStartWeek());
                    jsonItem.put("end_week", course.getEndWeek());
                    jsonItem.put("week_type", course.getWeekType());
                    jsonItem.put("cs_name", course.getCsName());
                    jsonItem.put("color", course.getColor());

                    jsonArray.put(jsonItem);
                } catch (JSONException e) {
                    LogUtil.e(this, "buildJsonOfAllCourse() failed--->" + course.toSelfString());
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public void cloudOverWriteLocal() {

    }
}
