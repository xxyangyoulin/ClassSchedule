package com.mnnyang.gzuclassschedule.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mnnyang.gzuclassschedule.app.Url;
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
    public static final String ACCESS_ERR = "访问故障,请重试";

    private HttpUtils() {
    }

    private static class Holder {
        private static final HttpUtils HTTP_UTILS = new HttpUtils();
    }

    public static HttpUtils newInstance() {
        return Holder.HTTP_UTILS;
    }


    public void captcha(final File dir, final HttpCallback<Bitmap> callback) {
        OkHttpUtils.post().url(Url.URL_LOGIN_PAGE)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onFail(ACCESS_ERR);
            }

            @Override
            public void onResponse(String response, int id) {
                toLoadCaptcha(dir, callback);
            }
        });
    }

    private void toLoadCaptcha(final File dir, final HttpCallback<Bitmap> callback) {
        OkHttpUtils.get().url(Url.URL_CHECK_CODE).build().execute(
                new FileCallBack(dir.getAbsolutePath(), "captcha.jpg") {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        callback.onFail(ACCESS_ERR);
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Bitmap bitmap = BitmapFactory.decodeFile(
                                dir.getAbsolutePath() + "/captcha.jpg");
                        callback.onSuccess(bitmap);
                    }
                });
    }

    public void impt(final String xh, String passwd,
                     String catpcha, final HttpCallback<String> callback) {
        OkHttpUtils.post().url(Url.URL_LOGIN_PAGE)
                .addParams("__VIEWSTATE", "dDwtNTE2MjI4MTQ7Oz4I55DQ6KPcVdzTLmjGjlJPRWgYUQ==")
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
//                callback.onSuccess(response);
                if (response.contains("验证码不正确")) {
                    callback.onFail("验证码不正确");
                } else if (response.contains("密码错误")) {
                    callback.onFail("密码错误");
                } else {
                    toImpt(xh, callback);
                }
            }
        });
    }

    private void toImpt(String xh, final HttpCallback<String> callback) {
        OkHttpUtils.get().url(Url.URL_LOAD_COURSE)
                .addHeader("Referer", "http://210.40.2.253:8888/xskbcx.aspx?xh=1500170110")
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
                        callback.onSuccess(response);
                    }
                });
    }


}
