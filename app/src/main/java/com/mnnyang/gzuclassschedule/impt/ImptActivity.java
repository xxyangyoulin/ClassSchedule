package com.mnnyang.gzuclassschedule.impt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.course.CourseActivity;
import com.mnnyang.gzuclassschedule.data.bean.CourseTime;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.ScreenUtils;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;
import com.mnnyang.gzuclassschedule.utils.spec.ShowTermDialog;

public class ImptActivity extends BaseActivity implements
        ImptContract.View, View.OnClickListener, View.OnFocusChangeListener {

    ImptContract.Presenter mPresenter;
    private ImageView mIvCaptcha;
    private EditText mEtXh;
    private EditText mEtPwd;
    private String mXh;
    private DialogHelper mHelper;
    private ImageView mIvClearXh;
    private ImageView mIvClearPwd;
    private ImageView mIvClearCaptcha;
    private EditText mEtCaptcha;

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
        mEtXh = (EditText) findViewById(R.id.et_xh);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mEtCaptcha = (EditText) findViewById(R.id.et_captcha);

        Button btnSkip = (Button) findViewById(R.id.btn_skip);
        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        LinearLayout layoutCaptcha = (LinearLayout) findViewById(R.id.layout_refresh_captcha);

        mIvClearXh = (ImageView) findViewById(R.id.iv_clear_xh);
        mIvClearPwd = (ImageView) findViewById(R.id.iv_clear_pwd);
        mIvClearCaptcha = (ImageView) findViewById(R.id.iv_clear_captcha);

        mEtXh.setText(Preferences.getString(Constant.XH, ""));

        mEtXh.setOnFocusChangeListener(this);
        mEtPwd.setOnFocusChangeListener(this);
        mEtCaptcha.setOnFocusChangeListener(this);


        mIvClearXh.setOnClickListener(this);
        mIvClearPwd.setOnClickListener(this);
        mIvClearCaptcha.setOnClickListener(this);

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
                mPresenter.start();
                break;
            case R.id.iv_clear_xh:
                mEtXh.setText("");
                break;
            case R.id.iv_clear_pwd:
                mEtPwd.setText("");
                break;
            case R.id.iv_clear_captcha:
                mEtCaptcha.setText("");
                break;
        }
    }

    private void confirm() {
        //TODO 数据验证
        mXh = mEtXh.getText().toString().trim();
        String pwd = mEtPwd.getText().toString().trim();
        String captcha = mEtCaptcha.getText().toString().trim();

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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_xh:
                mIvClearXh.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                mEtXh.setHint(hasFocus ? "" : getString(R.string.xh));
                break;
            case R.id.et_pwd:
                mIvClearPwd.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                mEtPwd.setHint(hasFocus ? "" : getString(R.string.pwd));
                break;
            case R.id.et_captcha:
                mIvClearCaptcha.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                mEtCaptcha.setHint(hasFocus ? "" : getString(R.string.chptcha));
                break;
            default:
                break;
        }
    }

}
