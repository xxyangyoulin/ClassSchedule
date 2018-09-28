package com.mnnyang.gzuclassschedule.mvp.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mnnyang.gzuclassschedule.BaseFragment;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Cache;
import com.mnnyang.gzuclassschedule.data.beanv2.UserWrapper;
import com.mnnyang.gzuclassschedule.mvp.login.LoginActivity;
import com.mnnyang.gzuclassschedule.mvp.mg.MgActivity;
import com.mnnyang.gzuclassschedule.mvp.setting.SettingActivity;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.mnnyang.gzuclassschedule.utils.RequestPermission;
import com.mnnyang.gzuclassschedule.utils.ScreenUtils;
import com.mnnyang.gzuclassschedule.utils.event.CourseDataChangeEvent;
import com.mnnyang.gzuclassschedule.utils.event.LoginEvent;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.mnnyang.gzuclassschedule.mvp.home.HomePresenter.getGravatar;

public class HomeFragment extends BaseFragment implements HomeContract.View, View.OnClickListener {

    HomeContract.Presenter mPresenter;
    private int REQUEST_CODE_SCAN = 1;
    private View mViewShare;
    private DialogHelper mProgressDialog;
    private LinearLayout mLayoutUpload;
    private LinearLayout mLayoutOverwriteLocal;
    private Toolbar mToolbar;
    private CircleImageView mCivAvator;
    private View mLayoutSetting;
    private View mLayoutCourseMg;
    private TextView mTvUsername;

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EvenBus
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {
        mToolbar = mRootView.findViewById(R.id.toolbar);
        mTvUsername = mRootView.findViewById(R.id.tv_username);
        mViewShare = mRootView.findViewById(R.id.layout_share);
        mLayoutSetting = mRootView.findViewById(R.id.layout_setting);
        mLayoutCourseMg = mRootView.findViewById(R.id.layout_course_mg);
        mLayoutOverwriteLocal = mRootView.findViewById(R.id.layout_overwrite_local);
        mLayoutUpload = mRootView.findViewById(R.id.layout_upload);

        mCivAvator = mRootView.findViewById(R.id.profile_image);

        backToolbar(mToolbar);
        initToolbarMenu();
    }

    private void initToolbarMenu() {
        mToolbar.inflateMenu(R.menu.toolbar_home);
    }

    @Override
    public void initData() {
        new HomePresenter(this).start();
    }

    @Override
    public void initListener() {
        mViewShare.setOnClickListener(this);
        mLayoutOverwriteLocal.setOnClickListener(this);
        mLayoutUpload.setOnClickListener(this);
        mLayoutSetting.setOnClickListener(this);
        mLayoutCourseMg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_share:
                showShareDialog();
                break;
            case R.id.layout_overwrite_local:
                mPresenter.cloudOverWriteLocal();
                break;
            case R.id.layout_upload:
                mPresenter.uploadLocalCourse();
                break;
            case R.id.layout_setting:
                setting();
                break;
            case R.id.layout_course_mg:
                courseManage();
                break;

        }
    }

    private void courseManage() {
        startActivity(new Intent(activity, MgActivity.class));
    }

    private void setting() {
        startActivity(new Intent(activity, SettingActivity.class));
    }

    private void showShareDialog() {
        final DialogHelper dialogHelper = new DialogHelper();
        View dialogView = View.inflate(getContext(), R.layout.dialog, null);
        dialogView.findViewById(R.id.layout_create_qr_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHelper.hideCustomDialog();
                mPresenter.createQRCode();

            }
        });

        dialogView.findViewById(R.id.layout_scan_qr_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHelper.hideCustomDialog();
                scanQRCode();
            }
        });

        dialogHelper.showCustomDialog(Objects.requireNonNull(getContext()),
                dialogView, null, ScreenUtils.dp2px(260), null);
    }

    /**
     * 扫描二维码
     */
    private void scanQRCode() {
        RequestPermission.with(this).permissions(Manifest.permission.CAMERA)
                .request(new RequestPermission.Callback() {
                    @Override
                    public void onGranted() {
                        Intent intent = new Intent(activity, CaptureActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_SCAN);
                    }

                    @Override
                    public void onDenied() {
                        Toast.makeText(activity, "没有打开相机的权限", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RequestPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void showLoading(String msg) {
        mProgressDialog = new DialogHelper();
        //TODO 弹出不可取消
        mProgressDialog.showProgressDialog(getContext(), "稍后", msg, false);
    }

    @Override
    public void stopLoading() {
        mProgressDialog.hideProgressDialog();
    }

    @Override
    public void noSignInPage() {
        mTvUsername.setText("未登录");
        mTvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, LoginActivity.class));
            }
        });
        //mCivAvator
    }

    @Override
    public void signedInPage() {
        mTvUsername.setOnClickListener(null);
    }

    @Override
    public void userInfoSucceed(UserWrapper.User user) {
        mTvUsername.setText(user.getEmail());
        updateShowAvator();
        Toast.makeText(activity, user.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateShowAvator() {
        String email = Cache.instance().getEmail();
        if (!TextUtils.isEmpty(email)) {
            String gravatar = getGravatar(email);
            System.out.println("-----------" + gravatar);
            Glide.with(getContext())
                    .load(gravatar)
                    .into(mCivAvator);
        }
    }

    @Override
    public void createQRCodeSucceed(Bitmap bitmap) {
        DialogHelper dialogHelper = new DialogHelper();
        View dialogView = View.inflate(getContext(), R.layout.dialog_qr_code, null);

        ((ImageView) dialogView.findViewById(R.id.iv_qr_code)).setImageBitmap(bitmap);

        dialogHelper.showCustomDialog(Objects.requireNonNull(getContext()),
                dialogView, null, ScreenUtils.dp2px(180), null);
    }

    @Override
    public void createQRCodeFailed(String msg) {
        toast(msg);
    }

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginEvent event) {
        mPresenter.loadUserInfo();
        LogUtil.e(this,"收到事件");
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void close() {
        super.close();
        activity.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //EvenBus
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
