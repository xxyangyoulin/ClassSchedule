package com.mnnyang.gzuclassschedule.mvp.setting;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.didikee.donate.AlipayDonate;
import android.didikee.donate.WeiXinDonate;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.mvp.about.AboutActivity;
import com.mnnyang.gzuclassschedule.mvp.add.AddActivity;
import com.mnnyang.gzuclassschedule.app.app;
import com.mnnyang.gzuclassschedule.mvp.course.CourseActivity;
import com.mnnyang.gzuclassschedule.custom.settting.SettingItemNormal;
import com.mnnyang.gzuclassschedule.mvp.mg.MgActivity;
import com.mnnyang.gzuclassschedule.mvp.school.SchoolActivity;
import com.mnnyang.gzuclassschedule.utils.ActivityUtil;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.DialogListener;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.RequestPermission;
import com.mnnyang.gzuclassschedule.utils.ScreenUtils;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;
import com.mnnyang.gzuclassschedule.utils.VersionUpdate;

import java.io.File;
import java.io.InputStream;

import static com.mnnyang.gzuclassschedule.app.Constant.themeColorArray;
import static com.mnnyang.gzuclassschedule.app.Constant.themeNameArray;

public class SettingActivity extends BaseActivity implements SettingContract.View,
        SettingItemNormal.SettingOnClickListener {
    private SettingContract.Presenter mPresenter;

    private SettingItemNormal sinUserAdd;
    private SettingItemNormal sinImportGzu;
    private SettingItemNormal sinKbManage;

    private SettingItemNormal sinMorePref;
    private SettingItemNormal sinFeedback;
    private SettingItemNormal sinDonate;
    private SettingItemNormal sinAbout;
    private HorizontalScrollView hsvTheme;
    private LinearLayout layoutTheme;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initBackToolbar(getString(R.string.setting));

        initView();
        initDefaultValues();

        new SettingPresenter(this);
    }

    private void initView() {
        hsvTheme = findViewById(R.id.hsv_theme);
        layoutTheme = findViewById(R.id.layout_theme);

        sinUserAdd = findViewById(R.id.sin_user_add);
        sinImportGzu = findViewById(R.id.sin_import_gzu);
        sinKbManage = findViewById(R.id.sin_kb_manage);

        sinMorePref = findViewById(R.id.sin_more_pref);
        sinFeedback = findViewById(R.id.sin_feedback);
        sinDonate = findViewById(R.id.sin_donate);
        sinAbout = findViewById(R.id.sin_about);

        sinUserAdd.setSettingOnClickListener(this);
        sinImportGzu.setSettingOnClickListener(this);
        sinKbManage.setSettingOnClickListener(this);

        sinMorePref.setSettingOnClickListener(this);
        sinFeedback.setSettingOnClickListener(this);
        sinDonate.setSettingOnClickListener(this);
        sinAbout.setSettingOnClickListener(this);

        layoutTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showThemeDialog();
            }
        });
    }

    private void initDefaultValues() {
        VersionUpdate vu = new VersionUpdate();
        String versionName = vu.getLocalVersionName(app.mContext);
        sinAbout.setSummary(versionName);
    }

    @Override
    public void onClick(View view, boolean checked) {
        System.out.println(view);
        switch (view.getId()) {
            case R.id.sin_user_add:
                gotoAddActivity();
                break;
            case R.id.sin_import_gzu:
                importCourseTable();
                break;

            case R.id.sin_kb_manage:
                gotoMgActivity();
                break;

            case R.id.sin_more_pref:
                gotoConfActivity();
                break;
            case R.id.sin_feedback:
                mPresenter.feedback();
                break;
            case R.id.sin_donate:
                donate();
                break;
            case R.id.sin_about:
                gotoAboutActivity();
                break;

            case R.id.hsv_theme:
                showThemeDialog();
            default:
                break;
        }
    }

    private void donate() {
        DialogHelper helper = new DialogHelper();
        View view = View.inflate(this, R.layout.dialog_donate, null);
        view.findViewById(R.id.iv_alipay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donateAlipay(getString(R.string.alipay_qr_code));
            }
        });
        view.findViewById(R.id.iv_wechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donateWeixinRemind();
            }
        });

        helper.showCustomDialog(this, view, null, null);
    }

    private void donateWeixinRemind() {
        new AlertDialog.Builder(this)
                .setTitle("微信捐赠操作步骤")
                .setMessage("点击确定按钮后会跳转微信扫描二维码界面：\n\n" + "1. 点击右上角的菜单按钮\n\n" + "2. 点击'从相册选取二维码'\n\n" + "3. 选择第一张二维码图片即可\n\n")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        donateWeixin();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 支付宝支付
     *
     * @param payCode 收款码后面的字符串；例如：收款二维码里面的字符串为 https://qr.alipay.com/stx00187oxldjvyo3ofaw60 ，则
     *                payCode = stx00187oxldjvyo3ofaw60
     *                注：不区分大小写
     */
    private void donateAlipay(String payCode) {
        boolean hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(this);
        if (hasInstalledAlipayClient) {
            AlipayDonate.startAlipayClient(this, payCode);
        }
    }


    private void donateWeixin() {
        RequestPermission.with(this)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .request(new RequestPermission.Callback() {
                    @Override
                    public void onGranted() {
                        toDonateWeixin();
                    }

                    @Override
                    public void onDenied() {
                        toast("微信捐赠需要在内存中存入二维码图片");
                    }
                });
    }

    /**
     * 需要提前准备好 微信收款码 照片，可通过微信客户端生成
     */
    private void toDonateWeixin() {
        InputStream weixinQrIs = getResources().openRawResource(R.raw.weixin_donate);
        String qrPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "MDKeBiao" + File.separator +
                "didikee_weixin.png";
        WeiXinDonate.saveDonateQrImage2SDCard(qrPath, BitmapFactory.decodeStream(weixinQrIs));
        WeiXinDonate.donateViaWeiXin(this, qrPath);
    }

    @Override
    public void onCheckedChanged(View view, boolean checked) {

    }

    int theme;

    private void showThemeDialog() {
        ScrollView scrollView = new ScrollView(this);
        RadioGroup radioGroup = new RadioGroup(this);
        scrollView.addView(radioGroup);
        int margin = ScreenUtils.dp2px(16);
        radioGroup.setPadding(margin / 2, margin, margin, margin);

        for (int i = 0; i < themeColorArray.length; i++) {
            AppCompatRadioButton arb = new AppCompatRadioButton(this);

            RadioGroup.LayoutParams params =
                    new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);

            arb.setLayoutParams(params);
            arb.setId(i);
            arb.setTextColor(getResources().getColor(themeColorArray[i]));
            arb.setText(themeNameArray[i]);
            arb.setTextSize(16);
            arb.setPadding(0, margin / 2, 0, margin / 2);
            radioGroup.addView(arb);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                theme = checkedId;
            }
        });

        DialogHelper dialogHelper = new DialogHelper();
        dialogHelper.showCustomDialog(this, scrollView,
                getString(R.string.theme_preference), new DialogListener() {
                    @Override
                    public void onPositive(DialogInterface dialog, int which) {
                        super.onPositive(dialog, which);
                        dialog.dismiss();
                        String key = getString(R.string.app_preference_theme);
                        int oldTheme = Preferences.getInt(key, 0);

                        if (theme != oldTheme) {
                            Preferences.putInt(key, theme);
                            ActivityUtil.finishAll();
                            startActivity(new Intent(app.mContext, CourseActivity.class));
                        }
                    }
                });
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

    private void importCourseTable() {
        gotoActivity(SchoolActivity.class);
    }

    private void gotoConfActivity() {
        ToastUtils.show("还在开发中...");
    }

    private void gotoAboutActivity() {
        gotoActivity(AboutActivity.class);
    }

    private void gotoMgActivity() {
        gotoActivity(MgActivity.class);
    }

    public void gotoAddActivity() {
        gotoActivity(AddActivity.class);
    }

    @Override
    public void showNotice(String notice) {
        ToastUtils.show(notice);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RequestPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}
