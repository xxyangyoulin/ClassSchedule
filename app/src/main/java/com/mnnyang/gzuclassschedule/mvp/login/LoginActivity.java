package com.mnnyang.gzuclassschedule.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Cache;
import com.mnnyang.gzuclassschedule.data.beanv2.BaseBean;
import com.mnnyang.gzuclassschedule.mvp.home.HomeActivity;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;

public class LoginActivity extends BaseActivity implements LoginContact.View {

    private LoginContact.Presenter mPresenter;
    private boolean isLogin = false;
    private TextView mTvBigTitle;
    private TextView mTvSwitch;
    private TextView mTvTip;
    private Button mBtnGo;
    private TextInputEditText mEtEmail;
    private TextInputEditText mEtPassword;
    private DialogHelper mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initListener();
        initToSignUp();

        new LoginPresenter(this).start();
    }


    private void initView() {
        mTvBigTitle = findViewById(R.id.tv_big_title);
        mTvSwitch = findViewById(R.id.tv_switch);
        mTvTip = findViewById(R.id.tv_tip);
        mBtnGo = findViewById(R.id.btn_go);

        mEtEmail = findViewById(R.id.tiet_email);
        mEtPassword = findViewById(R.id.tiet_password);
    }

    private void initListener() {
        mTvSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchStatus();
            }
        });

        mBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEtEmail.getText().toString();
                String password = mEtPassword.getText().toString();

                if (isLogin) {
                    mPresenter.signIn(email, password);
                } else {
                    mPresenter.signUp(email, password);
                }
            }
        });
    }

    private void switchStatus() {
        if (isLogin) {
            initToSignUp();
        } else {
            initToSignIn();
        }
    }

    private void initToSignIn() {
        isLogin = true;
        mTvBigTitle.setText("Sign in");
        mTvTip.setText("还没有加入？");
        mTvSwitch.setText("去注册");
    }

    private void initToSignUp() {
        isLogin = false;
        mTvBigTitle.setText("Sign up");
        mTvTip.setText("已有账号？");
        mTvSwitch.setText("去登录");
    }

    @Override
    public void setPresenter(LoginContact.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading(String msg) {
        mProgressDialog = new DialogHelper();
        mProgressDialog.showProgressDialog(this, "稍等", msg, false);
    }

    @Override
    public void stopLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.hideProgressDialog();
            mProgressDialog = null;
        }
    }

    @Override
    public void signInSucceed(BaseBean bean) {
        toast("登录成功");
        String email = mEtEmail.getText().toString();
        Cache.instance().setEmail(email);

        startActivity(new Intent(this, HomeActivity.class));
    }

    @Override
    public void signInFailed(String msg) {
        toast(msg);
    }

    @Override
    public void signUpSucceed(BaseBean bean) {
        toast("注册成功，请登录");
        initToSignIn();
    }

    @Override
    public void signUpFailed(String msg) {
        toast(msg);
    }
}
