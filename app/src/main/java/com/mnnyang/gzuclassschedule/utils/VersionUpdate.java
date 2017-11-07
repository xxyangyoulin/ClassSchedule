package com.mnnyang.gzuclassschedule.utils;

import com.google.gson.Gson;
import com.mnnyang.gzuclassschedule.app.Url;
import com.mnnyang.gzuclassschedule.data.bean.Version;
import com.mnnyang.gzuclassschedule.http.HttpCallback;
import com.mnnyang.gzuclassschedule.http.HttpUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by mnnyang on 17-11-7.
 */

public class VersionUpdate {

    public void checkUpdate(final HttpCallback<Version> callback){
        OkHttpUtils.get().url(Url.URL_CHECK_UPDATE_APP)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                callback.onFail(e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    Gson gson = new Gson();
                    Version version = gson.fromJson(response, Version.class);
                    callback.onSuccess(version);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail("parse error");
                }
            }
        });
    }
}
