package com.mnnyang.gzuclassschedule.mvp.home;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Cache;
import com.mnnyang.gzuclassschedule.data.beanv2.BaseBean;
import com.mnnyang.gzuclassschedule.data.beanv2.CourseGroup;
import com.mnnyang.gzuclassschedule.data.beanv2.CourseV2;
import com.mnnyang.gzuclassschedule.data.beanv2.DownCourseWrapper;
import com.mnnyang.gzuclassschedule.data.beanv2.UserWrapper;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;
import com.mnnyang.gzuclassschedule.data.greendao.CourseGroupDao;
import com.mnnyang.gzuclassschedule.data.greendao.CourseV2Dao;
import com.mnnyang.gzuclassschedule.data.http.HttpCallback;
import com.mnnyang.gzuclassschedule.data.http.MyHttpUtils;
import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.mnnyang.gzuclassschedule.utils.ScreenUtils;
import com.mnnyang.gzuclassschedule.utils.event.CourseDataChangeEvent;
import com.mnnyang.gzuclassschedule.utils.spec.QRCode;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        loadUserInfo();
    }

    @Override
    public void loadUserInfo() {

        if (TextUtils.isEmpty(Cache.instance().getEmail())) {
            if (mView == null) {
                //检查到view已经被销毁
                return;
            }
            mView.noSignInPage();
            return;
        }

        new MyHttpUtils().userInfo(new HttpCallback<UserWrapper>() {
            @Override
            public void onSuccess(UserWrapper userWrapper) {
                if (mView == null) {
                    //view被销毁
                    return;
                }
                if (userWrapper != null) {
                    if (userWrapper.getCode() == 1) {
                        Cache.instance().setUser(userWrapper.getData());

                        mView.signInPage(userWrapper.getData());
                    } else if (userWrapper.getCode() == 3) {
                        mView.noSignInPage();
                        LogUtil.e(this, userWrapper.toString());
                    }
                }
            }

            @Override
            public void onFail(String errMsg) {
                if (mView == null) {
                    //view被销毁
                    return;
                }
                LogUtil.e(this, errMsg);
            }
        });
    }


    @Override
    public void createQRCode(final Resources resources) {

        mView.showLoading("正在努力生成二维码");

        final String encodeContent = "hello";
        final int width = ScreenUtils.dp2px(150);

        Disposable subscribe = Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                Bitmap bitmap = new QRCode().makeQRCodeImage(encodeContent, width, width, BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher));
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
                        if (mView == null) {
                            //view被销毁
                            return;
                        }
                        mView.stopLoading();
                        mView.createQRCodeSucceed(bitmap);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (mView == null) {
                            //view被销毁
                            return;
                        }
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
        if (TextUtils.isEmpty(Cache.instance().getEmail())) {
            mView.pleaseLoginIn();
            return;
        }

        mView.showLoading("同步中");

        JSONObject result = buildJsonOfAllCourse();
        MyHttpUtils utils = new MyHttpUtils();
        System.out.println("-------" + result.toString());

        utils.uploadCourse(result.toString(), new HttpCallback<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (mView == null) {
                    //view被销毁
                    return;
                }
                mView.stopLoading();

                if (baseBean != null) {
                    if (baseBean.getCode() == 1) {
                        mView.showMassage("同步成功");

                    } else {
                        mView.showMassage("同步失败：" + baseBean.getMsg());
                    }
                } else {
                    mView.showMassage("同步失败");
                }
            }

            @Override
            public void onFail(String errMsg) {
                if (mView == null) {
                    //view被销毁
                    return;
                }
                mView.stopLoading();
                mView.showMassage(errMsg);
            }
        });
    }

    @NonNull
    private JSONObject buildJsonOfAllCourse() {
        List<CourseGroup> groups = Cache.instance().getCourseGroupDao()
                .queryBuilder().list();
        CourseV2Dao courseV2Dao = Cache.instance().getCourseV2Dao();

        JSONObject result = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            result.put("data", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (CourseGroup group : groups) {
            List<CourseV2> courseV2s = courseV2Dao.queryBuilder()
                    .where(CourseV2Dao.Properties.CouCgId.eq(group.getCgId()))
                    .list();

            for (CourseV2 course : courseV2s) {
                try {
                    JSONObject jsonItem = new JSONObject();
                    jsonItem.put("id", course.getCouId());
                    jsonItem.put("name", course.getCouName());
                    jsonItem.put("location", course.getCouLocation() == null ? "" : course.getCouLocation());
                    jsonItem.put("week", course.getCouWeek());
                    jsonItem.put("teacher", course.getCouTeacher() == null ? "" : course.getCouTeacher());
                    jsonItem.put("all_week", course.getCouAllWeek());
                    jsonItem.put("start_node", course.getCouStartNode());
                    jsonItem.put("node_count", course.getCouNodeCount());
                    jsonItem.put("color", course.getCouColor() == null ? "-1" : course.getCouColor());
                    jsonItem.put("group_name", group.getCgName());
                    jsonItem.put("only_id", course.getCouOnlyId());

                    jsonArray.put(jsonItem);
                } catch (JSONException e) {
                    LogUtil.e(this, "buildJsonOfAllCourse() failed--->" + course.toString());
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    @Override
    public void downCourse() {
        if (TextUtils.isEmpty(Cache.instance().getEmail())) {
            mView.pleaseLoginIn();
            return;
        }
        mView.showLoading("同步中");
        new MyHttpUtils().downCourse(new HttpCallback<DownCourseWrapper>() {
            @Override
            public void onSuccess(DownCourseWrapper bean) {
                if (mView == null) {
                    //view被销毁
                    return;
                }
                mView.stopLoading();
                if (bean != null) {
                    if (bean.getCode() == 1) {
                        //mView.showMassage("同步成功");
                        cloudOverWriteLocal(bean.getData());
                    } else {
                        mView.showMassage("同步失败：" + bean.getMsg());
                    }
                } else {
                    mView.showMassage("同步失败");
                }
            }

            @Override
            public void onFail(String errMsg) {
                if (mView == null) {
                    //view被销毁
                    return;
                }
                mView.stopLoading();
                mView.showMassage(errMsg);
            }
        });
    }

    private Map<String, Long> mCacheGroup;

    @Override
    public void cloudOverWriteLocal(List<DownCourseWrapper.DownCourse> downCourses) {
        mCacheGroup = new HashMap<>();

        CourseV2Dao courseDao = Cache.instance().getCourseV2Dao();
        if (downCourses != null) {
            for (DownCourseWrapper.DownCourse downCourse : downCourses) {
                Long groupId = getGroupId(downCourse);
                if (groupId != null) {
                    CourseV2 oldCourse = courseDao.queryBuilder()
                            .where(CourseV2Dao.Properties.CouOnlyId.eq(downCourse.getOnly_id()))
                            .unique();
                    if (oldCourse != null) {
                        // 移动端存在旧的数据 解决方法是使用云数据覆盖本地
                        updateCourse(courseDao, downCourse, oldCourse);
                    } else {
                        // 不存在则添加
                        newCourse(courseDao, downCourse, groupId);
                    }
                }
            }
        }

        if (mView == null) {// mView被销毁
            return;
        }
        mView.stopLoading();
        EventBus.getDefault().post(new CourseDataChangeEvent());
        mView.cloudToLocalSucceed();
    }

    private void newCourse(CourseV2Dao courseDao, DownCourseWrapper.DownCourse downCourse, Long groupId) {
        CourseV2 oldCourse;
        oldCourse = new CourseV2()
                .setCouOnlyId(downCourse.getOnly_id())
                .setCouCgId(groupId)
                .setCouName(downCourse.getName())
                .setCouTeacher(downCourse.getTeacher())
                .setCouLocation(downCourse.getLocation())
                .setCouColor(downCourse.getColor())
                .setCouWeek(downCourse.getWeek())
                .setCouStartNode(downCourse.getStart_node())
                .setCouNodeCount(downCourse.getNode_count())
                .setCouAllWeek(downCourse.getAll_week());
        courseDao.insert(oldCourse);
    }

    private void updateCourse(CourseV2Dao courseDao, DownCourseWrapper.DownCourse downCourse, CourseV2 oldCourse) {
        oldCourse.setCouName(downCourse.getName())
                .setCouTeacher(downCourse.getTeacher())
                .setCouLocation(downCourse.getLocation())
                .setCouColor(downCourse.getColor())
                .setCouWeek(downCourse.getWeek())
                .setCouStartNode(downCourse.getStart_node())
                .setCouNodeCount(downCourse.getNode_count())
                .setCouAllWeek(downCourse.getAll_week());
        courseDao.update(oldCourse);
    }

    private Long getGroupId(DownCourseWrapper.DownCourse downCourse) {
        if (downCourse == null) {
            return null;
        }

        CourseGroupDao groupDao = Cache.instance().getCourseGroupDao();

        String groupName = downCourse.getGroup_name();
        if (!TextUtils.isEmpty(groupName)) {
            Long groupId = mCacheGroup.get(groupName);
            if (null == groupId) {
                CourseGroup dbGroup = groupDao.queryBuilder()
                        .where(CourseGroupDao.Properties.CgName.eq(groupName))
                        .unique();
                if (dbGroup == null) {
                    CourseGroup newGroup = new CourseGroup().setCgName(groupName);
                    groupId = groupDao.insert(newGroup);
                } else {
                    groupId = dbGroup.getCgId();
                }
                mCacheGroup.put(groupName, groupId);
            }
            return groupId;
        } else {
            LogUtil.e(this, "下载的数据未找到group_name");
            return null;
        }
    }

    @Override
    public void onDestroy() {
        mView = null;
        System.gc();
    }
}
