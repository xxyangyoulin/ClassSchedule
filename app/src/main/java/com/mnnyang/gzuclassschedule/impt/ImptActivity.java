package com.mnnyang.gzuclassschedule.impt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.course.CourseActivity;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.ScreenUtils;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;

public class ImptActivity extends BaseActivity implements ImptContract.View, View.OnClickListener {

    ImptContract.Presenter mPresenter;
    private ImageView mIvCaptcha;
    private EditText mEtXh;
    private EditText mEtPwd;
    private String mXh;
    private DialogHelper mHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.setSystemBarTransparent(this);
        setContentView(R.layout.activity_impt);

        initToolbar();
        initView();

        mPresenter = new ImptPresenter(this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("导入课表");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        mIvCaptcha = (ImageView) findViewById(R.id.iv_captcha);
        mEtXh = (EditText) findViewById(R.id.et_xh);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);

        Button btnSkip = (Button) findViewById(R.id.btn_skip);
        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        LinearLayout layoutCaptcha = (LinearLayout) findViewById(R.id.layout_refresh_captcha);

        mEtXh.setText(Preferences.getString(Constant.XH, ""));

        btnSkip.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        layoutCaptcha.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public ImageView getCaptchaIV() {
        return mIvCaptcha;
    }

    @Override
    public void showImpting() {
        mHelper = new DialogHelper();
        mHelper.showProgressDialog(this,
                "导入中", "请稍等...", false);
    }

    @Override
    public void hideImpting() {
        if (mHelper != null) mHelper.hideProgressDialog();
    }

    @Override
    public void showFail(String errMsg) {
        ToastUtils.show(errMsg);
        reLoadCaptcha();
    }

    private void reLoadCaptcha() {
        mPresenter.getCaptcha();
    }

    @Override
    public void showSucceed() {
        ToastUtils.show("导入成功");
        Preferences.putString(Constant.XH, mXh);
        Intent intent = new Intent(this, CourseActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_skip:
                skip();
                break;
            case R.id.btn_confirm:
                confirm();
                break;
            case R.id.layout_refresh_captcha:
                mPresenter.start();
                break;
        }
    }

    private void confirm() {
        EditText etCaptcha = (EditText) findViewById(R.id.et_captcha);

        //TODO 数据验证
        mXh = mEtXh.getText().toString().trim();
        String pwd = mEtPwd.getText().toString().trim();
        String captcha = etCaptcha.getText().toString().trim();

        mPresenter.importCourses(mXh, pwd, captcha);
    }

    private void skip() {
        Intent intent = new Intent(this, CourseActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
