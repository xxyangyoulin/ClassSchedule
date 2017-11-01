package com.mnnyang.gzuclassschedule.guide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.course.CourseActivity;
import com.mnnyang.gzuclassschedule.impt.ImptActivity;

public class GuideActivity extends BaseActivity implements View.OnClickListener {


    private Button mBtnGzuLogin;
    private Button mBtnInto;
    private Intent mIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
    }

    private void initView() {
        mBtnInto = (Button) findViewById(R.id.btn_into);
        mBtnGzuLogin = (Button) findViewById(R.id.btn_gzu_login);
        mBtnInto.setOnClickListener(this);
        mBtnGzuLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_into:
                mIntent = new Intent(GuideActivity.this, CourseActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_gzu_login:
                mIntent = new Intent(GuideActivity.this, ImptActivity.class);
                startActivity(mIntent);
                break;
        }
    }
}
