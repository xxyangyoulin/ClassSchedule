package com.mnnyang.gzuclassschedule.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Url;
import com.mnnyang.gzuclassschedule.app.app;
import com.mnnyang.gzuclassschedule.utils.CourseParse;
import com.mnnyang.gzuclassschedule.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;

/**
 * Created by mnnyang on 17-10-19.
 */

public class HttpUtils {
    public static final String ACCESS_ERR = "抱歉,访问出错,请重试";

    private HttpUtils() {
    }

    private static class Holder {
        private static final HttpUtils HTTP_UTILS = new HttpUtils();
    }

    public static HttpUtils newInstance() {
        return Holder.HTTP_UTILS;
    }


    public void loadCaptcha(final File dir, final HttpCallback<Bitmap> callback) {
        OkHttpUtils.post().url(Url.URL_LOGIN_PAGE)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onFail(ACCESS_ERR);
            }

            @Override
            public void onResponse(String response, int id) {
                Url.VIEWSTATE_LOGIN_CODE = CourseParse.parseViewStateCode(response);
                LogUtils.d(this, "login_code:" + Url.VIEWSTATE_LOGIN_CODE);
                toLoadCaptcha(dir, callback);
            }
        });
    }

    private void toLoadCaptcha(final File dir, final HttpCallback<Bitmap> callback) {
        OkHttpUtils.get().url(Url.URL_CHECK_CODE).build().execute(
                new FileCallBack(dir.getAbsolutePath(), "loadCaptcha.jpg") {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        callback.onFail(ACCESS_ERR);
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Bitmap bitmap = BitmapFactory.decodeFile(
                                dir.getAbsolutePath() + "/loadCaptcha.jpg");
                        callback.onSuccess(bitmap);
                    }
                });
    }

    public void login(final String xh, String passwd, String catpcha,
                      final String courseTime, final String term,
                      final HttpCallback<String> callback) {
        OkHttpUtils.post().url(Url.URL_LOGIN_PAGE)
                .addParams("__VIEWSTATE", Url.VIEWSTATE_LOGIN_CODE)
                .addParams("txtUserName", xh)
                .addParams("Textbox1", "")
                .addParams("Textbox2", passwd)
                .addParams("txtSecretCode", catpcha)
                .addParams("RadioButtonList1", "学生")
                .addParams("Button1", "")
                .addParams("lbLanguage", "")
                .addParams("hidPdrs	", "")
                .addParams("hidsc", "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                callback.onFail(ACCESS_ERR);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.e(this, response);

                if (response.contains(app.mContext.getString(R.string.captcha_err))) {
                    callback.onFail(app.mContext.getString(R.string.captcha_err));
                } else if (response.contains(app.mContext.getString(R.string.pwd_err))) {
                    callback.onFail(app.mContext.getString(R.string.pwd_err));
                } else {
                    if (TextUtils.isEmpty(courseTime) || TextUtils.isEmpty(term)) {
                        toImpt(xh, callback);
                        return;
                    }
                    toImpt(xh, courseTime, term, callback);
                }
            }
        });
    }

    /**
     * get normal
     *
     * @param xh
     * @param callback
     */
    public void toImpt(String xh, final HttpCallback<String> callback) {
        LogUtils.d(this, "get normal+"+xh);
        OkHttpUtils.get().url(Url.URL_LOAD_COURSE)
                .addHeader("Referer", Url.URL_LOAD_COURSE + "?xh=" + xh)
                .addParams(Url.PARAM_XH, xh)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        callback.onFail(ACCESS_ERR);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println(response);
                        Url.VIEWSTATE_POST_CODE = CourseParse.parseViewStateCode(response);
                        callback.onSuccess(response);
                    }
                });
    }

    /**
     * @param xh
     * @param courseTime 格式: 2017-2018
     * @param term       学期num
     * @param callback
     */
    public void toImpt(String xh, String courseTime, String term,
                       final HttpCallback<String> callback) {
        LogUtils.d("toImpt","xh"+xh+"c"+courseTime+"t"+term);
        OkHttpUtils.post().url(Url.URL_LOAD_COURSE + "?xh=" + xh + "&xm=%D1%EE%D3%D1%C1%D6&gnmkdm=N121603")
                .addHeader("Referer", Url.URL_LOAD_COURSE + "?xh=" + xh + "&xm=%D1%EE%D3%D1%C1%D6&gnmkdm=N121603")
                .addHeader("Connection", "keep-alive")

                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
                .addHeader("Cache-Control", "max-age=0")
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Length", "5443")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Host", "210.40.2.253:8888")
                .addHeader("Origin", "http://210.40.2.253:8888")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36")



                .addParams("__EVENTTARGET", "xnd")
                .addParams("__EVENTARGUMENT", "")
                .addParams("__VIEWSTATE", Url.VIEWSTATE_POST_CODE)
                .addParams(Url.PARAM_XND, courseTime)
                .addParams(Url.PARAM_XQD, term)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        callback.onFail(ACCESS_ERR);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Url.VIEWSTATE_POST_CODE = CourseParse.parseViewStateCode(response);
                        callback.onSuccess(response);
                    }
                });
    }
}


