package com.mnnyang.gzuclassschedule.mg;

import android.os.Bundle;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;

public class CourseMgActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        initBackToolbar("课表管理");
    }
}
