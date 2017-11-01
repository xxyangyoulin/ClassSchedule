package com.mnnyang.gzuclassschedule.http;

public abstract class HttpCallback<T> {
    public abstract void onSuccess(T t);

    public abstract void onFail(Exception e);
}