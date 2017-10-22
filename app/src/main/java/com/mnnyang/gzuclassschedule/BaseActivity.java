package com.mnnyang.gzuclassschedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mnnyang.gzuclassschedule.utils.LogUtils;


/**
 * Created by mnnyang on 17-10-2.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d(TAG, "onCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy");
    }
}
