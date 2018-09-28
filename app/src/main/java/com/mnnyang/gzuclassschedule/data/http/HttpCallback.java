package com.mnnyang.gzuclassschedule.data.http;

public abstract class HttpCallback<T> {
    public abstract void onSuccess(T t);

    public abstract void onFail(String errMsg);
}