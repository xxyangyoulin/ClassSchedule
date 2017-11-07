package com.mnnyang.gzuclassschedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.mg.MgActivity;
import com.mnnyang.gzuclassschedule.utils.LogUtils;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;


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

    protected void initBackToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    public void gotoActivity(Class clzz) {
        Intent intent = new Intent(this, clzz);
        startActivity(intent);
    }

    /**
     * 通知更新
     */
    public void notifiUpdateMainPage(int type) {
        Intent intent = new Intent();
        intent.setAction(Constant.INTENT_UPDATE);
        intent.putExtra(Constant.INTENT_UPDATE_TYPE, type);
        sendBroadcast(intent);
    }

    public void toast(String msg) {
        ToastUtils.show(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy");
    }
}
