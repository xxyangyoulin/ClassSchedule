package com.mnnyang.gzuclassschedule.mvp.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mnnyang.gzuclassschedule.BaseFragment;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.AppUtils;
import com.mnnyang.gzuclassschedule.app.Cache;
import com.mnnyang.gzuclassschedule.data.beanv2.CourseGroup;
import com.mnnyang.gzuclassschedule.data.beanv2.UserWrapper;
import com.mnnyang.gzuclassschedule.mvp.add.AddActivity;
import com.mnnyang.gzuclassschedule.mvp.login.SignActivity;
import com.mnnyang.gzuclassschedule.mvp.mg.MgActivity;
import com.mnnyang.gzuclassschedule.mvp.school.SchoolActivity;
import com.mnnyang.gzuclassschedule.mvp.setting.SettingActivity;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.DialogListener;
import com.mnnyang.gzuclassschedule.utils.RequestPermission;
import com.mnnyang.gzuclassschedule.utils.ScreenUtils;
import com.mnnyang.gzuclassschedule.utils.event.SignEvent;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends BaseFragment implements HomeContract.View,
        View.OnClickListener, Toolbar.OnMenuItemClickListener {

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
    private View mViewAdd;

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showMassage(String msg) {
        toast(msg);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {
        mToolbar = mRootView.findViewById(R.id.toolbar);
        mTvUsername = mRootView.findViewById(R.id.tv_username);
        mViewAdd = mRootView.findViewById(R.id.layout_add);
        mViewShare = mRootView.findViewById(R.id.layout_share);
        mLayoutSetting = mRootView.findViewById(R.id.layout_setting);
        mLayoutCourseMg = mRootView.findViewById(R.id.layout_course_mg);
        mLayoutOverwriteLocal = mRootView.findViewById(R.id.layout_overwrite_local);
        mLayoutUpload = mRootView.findViewById(R.id.layout_upload);

        mCivAvator = mRootView.findViewById(R.id.profile_image);

        initToolbar();
    }

    private void initToolbar() {
        backToolbar(mToolbar);
        mToolbar.inflateMenu(R.menu.toolbar_home);
        mToolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public void initData() {
        new HomePresenter(this).start();
        showCacheData();
    }

    @Override
    public void initListener() {
        mViewAdd.setOnClickListener(this);
        mViewShare.setOnClickListener(this);
        mLayoutOverwriteLocal.setOnClickListener(this);
        mLayoutUpload.setOnClickListener(this);
        mLayoutSetting.setOnClickListener(this);
        mLayoutCourseMg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_add:
                addSelectDialog();
                break;
            case R.id.layout_share:
                showShareDialog();
                break;

            case R.id.layout_overwrite_local:
                mPresenter.downCourse();
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
            default:
                break;
        }
    }

    private void addSelectDialog() {
        DialogHelper helper = new DialogHelper();
        String[] selectAddMenu = {"手动添加", "导入方正课表"};
        helper.showListDialog(activity, null, selectAddMenu, new DialogListener() {
            @Override
            public void onItemClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        userAdd();
                        break;
                    case 1:
                        autoAdd();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void autoAdd() {
        startActivity(new Intent(activity, SchoolActivity.class));
    }

    private void userAdd() {
        startActivity(new Intent(activity, AddActivity.class));
    }

    private void courseManage() {
        startActivity(new Intent(activity, MgActivity.class));
    }

    private void setting() {
        startActivity(new Intent(activity, SettingActivity.class));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                mPresenter.downShare(content);
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
    public void showCacheData() {
        String email = Cache.instance().getEmail();
        if (!TextUtils.isEmpty(email)) {
            UserWrapper.User user = new UserWrapper.User();
            user.setEmail(email);

            signInPage(user);
        }
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
        if (!isActive()) {
            return;
        }
        hideSignOutMenu(false);
        mTvUsername.setText("未登录");
        mTvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, SignActivity.class));
            }
        });

        // default avator
        mCivAvator.setImageResource(R.drawable.ic_avator_black_24dp);
    }

    @Override
    public void signInPage(UserWrapper.User user) {
        if (!isActive()) {
            return;
        }
        hideSignOutMenu(true);
        mTvUsername.setOnClickListener(null);
        mTvUsername.setText(user.getEmail());

        updateShowAvator(user.getEmail());
    }

    @Override
    public void pleaseLoginIn() {
        if (!isActive()) {
            return;
        }
        toast("请登录");
        startActivity(new Intent(activity, SignActivity.class));
    }

    @Override
    public void updateShowAvator(@NonNull String email) {
        if (!isActive()) {
            return;
        }
        String grAvatar = AppUtils.getGravatar(email);
        Glide.with(Objects.requireNonNull(getContext()))
                .load(grAvatar)
                .into(mCivAvator);
    }

    /**
     * 二维码生成成功
     */
    @Override
    public void createQRCodeSucceed(Bitmap bitmap) {
        if (!isActive()) {
            return;
        }

        DialogHelper dialogHelper = new DialogHelper();
        View dialogView = View.inflate(getContext(), R.layout.dialog_qr_code, null);

        ((ImageView) dialogView.findViewById(R.id.iv_qr_code)).setImageBitmap(bitmap);

        dialogHelper.showCustomDialog(Objects.requireNonNull(getContext()),
                dialogView, null, ScreenUtils.dp2px(220), null);
    }

    @Override
    public void cloudToLocalSucceed() {
        startActivity(new Intent(activity, MgActivity.class));
        activity.finish();
    }

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //登录事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(SignEvent event) {
        activity.recreate();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     * 显示分享弹窗
     */
    private void showShareDialog() {
        final DialogHelper dialogHelper = new DialogHelper();
        View dialogView = View.inflate(getContext(), R.layout.dialog, null);
        dialogView.findViewById(R.id.layout_create_qr_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHelper.hideCustomDialog();
//                mPresenter.createQRCode(getResources());
                mPresenter.showCourseGroup();
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
    public void showGroupDialog(List<CourseGroup> groups) {
        DialogHelper helper = new DialogHelper();
        final String[] items = new String[groups.size()];
        final long[] ids = new long[items.length];

        for (int i = 0; i < groups.size(); i++) {
            items[i] = groups.get(i).getCgName();
            ids[i] = groups.get(i).getCgId();
        }

        helper.showListDialog(activity, "选择要分享的课表", items, new DialogListener() {
            @Override
            public void onItemClick(DialogInterface dialog, int which) {
                super.onItemClick(dialog, which);
                mPresenter.createShare(ids[which], items[which]);
            }
        });
    }

    private void hideSignOutMenu(boolean hide) {
        if (TextUtils.isEmpty(Cache.instance().getEmail())) {
            mToolbar.getMenu().findItem(R.id.action_sign_out).setVisible(hide);
        }
    }

    @Override
    public void close() {
        super.close();
        activity.finish();
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
        //EvenBus
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                signOut();
                break;
        }
        return false;
    }

    private void signOut() {
        Cache.instance().clearCookie().setEmail(null);

        SignEvent event = new SignEvent().setSignOut(true);
        EventBus.getDefault().post(event);
    }
}
