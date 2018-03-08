package com.mnnyang.gzuclassschedule.impt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.course.CourseActivity;
import com.mnnyang.gzuclassschedule.custom.EditTextLayout;
import com.mnnyang.gzuclassschedule.data.bean.CourseTime;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.ScreenUtils;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;
import com.mnnyang.gzuclassschedule.utils.spec.ShowTermDialog;

public class ImptActivity extends BaseActivity implements
        ImptContract.View, View.OnClickListener {

    ImptContract.Presenter mPresenter;
    private ImageView mIvCaptcha;
    private String mXh;
    private DialogHelper mHelper;
    private EditTextLayout mEtlXh;
    private EditTextLayout mEtlPwd;
    private EditTextLayout mEtlCaptcha;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.setSystemBarTransparent(this);
        setContentView(R.layout.activity_impt);

        initBackToolbar("导入课程表");
        initView();

        mPresenter = new ImptPresenter(this);
    }

    private void initView() {
        mIvCaptcha = (ImageView) findViewById(R.id.iv_captcha);
        mEtlXh = findViewById(R.id.etl_xh);
        mEtlPwd = findViewById(R.id.etl_pwd);
        mEtlCaptcha = findViewById(R.id.etl_captcha);

        Button btnSkip = (Button) findViewById(R.id.btn_skip);
        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        LinearLayout layoutCaptcha = (LinearLayout) findViewById(R.id.layout_refresh_captcha);

        mEtlXh.setText(Preferences.getString(Constant.XH, ""));
        mEtlPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        btnSkip.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        layoutCaptcha.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAction();
    }

    private void startAction() {
        mPresenter.start();
    }

    @Override
    public ImageView getCaptchaIV() {
        return mIvCaptcha;
    }

    @Override
    public void showImpting() {
        mHelper = new DialogHelper();
        mHelper.showProgressDialog(this, "导入中", "请稍等...", false);
    }

    @Override
    public void hideImpting() {
        LogUtil.d(this, "hideimp");
        if (mHelper != null) mHelper.hideProgressDialog();
    }

    @Override
    public void showErrToast(String errMsg, boolean reLoad) {
        ToastUtils.show(errMsg);
        if (reLoad) reLoadCaptcha();
    }

    private void reLoadCaptcha() {
        mPresenter.getCaptcha();
    }

    @Override
    public void showSucceed() {
        notifiUpdateMainPage(Constant.INTENT_UPDATE_TYPE_COURSE);
        ToastUtils.show("导入成功");
        Preferences.putString(Constant.XH, mXh);
        Intent intent = new Intent(this, CourseActivity.class);
        startActivity(intent);
        finish();
    }

    String selectedTime, selectedTerm;

    @Override
    public void showCourseTimeDialog(CourseTime ct) {
        new ShowTermDialog().showSelectTimeTermDialog(this,
                ct.years.toArray(new String[0]), new ShowTermDialog.TimeTermCallback() {
                    @Override
                    public void onTimeChanged(String time) {
                        selectedTime = time;
                    }

                    @Override
                    public void onTermChanged(String term) {
                        selectedTerm = term;
                    }

                    @Override
                    public void onPositive(DialogInterface dialog, int which) {
                        mPresenter.importCustomCourses(selectedTime, selectedTerm);
                    }
                });
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
                startAction();
                break;
        }
    }

    private void confirm() {
        //TODO 数据验证
        mXh = mEtlXh.getText().trim();
        String pwd = mEtlPwd.getText().trim();
        LogUtil.e(this,"passwd="+pwd);
        String captcha = mEtlCaptcha.getText().trim();

        mPresenter.loadCourseTimeAndTerm(mXh, pwd, captcha);
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
