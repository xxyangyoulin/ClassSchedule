package com.mnnyang.gzuclassschedule.data.http;

import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.app.Url;
import com.mnnyang.gzuclassschedule.data.beanv2.BaseBean;
import com.mnnyang.gzuclassschedule.data.beanv2.DownCourseWrapper;
import com.mnnyang.gzuclassschedule.data.beanv2.ShareBean;
import com.mnnyang.gzuclassschedule.data.beanv2.UserWrapper;
import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 登录和备份
 */
public class MyHttpUtils {
    public void uploadCourse(String json, final HttpCallback<BaseBean> callback) {
        OkHttpUtils.postString()
                .url(Url.URL_UPLOAD_COURSE)
                .content(json)
                .mediaType(MediaType.parse(Constant.CONTENT_TYPE_JSON))
                .build()
                .execute(new JsonCallback<BaseBean>(BaseBean.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        callback.onFail(e.getMessage());
                    }

                    @Override
                    public void onResponse(BaseBean response, int id) {
                        callback.onSuccess(response);
                        LogUtil.e(this, "onResponse-->" + response.toString());
                    }
                });
    }


    public void uploadShare(String json, final HttpCallback<ShareBean> callback) {
        OkHttpUtils.postString()
                .url(Url.URL_SHARE)
                .content(json)
                .mediaType(MediaType.parse(Constant.CONTENT_TYPE_JSON))
                .build()
                .execute(new JsonCallback<ShareBean>(ShareBean.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        callback.onFail(e.getMessage());
                    }

                    @Override
                    public void onResponse(ShareBean response, int id) {
                        callback.onSuccess(response);
                        LogUtil.e(this, "onResponse-->" + response.toString());
                    }
                });
    }


    public void downCourse(final HttpCallback<DownCourseWrapper> callback) {
        OkHttpUtils.post()
                .url(Url.URL_DOWN_COURSE)
                .build()
                .execute(new JsonCallback<DownCourseWrapper>(DownCourseWrapper.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        callback.onFail(e.getMessage());
                    }

                    @Override
                    public void onResponse(DownCourseWrapper response, int id) {
                        callback.onSuccess(response);
                        LogUtil.e(this, "onResponse-->" + response.toString());
                    }
                });
    }

    public void downShare(String shareUrl, final HttpCallback<DownCourseWrapper> callback) {
        OkHttpUtils.get()
                .url(shareUrl)
                .build()
                .execute(new JsonCallback<DownCourseWrapper>(DownCourseWrapper.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        callback.onFail(e.getMessage());
                    }

                    @Override
                    public void onResponse(DownCourseWrapper response, int id) {
                        callback.onSuccess(response);
                        LogUtil.e(this, "onResponse-->" + response.toString());
                    }
                });
    }

    public void login(String email, String password, final HttpCallback<BaseBean> callback) {
        OkHttpUtils.post()
                .url(Url.URL_LOGIN)
                .addHeader("HTTP_X_REQUESTED_WITH", "XMLHttpRequest")
                .addParams("username", email)
                .addParams("password", password)
                .build()
                .execute(new JsonCallback<BaseBean>(BaseBean.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        callback.onFail(e.getMessage());
                    }

                    @Override
                    public void onResponse(BaseBean response, int id) {
                        callback.onSuccess(response);
                    }
                });
    }

    public void register(String email, String password, final HttpCallback<BaseBean> callback) {
        OkHttpUtils.post()
                .url(Url.URL_REGISTER)
                .addHeader("HTTP_X_REQUESTED_WITH", "XMLHttpRequest")
                .addParams("username", email)
                .addParams("password", password)
                .build()
                .execute(new JsonCallback<BaseBean>(BaseBean.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        callback.onFail(e.getMessage());
                    }

                    @Override
                    public void onResponse(BaseBean response, int id) {
                        callback.onSuccess(response);
                    }
                });
    }

    public void userInfo(final HttpCallback<UserWrapper> callback) {
        OkHttpUtils.get()
                .url(Url.URL_USER_INFO)
                .build()
                .execute(new JsonCallback<UserWrapper>(UserWrapper.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        callback.onFail(e.getMessage());
                    }

                    @Override
                    public void onResponse(UserWrapper response, int id) {
                        callback.onSuccess(response);
                    }
                });
    }


}
